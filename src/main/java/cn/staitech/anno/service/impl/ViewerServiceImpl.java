package cn.staitech.anno.service.impl;

import cn.staitech.anno.config.RedisClientUtil;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.document.GeometryDoc;
import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.Properties;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.elasticsearchRepositories.GeometryDocMapper;
import cn.staitech.anno.mapper.MarkingMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.service.ViewerService;
import cn.staitech.anno.utils.CustomizationIdUtils;
import cn.staitech.anno.utils.FileUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static cn.staitech.anno.utils.FileUtils.getFileNameNoEx;
import static cn.staitech.anno.utils.TimeUtils.CurrentTime;
import static org.reflections.Reflections.log;

@Service
public class ViewerServiceImpl implements ViewerService {

    @Resource
    private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;

    @Resource
    private RedisClientUtil redisClientUtil;

    @Resource
    private MarkingMapper markingMapper;

    @Resource
    private GeometryDocMapper geometryDocMapper;

    @Resource
    private SlideService slideService;

    /**
     * 获取geojsonUrl
     *
     * @param geojsonUrls 文件地址,slideId 切片id
     * @return geojsonUrl
     */
    @Override
    public String getGeojsonUrls(String geojsonUrls, Long slideId) {
        String geojsonUrl;
        // 获取文件地址是否存在
        if (geojsonUrls == null || geojsonUrls.isEmpty()) {

            geojsonUrl = FileUtils.createFile(slideId);
            return geojsonUrl;
        }
        geojsonUrl = geojsonUrls;
        return geojsonUrl;
    }

    /**
     * 构建添加标注数据
     *
     * @param req 入参数据
     * @return geojsonUrl
     */
    @Override
    public Features constructAddMarking(viewAddIn req) {
        Features marking = new Features();
        Properties properties = new Properties();
        String id = CustomizationIdUtils.getSdId();
        marking.setId(id);
        marking.setType("Feature");
        marking.setGeometry(req.getGeometry());

        String numKey = req.getSlide_id() + "_" + req.getMeasure_name();
        Long numId = redisClientUtil.getAndAddLong("labelNameNum:" + numKey, 1L);
        // 获取标注名称
        String measure_full_name = req.getMeasure_name() + numId + "_";
        if (req.getCategory_id() != null) {
            //根据标注id获取标注类别详情
            PathologicalIndicatorCategory categoryBy = pathologicalIndicatorCategoryMapper.selectByPrimaryKey(req.getCategory_id());
            if (categoryBy != null) {
                properties.setLabel_color(categoryBy.getHex());
                properties.setLabel_name(categoryBy.getCategoryName());
                measure_full_name += categoryBy.getCategoryName();
            }
        }
        String date = CurrentTime();
//        properties.setAnnotation_owner(SecurityUtils.getUserId());
        properties.setAnnotation_type("Draw");
        properties.setCreate_time(date);
        properties.setCategory_id(req.getCategory_id());
        properties.setMeasure_type(req.getMeasure_type());
        properties.setMeasure_relation(req.getMeasure_relation());
        properties.setMeasure_name(req.getMeasure_name());
        properties.setMeasure_full_name(measure_full_name);
        properties.setDescription(req.getDescription());
        properties.setMeasure_number(req.getMeasure_number());
        properties.setPerimeter(req.getPerimeter());
        properties.setLocation_type(req.getLocation_type());
        properties.setArea(req.getArea());
        return marking;
    }


    @Override
    public Features constructUpdMarking(MarkingUpdateIn req, String measureFullName) {
        // 更新标注信息
        Features marking = new Features();
        Properties properties = new Properties();
//        marking.setId(req.getAnnotation_id());
        marking.setGeometry(req.getGeometry());
        properties.setArea(req.getArea());
        properties.setPerimeter(req.getPerimeter());
        properties.setCategory_id(req.getCategory_id());
        BeanUtils.copyProperties(req, properties);
        BeanUtils.copyProperties(req, marking);
        if (req.getCategory_id() != null) {
            //根据标注id获取标注类别详情
            PathologicalIndicatorCategory categoryBy = pathologicalIndicatorCategoryMapper.selectByPrimaryKey(req.getCategory_id());
            if (categoryBy != null) {
                properties.setLabel_color(categoryBy.getHex());
                properties.setLabel_name(categoryBy.getCategoryName());
                String res1 = String.valueOf(measureFullName.charAt(measureFullName.length() - 1));
                if (res1.equals("_")) {
                    measureFullName = measureFullName + categoryBy.getCategoryName();
                } else {
                    measureFullName = measureFullName.replaceAll(measureFullName.split("_")[measureFullName.split("_").length - 1], categoryBy.getCategoryName());
                }
            }
        }
        properties.setMeasure_full_name(measureFullName);
        return marking;
    }


