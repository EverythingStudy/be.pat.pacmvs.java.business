package cn.staitech.anno.config;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.MarkingMapperV1;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.project.service.SlideAttrService;
import cn.staitech.anno.utils.GeometryUtil;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.slide.SlideRes;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author: wangfeng
 * @create: 2023-06-21 14:28:47
 * @Description: 异步Task
 */

@Slf4j
@Service
public class AsyncTask {


    private static final int BATCH_SIZE = 5000;

    @Resource
    private SlideMapperV1 slideMapperV1;
    @Resource
    private SlideMapper slideMapper;
    @Resource
    private SlideAttrService slideAttrService;
    @Resource
    private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;
    @Resource
    private MarkingServiceV1 markingServiceV1;
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private MarkingMapperV1 markingMapperV1;

    /**
     * 异步删除文件
     *
     * @param file
     * @throws InterruptedException
     */
    @Async
    public void deleteFileTask(File file) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        for (; ; ) {
            Thread.sleep(1000);
            if (file.exists() && file.delete()) {
                long endTime = System.currentTimeMillis();
                log.info("[{}] async delete file success:{},cost {} ms,cas count:{}", Thread.currentThread().getName(), file.getAbsolutePath(), endTime - startTime, count.getAndIncrement());
                break;
            }
            if (count.getAndIncrement() > 5000) {
                break;
            }
        }
    }


    @SneakyThrows
    @Async
    //TODO1 解析json过程中无法标注
    //@Transactional
    public void zipExport(String zipUrl, Long projectId)  {
        File file1 = new File(zipUrl);
//        try {
            // 查询切片列表
            List<SlideRes> slideResList = slideMapper.selectImageList(projectId);
            //zip可以包含对个文件，如果只有一个文件，则只解析一个文件的，包含多个文件则分别解析
            //必须指明读取的各式，不然会存在问题
            ZipFile zipFile = new ZipFile(file1, Charset.forName("gbk"));
            //按流的方式读取文件，输入到管道中
            InputStream in = new BufferedInputStream(Files.newInputStream(file1.toPath()));
            //字节流转换为压缩文件输入流，通常用来读取压缩文件
            ZipInputStream zp = new ZipInputStream(in);
            //定义文件条目
            ZipEntry ze;
            Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
          // 循环压缩包中解压内容==>TODO 增加文件大小的校验
            while (zipEnum.hasMoreElements()) {
                // 获取下一个元素
                ze = zipEnum.nextElement();
                if (!ze.isDirectory()) {
                    long size = ze.getSize();
                    //大小计算
      		        double fileSizeInMB = (double) size / (1024 * 1024);
                    if (fileSizeInMB >  CommonConstant.UPLOAD_FILE_LIMIT) {
                    	throw new Exception(MessageSource.M("FILE_DOWNLOAD_ERROR"));
                    }
                }
                zp.closeEntry();
            }
            // 循环压缩包中解压内容
            while (zipEnum.hasMoreElements()) {
                // 获取下一个元素
                ze = zipEnum.nextElement();
                if (!ze.isDirectory()) {
                    long size = ze.getSize();
                    if (size > 0) {
                        InputStream bf = zipFile.getInputStream(ze);
                        InputStream newBf = zipFile.getInputStream(ze);
                        parseJson(bf, newBf, slideResList);
                        bf.close();
                    }
                }
                zp.closeEntry();
            }
//        } catch (Exception e) {
//            throw new Exception("json文件解析失败");
//        }
//        return true;
    }


    /**
     * 解析json文件流
     *
     * @param fileUrl      文件流
     * @param slideResList 切片集合
     * @throws Exception
     */
    public void parseJson(InputStream fileUrl, InputStream newBf, List<SlideRes> slideResList) throws Exception {
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createParser(fileUrl);
        JsonToken current;
        current = jp.nextToken();
        if (current != JsonToken.START_OBJECT) {
            throw new RemoteException("json type error！");
        }
        String imageName = null;

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            jp.nextToken();
            // move from field name to field value
            if ("image".equals(fieldName)) {
                JsonNode treeNode = jp.readValueAsTree();
                imageName = treeNode.get("image_name").asText();
            } else {
                jp.skipChildren();
            }
        }
        // 校验切片名称
        fileNameContrast(imageName, slideResList, newBf);

    }


    public void fileNameContrast(String imageName, List<SlideRes> slideResList, InputStream newBf) throws Exception {
        List<cn.staitech.anno.project.domain.Marking> markingList = new ArrayList<>();
        if (imageName != null) {
            for (SlideRes slide : slideResList) {
                // 判断名称切片名称是否相同

                if (Objects.equals(slide.getImageName(), imageName)) {
                    // 删除当前切片中所有标注
                    QueryWrapper<Marking> markingQueryWrapperBy = new QueryWrapper<>();
                    markingQueryWrapperBy.eq("slide_id", slide.getSlideId());
                    markingMapperV1.delete(markingQueryWrapperBy);
                    // 查询切片详情
                    Slide slideBy = slideMapperV1.selectById(slide.getSlideId());
                    // 查询图片详情
                    Image image = imageMapper.selectById(slideBy.getImageId());
                    // 定义病理指标标签
                    Map<String, Long> categoryMap = new HashMap<>();
                    // 定义用户列表
                    List<Long> userByList = new ArrayList<>();
                    Map<String, Object> objMap = null;
                    // 循环列表，对数据进行处理
                    JsonFactory f = new MappingJsonFactory();
                    JsonParser jp = f.createParser(newBf);
                    JsonToken current;
                    current = jp.nextToken();
                    while (jp.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = jp.getCurrentName();
                        // move from field name to field value
                        current = jp.nextToken();
                        if ("features".equals(fieldName)) {
                            if (current == JsonToken.START_ARRAY) {
                                while (jp.nextToken() != JsonToken.END_ARRAY) {
                                    String node = jp.readValueAsTree().toString();
                                    JSONObject featureObject = JSONObject.parseObject(node);
                                    objMap = writeMarking(slide.getSlideId(), featureObject, slideBy, image, categoryMap);
                                    cn.staitech.anno.project.domain.Marking marking = (cn.staitech.anno.project.domain.Marking) objMap.get("marking");
                                    // 添加至列表中
                                    markingList.add(marking);
                                    // 获取用户列表
                                    if (!userByList.contains(marking.getCreateBy())) {
                                        userByList.add(marking.getCreateBy());
                                    }
                                    // 将标签map进行赋值
                                    Object categoryNewMap = objMap.get("category");
                                    if (categoryNewMap != null) {
                                        categoryMap = (Map<String, Long>) categoryNewMap;
                                    }
                                    // 添加数据入库
                                    if (markingList.size() >= BATCH_SIZE) {
                                        markingServiceV1.saveBatch(markingList);
                                        markingList = new ArrayList<>();
                                    }
                                }
                            }
                        } else {
                            jp.skipChildren();
                        }
                    }
                    // 将剩余数据进行添加
                    if (markingList.size() > 0) {
                        markingServiceV1.saveBatch(markingList);
                    }
                    // 获取标签列表
                    List<Long> categoryList = new ArrayList<>();
                    if (categoryMap.size() > 0) {
                        categoryList.addAll(categoryMap.values());
                    }
                    // 更新切片表中状态
                    if (Objects.equals(slideBy.getStatus(), "1")) {
                        // 更新切片表中状态至切片中
                        slideBy.setStatus("2");
                        slideMapperV1.updateById(slideBy);
                    }
                    // 添加结束之后，更新标签信息
                    slideAttrService.saveAnnoUsers(slide.getSlideId(), userByList);
                    slideAttrService.saveAnnoCategory(slide.getSlideId(), categoryList);
                }
            }
        }
    }


    public Map<String, Object> writeMarking(Long slideId, JSONObject featureObject, Slide slideBy, Image image, Map<String, Long> categoryMap) throws Exception {

        Map<String, Object> map = new HashMap<>();

        // 获取annotationId
        String annotationId = featureObject.getString("id");
        // 获取geometry数据
        JSONObject geometry = featureObject.getJSONObject("geometry");
        // 获取属性和自定义字段
        JSONObject properties = featureObject.getJSONObject("properties");
        cn.staitech.anno.vo.geojson.Properties properties1 = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(properties)), Properties.class);
        cn.staitech.anno.project.domain.Marking marking = new cn.staitech.anno.project.domain.Marking();
        // 查询标签信息
        if (!Objects.equals(properties1.getLabel_code(), "") && properties1.getLabel_code() != null) {
            Long categoryId = categoryMap.get(properties1.getLabel_code());
            if (categoryId == null) {
                PathologicalIndicatorCategory pathologicalIndicatorCategory = pathologicalIndicatorCategoryMapper.selectProjectAndNumber(Long.valueOf(slideBy.getProjectId()), properties1.getLabel_code());
                if (pathologicalIndicatorCategory != null) {
                    marking.setCategoryId(pathologicalIndicatorCategory.getCategoryId());
                    categoryMap.put(properties1.getLabel_code(), pathologicalIndicatorCategory.getCategoryId());
                    map.put("category", categoryMap);
                }
            } else {
                marking.setCategoryId(categoryId);
            }
        }
        // 根据用户id查询用户详情信息
        SysUser user = userMapper.selectUserById(Long.valueOf(properties1.getAnnotation_owner()));
        if (user != null) {
            marking.setAnnotationOwner(user.getUserName());
        }
        // 写入实体类
        marking.setAnnotationId(annotationId);
        marking.setArea(properties1.getArea());
        marking.setPerimeter(properties1.getPerimeter());
        marking.setNumber(properties1.getNumber());
        marking.setMeasureType(properties1.getMeasure_type());
        marking.setMeasureRelation(properties1.getMeasure_relation());
        marking.setMeasureName(properties1.getMeasure_name());
        marking.setMeasureNumber(properties1.getMeasure_number());
        marking.setRadius(properties1.getRadius());
        marking.setMeanDistance(properties1.getMean_distance());
        marking.setMaxDistance(properties1.getMax_distance());
        marking.setMinDistance(properties1.getMin_distance());
        marking.setInnerAngle(properties1.getInner_angle());
        marking.setExteriorAngle(properties1.getExterior_angle());
        marking.setAnnotationType(properties1.getAnnotation_type());
        marking.setCenterPoint(properties1.getCenter_point());
        marking.setProjectId(Long.valueOf(slideBy.getProjectId()));
        marking.setImageId(Long.valueOf(slideBy.getImageId()));
        marking.setImageUrl(image.getImageUrl());
        marking.setCreateBy(Long.valueOf(properties1.getAnnotation_owner()));
        marking.setGeometry(GeometryUtil.updateYAxle(geometry));
        marking.setSlideId(slideId);
        marking.setCreateTime(new Date());
        map.put("marking", marking);
        return map;
    }
















}