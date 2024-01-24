package cn.staitech.anno.config;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.MarkingMapperV1;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.mapper.SysUserMapperV1;
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.project.service.SlideAttrService;
import cn.staitech.anno.utils.GeometryUtil;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.slide.SlideRes;
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

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
    private static final ExecutorService EXECUTOR = ExecutorBuilder.create().setCorePoolSize(
                    Runtime.getRuntime()
                            .availableProcessors())
            .setMaxPoolSize(Runtime.getRuntime()
                    .availableProcessors() * 2)
            .setKeepAliveTime(0)
            .build();

    /**
     * 如果需要调整并发数目，修改下面方法的第二个参数即可
     */
    static {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "4");
    }

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
    private MarkingMapperV1 markingMapperV1;
    @Resource
    private SysUserMapperV1 userMapperV1;

    /**
     * 插入方法
     *
     * @param list     插入数据集合
     * @param consumer 消费型方法，直接使用 mapper::method 方法引用的方式
     * @param <T>      插入的数据类型
     */
    public static <T> void insertData(List<T> list, Consumer<List<T>> consumer) {
        if (list == null || list.size() < 1) {
            return;
        }

        List<List<T>> streamList = new ArrayList<>();

        for (int i = 0; i < list.size(); i += BATCH_SIZE) {
            int j = Math.min((i + BATCH_SIZE), list.size());
            List<T> subList = list.subList(i, j);
            streamList.add(subList);
        }
        // 并行流使用的并发数是 CPU 核心数，不能局部更改。全局更改影响较大，斟酌
        streamList.parallelStream().forEach(consumer);
    }

    /**
     * TODO:解析json过程中无法标注
     *
     * @param zipUrl
     * @param projectId
     * @param organizationId
     * @param userId
     * @return
     * @throws Exception
     */
    @SneakyThrows
    @Async("getAsyncExecutor")
    public Runnable zipExport(String zipUrl, Long projectId, Long organizationId, Long userId) {
        File file1 = new File(zipUrl);
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

        // 循环压缩包中解压内容
        while (zipEnum.hasMoreElements()) {
            // 获取下一个元素
            ze = zipEnum.nextElement();
            if (!ze.isDirectory()) {
                long size = ze.getSize();
                if (size > 0) {
                    InputStream bf = zipFile.getInputStream(ze);
                    InputStream newBf = zipFile.getInputStream(ze);
                    parseJson(bf, newBf, slideResList, organizationId, userId);
                    bf.close();
                }
            }
        }
        zp.closeEntry();
        return null;
    }

    public void parseJson(InputStream fileUrl, InputStream newBf, List<SlideRes> slideResList, Long organizationId, Long userId) throws Exception {
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createParser(fileUrl);
        JsonToken current;
        current = jp.nextToken();
        if (current != JsonToken.START_OBJECT) {
            throw new RemoteException("json type error！");
        }
        String imageName = null;
        List<Long> userIdList = new ArrayList<>();
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            current = jp.nextToken();
            // move from field name to field value
            if ("image".equals(fieldName)) {
                JsonNode treeNode = jp.readValueAsTree();
                imageName = treeNode.get("image_name").asText();
            } else if ("features".equals(fieldName)) {
                if (current == JsonToken.START_ARRAY) {
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        String node = jp.readValueAsTree().toString();
                        JSONObject featureObject = JSONObject.parseObject(node);
                        // 获取属性和自定义字段
                        JSONObject properties = featureObject.getJSONObject("properties");
                        cn.staitech.anno.vo.geojson.Properties properties1 = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(properties)), Properties.class);
                        if (!userIdList.contains(Long.valueOf(properties1.getAnnotation_owner()))) {
                            userIdList.add(Long.valueOf(properties1.getAnnotation_owner()));
                        }
                    }
                }
            } else {
                jp.skipChildren();
            }
        }
        // 校验切片名称
        fileNameContrast(imageName, slideResList, newBf, userIdList, organizationId, userId);

    }

    void fileNameContrast(String imageName, List<SlideRes> slideResList, InputStream newBf, List<Long> userIdList, Long organizationId, Long userId) throws Exception {
        if (imageName != null) {
            for (SlideRes slide : slideResList) {
                // 判断名称切片名称是否相同
                if (Objects.equals(slide.getImageName(), imageName)) {
                    List<cn.staitech.anno.project.domain.Marking> markingList = new ArrayList<>();
                    QueryWrapper<cn.staitech.anno.project.domain.SysUser> userQueryWrapper = new QueryWrapper<>();
                    userQueryWrapper.in("user_id", userIdList);
                    List<cn.staitech.anno.project.domain.SysUser> userList = userMapperV1.selectList(userQueryWrapper);

                    QueryWrapper<Marking> markingQueryWrapperBy = new QueryWrapper<>();
                    markingQueryWrapperBy.in("create_by", userIdList);
                    markingQueryWrapperBy.eq("slide_id", slide.getSlideId());
                    // 删除json文件中所有用户在切片中的轮廓数据
                    // 查询当前切片下的用户数量
                    int markingCount = markingMapperV1.selectCount(markingQueryWrapperBy);
                    double result = (double) markingCount / 2000;
                    int ceilNum = (int) Math.ceil(result);
                    for (int i = 0; i < ceilNum; i++) {
                        synchronized (this) {
                            markingQueryWrapperBy.last("limit 2000").orderByDesc("create_time");
                            markingMapperV1.delete(markingQueryWrapperBy);
                        }
                    }
                    // 将数据转化为map
                    Map<Long, String> userMap = userList.stream().collect(Collectors.toMap(cn.staitech.anno.project.domain.SysUser::getUserId, cn.staitech.anno.project.domain.SysUser::getUserName));
                    // 查询切片详情
                    Slide slideBy = slideMapperV1.selectById(slide.getSlideId());
                    // 查询图片详情
                    Image image = imageMapper.selectById(slideBy.getImageId());
                    // 定义病理指标标签
                    Map<String, Long> categoryMap = new HashMap<>(16);
                    // 定义标签map
                    Map<String, Object> objMap = null;
                    // 循环列表，对数据进行处理
                    JsonFactory f = new MappingJsonFactory();
                    JsonParser jp = f.createParser(newBf);
                    JsonToken current;
                    Long aTime = 0L;
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
                                    objMap = writeMarking(slide.getSlideId(), featureObject, slideBy, image, categoryMap, userMap, organizationId);
                                    cn.staitech.anno.project.domain.Marking marking = (cn.staitech.anno.project.domain.Marking) objMap.get("marking");
                                    // 添加至列表中
                                    markingList.add(marking);
                                    // 将标签map进行赋值
                                    Object categoryNewMap = objMap.get("category");
                                    if (categoryNewMap != null) {
                                        categoryMap = (Map<String, Long>) categoryNewMap;
                                    }
                                    // 添加数据入库

                                    if (markingList.size() >= BATCH_SIZE) {
                                        markingServiceV1.saveBatch(markingList);
//                                    insertData(markingList,markingServiceV1::saveBatch);
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
                        markingList = new ArrayList<>();
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
//                    slideMapperV1.updateById(slideBy);
                    }
                    // 添加结束之后，更新标签信息
                    slideAttrService.saveAnnoUsers(slide.getSlideId(), userIdList, userId);
                    slideAttrService.saveAnnoCategory(slide.getSlideId(), categoryList, userId);


//                    EXECUTOR.submit(new TaskThread(userIdList, slide.getSlideId(), newBf, organizationId, userId));
                }
            }
        }
    }

    public Map<String, Object> writeMarking(Long slideId, JSONObject featureObject, Slide slideBy, Image image, Map<String, Long> categoryMap, Map<Long, String> userMap, Long organizationId) throws Exception {

        Map<String, Object> map = new HashMap<>(16);

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
                PathologicalIndicatorCategory pathologicalIndicatorCategory = pathologicalIndicatorCategoryMapper.selectProjectAndNumber(Long.valueOf(slideBy.getProjectId()), properties1.getLabel_code(), organizationId);
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
        if (userMap.get(Long.valueOf(properties1.getAnnotation_owner())) != null) {
            marking.setAnnotationOwner(userMap.get(Long.valueOf(properties1.getAnnotation_owner())));
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

    class TaskThread implements Runnable {

        private final List<Long> userIdList;
        private final Long slideId;
        private final InputStream newBf;
        private final Long organizationId;
        private final Long userId;

        public TaskThread(List<Long> userIdList, Long slideId, InputStream newBf, Long organizationId, Long userId) {
            this.userIdList = userIdList;
            this.slideId = slideId;
            this.newBf = newBf;
            this.organizationId = organizationId;
            this.userId = userId;
        }

        @Override
        public void run() {
            try {
                // 使用多线程来解析json文件
                // 查询json文件中所有标注人员
                List<cn.staitech.anno.project.domain.Marking> markingList = new ArrayList<>();
                QueryWrapper<cn.staitech.anno.project.domain.SysUser> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.in("user_id", userIdList);
                List<cn.staitech.anno.project.domain.SysUser> userList = userMapperV1.selectList(userQueryWrapper);

                QueryWrapper<Marking> markingQueryWrapperBy = new QueryWrapper<>();
//                for (Long createBy : userIdList) {
                markingQueryWrapperBy.in("create_by", userIdList);
                markingQueryWrapperBy.eq("slide_id", slideId);
                // 删除json文件中所有用户在切片中的轮廓数据
                // 查询当前切片下的用户数量
                int markingCount = markingMapperV1.selectCount(markingQueryWrapperBy);
                double result = (double) markingCount / 2000;
                int ceilNum = (int) Math.ceil(result);
                for (int i = 0; i < ceilNum; i++) {
                    synchronized (this) {
                        markingQueryWrapperBy.last("limit 2000").orderByDesc("create_time");
                        markingMapperV1.delete(markingQueryWrapperBy);
                    }
                }
//                }
                // 将数据转化为map
                Map<Long, String> userMap = userList.stream().collect(Collectors.toMap(cn.staitech.anno.project.domain.SysUser::getUserId, cn.staitech.anno.project.domain.SysUser::getUserName));
                // 查询切片详情
                Slide slideBy = slideMapperV1.selectById(slideId);
                // 查询图片详情
                Image image = imageMapper.selectById(slideBy.getImageId());
                // 定义病理指标标签
                Map<String, Long> categoryMap = new HashMap<>(16);
                // 定义标签map
                Map<String, Object> objMap = null;
                // 循环列表，对数据进行处理
                JsonFactory f = new MappingJsonFactory();
                JsonParser jp = f.createParser(newBf);
                JsonToken current;
                Long aTime = 0L;
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
                                objMap = writeMarking(slideId, featureObject, slideBy, image, categoryMap, userMap, organizationId);
                                cn.staitech.anno.project.domain.Marking marking = (cn.staitech.anno.project.domain.Marking) objMap.get("marking");
                                // 添加至列表中
                                markingList.add(marking);
                                // 将标签map进行赋值
                                Object categoryNewMap = objMap.get("category");
                                if (categoryNewMap != null) {
                                    categoryMap = (Map<String, Long>) categoryNewMap;
                                }
                                // 添加数据入库

                                if (markingList.size() >= BATCH_SIZE) {
                                    markingServiceV1.saveBatch(markingList);
//                                    insertData(markingList,markingServiceV1::saveBatch);
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
                    markingList = new ArrayList<>();
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
//                    slideMapperV1.updateById(slideBy);
                }
                // 添加结束之后，更新标签信息
                slideAttrService.saveAnnoUsers(slideId, userIdList, userId);
                slideAttrService.saveAnnoCategory(slideId, categoryList, userId);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}