    @Override
    public boolean zipExport(String zipUrl, Long specialId) throws Exception {
        StringBuilder sb;
        File file1 = new File(zipUrl);
        Map<String, String> ddlList = new HashMap<>();
        try {
            List<SlideRes> slideList = markingMapper.selectSlideList(specialId);
            //zip可以包含对个文件，如果只有一个文件，则只解析一个文件的，包含多个文件则分别解析
//            ZipInputStream in = new ZipInputStream(Files.newInputStream(file.toPath()));
            ZipFile zipFile = new ZipFile(file1, Charset.forName("gbk"));//必须指明读取的各式，不是会存在问题***
            InputStream in = new BufferedInputStream(Files.newInputStream(file1.toPath()));//按流的方式读取文件，输入到管道中
            ZipInputStream zp = new ZipInputStream(in);//字节流转换为压缩文件输入流，通常用来读取压缩文件
            ZipEntry ze;//定义文件条目
            Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
            while (zipEnum.hasMoreElements()) {//判断是否还有元素



                ze = (ZipEntry) zipEnum.nextElement();//返回下一对象

                String fileNames = ze.getName();
                if (!fileNames.contains(".")) {
                    throw new Exception("未检测到json文件");
                }
                String suffix = (fileNames.split("\\.")[fileNames.split("\\.").length - 1]);
                if (!Objects.equals(suffix, "json")) {
                    throw new Exception("未检测到json文件");
                }
                sb = new StringBuilder();
                if (ze.isDirectory()) {
                } else {
                    System.out.println("file - " + ze.getName() + " : " + ze.getSize() + " bytes");
                    long size = ze.getSize();
                    if (size > 0) {
                        BufferedReader bf = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze), StandardCharsets.UTF_8));//读取文件内容
                        String line;
                        while ((line = bf.readLine()) != null) {
                            sb.append(line);
                        }
                        org.json.JSONObject jsonObject = new org.json.JSONObject(sb.toString());
                        // 去除后缀获取名称判断是否相等
                        org.json.JSONObject image = jsonObject.getJSONObject("image");
                        // 获取标注名称
                        if (image != null) {
                            String imageName = image.getString("image_name");
                            log.info(imageName + ">>>>>>>>>>>");
                            if (imageName != null) {
                                for (SlideRes slideRes : slideList) {
                                    // 获取数据库文件名称
                                    String slideImageName = getFileNameNoEx(slideRes.getImageName());
                                    log.info(slideImageName + "<<<<<");
                                    // 判断名称相等，获取切片id
                                    if (Objects.equals(imageName, slideImageName)) {
                                        log.info(slideRes + "----------->");
                                        org.json.JSONArray features = jsonObject.getJSONArray("features");

                                        JSONArray jsonArray = JSONArray.parseArray(String.valueOf(features));
                                        // 循环追加写入文件，添加数据库并保存es中
                                        for (Object feature : jsonArray) {
                                            JSONObject featureObject = (JSONObject) feature;
                                            // 获取annotationId
                                            String annotationId = featureObject.getString("id");
                                            // 获取geometry数据
                                            JSONObject geometry = featureObject.getJSONObject("geometry");
                                            // 获取属性和自定义字段
                                            JSONObject properties = featureObject.getJSONObject("properties");

                                            Features features1 = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(featureObject)), Features.class);

                                            Properties properties1 = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(properties)), Properties.class);
                                            // 获取描述
//                                            String description = properties.getString("description");
                                            // 写入数据库
                                            Marking marking = new Marking();
                                            marking.setAnnotation_id(annotationId);
                                            marking.setCreate_by(SecurityUtils.getUserId());
                                            marking.setSlide_id(slideRes.getSlideId());
                                            cn.staitech.common.core.utils.bean.BeanUtils.copyProperties(properties1, marking);
                                            markingMapper.insert(marking);
                                            // 添加到es中
                                            GeometryDoc geometryDoc = new GeometryDoc();
                                            cn.staitech.common.core.utils.bean.BeanUtils.copyProperties(properties1, geometryDoc);
                                            geometryDoc.setId(marking.getMarking_id());
                                            geometryDoc.setMarking_id(marking.getMarking_id());
                                            String jsonStr = geometry.toString();
                                            geometryDoc.setGeometry(jsonStr);
                                            geometryDoc.setSlideId(slideRes.getSlideId());
                                            geometryDocMapper.save(geometryDoc);
                                            // 存入文件中
                                            // 查询切片详情
                                            Slide slide = slideService.getById(slideRes.getSlideId());
                                            if (slide != null) {
                                                // 查询切片表中地址
                                                // 写入文件中
                                                // 获取geojsonUrl地址
                                                String geojsonUrl = getGeojsonUrls(slide.getGeojsonUrl(), slide.getSlideId());
                                                slide.setGeojsonUrl(geojsonUrl);
                                                // 更新slide表中geojsonUrl地址
                                                if (slide.getGeojsonUrl() != null) {
                                                    slideService.updateById(slide);
                                                }
                                                properties1.setMarking_id(marking.getMarking_id());
                                                FileUtils.addGeojson(features1, geojsonUrl, slideRes.getSlideId());
                                            }
                                        }
                                    }
                                }
                            }
                            //这里是对读取的文件内容进行处理
                            ddlList.put(ze.getName(), sb.toString());
                            bf.close();
                        }
                    }
                }
                zp.closeEntry();
            }
            // 删除文件
            if (!file1.delete()) {
                log.error("删除文件" + file1 + "失败！");
            }
//            DeleteFolder(zipUrl);
            // 查询
        } catch (Exception e) {
            throw new Exception("json文件解析失败");
        }
        return true;
    }


}
