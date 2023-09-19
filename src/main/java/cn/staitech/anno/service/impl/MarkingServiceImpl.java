package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.R.MeasureResponseConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.geojson.*;
import cn.staitech.anno.domain.geojson.Properties;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.domain.vo.BroadcastVO;
import cn.staitech.anno.domain.vo.file.SlideFileName;
import cn.staitech.anno.domain.vo.marking.out.MarkingSelectListVo;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.project.service.SlideAttrService;
import cn.staitech.anno.service.FileService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.utils.*;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.springframework.transaction.annotation.Transactional;


import static cn.staitech.anno.constant.AnnotationConstant.*;
import static cn.staitech.anno.constant.ViewerConstant.MICRON;
import static cn.staitech.anno.aspect.LogFileAspect.response;

@Service
public class MarkingServiceImpl implements MarkingService {

    @Resource
    private SlideMapper slideMapper;
    @Resource
    private SlideAttrService slideAttrService;

    @Resource
    private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;

    @Resource
    private MarkingMapper markingMapper;

    @Resource
    private FileService fileService;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private SysUserMapper userMapper;


    @Override
    public List<MarkingSelectListVo> selectList(Long slideId) throws Exception {
        Slide slideBy = slideMapper.selectById(slideId);
        if (!Optional.ofNullable(slideBy).isPresent()) {
            throw new Exception("切片信息异常,未查询到切片信息");
        }
        List<MarkingSelectListVo> markingSelectListVoList = markingMapper.selectList(slideId);
        List<MarkingSelectListVo> pointCountList = markingMapper.selectPointCountList(slideId);
        markingSelectListVoList = Stream.of(markingSelectListVoList, pointCountList).flatMap(list -> list.stream().map(x -> (MarkingSelectListVo) x)).collect(Collectors.toList());
        return markingSelectListVoList;
    }

    @Override
    public List<Features> selectLists(Long slideId) throws Exception {
        Slide slideBy = slideMapper.selectById(slideId);
        if (!Optional.ofNullable(slideBy).isPresent()) {
            throw new Exception("切片信息异常,未查询到切片信息");
        }
        return markingMapper.selectLists(slideId);
    }


    @Override
    public List<SlideRes> selectSlideList(Long specialId) {
        return markingMapper.selectSlideList(specialId);
    }

    @Override
    public PointCount selectCategoryCount(Marking marking) {
        return markingMapper.selectCategoryCount(marking);
    }

    @Override
    public List<PointCount> selectCategoryCountList(Long slideId) {
        return markingMapper.selectCategoryCountList(slideId);
    }


    @Override
    public Marking selectById(Long markingId) {
        return markingMapper.selectById(markingId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(viewAddIn req) throws Exception {
        Slide slideBy = slideMapper.selectById(req.getSlide_id());
        if (slideBy == null) {
            throw new Exception("未查询到切片信息");
        }
        Marking marking = new Marking();
        BeanUtils.copyProperties(req, marking);
        // 拼接标注名称
        String annotationId = CustomizationIdUtils.getSdId();
        marking.setAnnotation_id(annotationId);
        if (req.getArea() != null) {
            Double area = new Double(req.getArea()) * MICRON;
            marking.setArea(String.valueOf(area));
        }
        if (req.getPerimeter() != null) {
            Double perimeter = new Double(req.getPerimeter()) * MICRON;
            marking.setPerimeter(String.valueOf(perimeter));
        }
        marking.setCreate_by(req.getCreate_by());
        marking.setAnnotation_type("Draw");
        marking.setOrganization_id(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        marking.setCreate_time(new Date());
        SysUser user = userMapper.selectUserById(req.getCreate_by());
        if(user != null){
            marking.setAnnotation_owner(user.getUserName());
        }
        // 查询
        QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
        // 根据切片和测量轮廓名称查询最大值
        markingQueryWrapper.eq("slide_id", req.getSlide_id()).eq("measure_name", req.getMeasure_name()).orderByDesc("create_time").last("limit 1");
        Marking markingBy = markingMapper.selectOne(markingQueryWrapper);
        int number = 1;
        if (markingBy != null) {
            if (markingBy.getNumber() != null) {
                number += markingBy.getNumber();
            }
        }
        marking.setProject_id(slideBy.getProjectId());
        Image image = imageMapper.selectById(slideBy.getImageId());
        if(image != null){
            marking.setImage_id(image.getImageId());
            marking.setImage_url(image.getImageUrl());
        }
        marking.setNumber(number);
        // 添加数据库，添加后返回自增id
        markingMapper.insert(marking);
        Properties properties = markingMapper.selectBy(marking.getMarking_id());
        Features features = socketData(annotationId, req.getGeometry(), properties);
        // 如果是点类型，返回点的总数并返回
        List<PointCount> pointCountList = updatePoint(req.getLocation_type(), marking);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features, pointCountList);
        NioWebSocketHandler.sendAll(req.getSlide_id(), broadcastVO);
        // 更新切片表中最新状态
        updateSLide(marking.getSlide_id());
        slideAttrService.saveAnnoUsers(req.getSlide_id(), Collections.singletonList(marking.getCreate_by()));
        slideAttrService.saveAnnoCategory(req.getSlide_id(), Collections.singletonList(marking.getCategory_id()));
        return marking.getMarking_id();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(MarkingUpdateIn req) throws Exception {
        // 查询标注表中信息
        Marking markingBy = markingMapper.selectById(req.getMarking_id());
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception("未查询到标注信息");
        }
        // 查询标注表中信息
        Slide slide = slideMapper.selectById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception("未查询到切片信息");
        }
        // 更新前数据
        // 更新文件中的内容
        Marking marking = new Marking();
        BeanUtils.copyProperties(req, marking);
        if(req.getCreate_by() != null){
            marking.setUpdate_by(req.getCreate_by());
            SysUser user = userMapper.selectUserById(req.getCreate_by());
            if(user != null){
                marking.setAnnotation_owner(user.getUserName());
            }
        }else{
            marking.setUpdate_by(SecurityUtils.getUserId());
            marking.setAnnotation_update_owner(SecurityUtils.getUsername());
        }
        marking.setUpdate_time(new Date());
        if (req.getArea() != null) {
            Double area = new Double(req.getArea()) * MICRON;
            marking.setArea(String.valueOf(area));
        }
        if (req.getPerimeter() != null) {
            Double perimeter = new Double(req.getPerimeter()) * MICRON;
            marking.setPerimeter(String.valueOf(perimeter));
        }
        List<PointCount> pointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
        markingMapper.updateById(marking);
        // 判断标签
        if (req.getCategory_id() != null) {
            if (req.getCategory_id() != 0 && !req.getCategory_id().equals(markingBy.getCategory_id())) {
                markingBy.setCategory_id(req.getCategory_id());
                List<PointCount> newPointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
                pointCountList = Stream.of(pointCountList, newPointCountList).flatMap(list -> list.stream().map(x -> (PointCount) x)).collect(Collectors.toList());
            }
        }
        Properties properties = markingMapper.selectBy(marking.getMarking_id());
        Features features = socketData(markingBy.getAnnotation_id(), req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features, pointCountList);
        // 使用websocket发送数据
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        // 更新切片表中数据
        updateSLide(slide.getSlideId());
        // 更新前数据
        slideAttrService.removeAnnoUsers(slide.getSlideId(), Collections.singletonList(markingBy.getCreate_by()));
        slideAttrService.removeAnnoCategory(slide.getSlideId(), Collections.singletonList(markingBy.getCategory_id()));
        // 更新后数据
        slideAttrService.saveAnnoUsers(slide.getSlideId(), Collections.singletonList(SecurityUtils.getUserId()));
        if(req.getCategory_id() != null){
            slideAttrService.saveAnnoCategory(slide.getSlideId(), Collections.singletonList(req.getCategory_id()));
        }else{
            slideAttrService.saveAnnoCategory(slide.getSlideId(), new ArrayList<>());
        }

        return markingBy.getMarking_id();
    }

    @Override
    public int updatePointCount(Marking marking) {
        return markingMapper.updatePointCount(marking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long markingId) throws Exception {
        if (!Optional.ofNullable(markingId).isPresent()) {
            throw new Exception("参数异常");
        }
        Marking markingBy = markingMapper.selectById(markingId);
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception("未查询到标注信息");
        }
        Slide slide = slideMapper.selectById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception("未查询到切片信息");
        }
        Properties properties = markingMapper.selectBy(markingId);
        Features features = socketData(markingBy.getAnnotation_id(), markingBy.getGeometry(), properties);
        List<PointCount> pointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(DELETE_STATUS, features, pointCountList);
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        int res = markingMapper.delete(markingId);
        updateSLide(slide.getSlideId());
        // 更新前数据
        slideAttrService.removeAnnoUsers(slide.getSlideId(), Collections.singletonList(markingBy.getCreate_by()));
        slideAttrService.removeAnnoCategory(slide.getSlideId(), Collections.singletonList(markingBy.getCategory_id()));
        return res;
    }

    @Override
    public String jsonExport(Long slideId) throws Exception {
//        CompletableFuture<R<String>> cf2 = CompletableFuture.supplyAsync(() -> {
        if (!Optional.ofNullable(slideId).isPresent()) {
            try {
                throw new Exception("参数异常");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Slide slideBy = slideMapper.selectById(slideId);
        if (!Optional.ofNullable(slideBy).isPresent()) {
            try {
                throw new Exception("未查询到切片信息");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        String fileUrl = null;
        try {
            fileUrl = fileService.createFiles(slideId,".json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 标注数据
        List<Features> features = markingMapper.selectLists(slideId);
        // 查询项目详情
        JsonExport jsonExport = markingMapper.jsonExportSelect(slideId);
        // 项目信息
        GeoProject project = new GeoProject();
        project.setProject_id(jsonExport.getTopicName());
        project.setProject_name(jsonExport.getProjectName());
        // 图像信息
        GeoImage image = new GeoImage();
        image.setImage_shape(jsonExport.getImageShape());
        image.setImage_type(jsonExport.getFormat());
        image.setImage_name(jsonExport.getImageName());
        image.setCreate_time(jsonExport.getCreateTime());
        String imageId = jsonExport.getImageName() + System.currentTimeMillis() + RandomUtils.RandomNumbers();
        image.setImage_id(imageId);
        // 作者信息
        GeoAttribute attribute = new GeoAttribute();
        attribute.setAuthor(SecurityUtils.getUsername());
//            attribute.setDepartment(SecurityUtils.getLoginUser().getSysUser().getDept());
        attribute.setDepartment("标注组");
        List<GeoLabel> categoryList = pathologicalIndicatorCategoryMapper.selectIndicatorIdList(jsonExport.getIndicatorId());
        // 构建geoJson数据
        GeoJson geoJson = new GeoJson();
        geoJson.setFeatures(features);
        geoJson.setImage(image);
        geoJson.setProject(project);
        geoJson.setAttribute(attribute);
        geoJson.setLabel_info(categoryList);
        String jsonString = JSON.toJSONString(geoJson, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        // 写入文件
        exportJson(fileUrl,jsonString);
//            return R.ok(fileUrl);
//        });
        return fileUrl;
    }



    @Override
    public boolean zipExport(String zipUrl, Long projectId) throws Exception {
        StringBuilder sb;
        File file1 = new File(zipUrl);
        Map<String, String> ddlList = new HashMap<>();
        try {
            // 查询切片列表
            List<SlideRes> slideResList = slideMapper.selectImageList(projectId);
            //zip可以包含对个文件，如果只有一个文件，则只解析一个文件的，包含多个文件则分别解析
            ZipFile zipFile = new ZipFile(file1, Charset.forName("gbk"));//必须指明读取的各式，不是会存在问题***
            InputStream in = new BufferedInputStream(Files.newInputStream(file1.toPath()));//按流的方式读取文件，输入到管道中
            ZipInputStream zp = new ZipInputStream(in);//字节流转换为压缩文件输入流，通常用来读取压缩文件
            ZipEntry ze;//定义文件条目
            Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
            while (zipEnum.hasMoreElements()) {//判断是否还有元素
                ze =  zipEnum.nextElement();//返回下一对象
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
                            if (imageName != null) {
                                for (SlideRes slideRes : slideResList) {
                                    // 获取数据库文件名称
                                    String slideImageName = slideRes.getImageName();
                                    // 判断名称相等，获取切片id
                                    if (Objects.equals(imageName, slideImageName)) {
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

                                            Properties properties1 = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(properties)), Properties.class);
                                            // 写入数据库
                                            Marking marking = new Marking();
                                            marking.setAnnotation_id(annotationId);
                                            marking.setCreate_by(SecurityUtils.getUserId());
                                            marking.setGeometry(geometry);
                                            marking.setSlide_id(slideRes.getSlideId());
                                            cn.staitech.common.core.utils.bean.BeanUtils.copyProperties(properties1, marking);
                                            markingMapper.insert(marking);
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
//            if (!file1.delete()) {
//                log.error("删除文件" + file1 + "失败！");
//            }
        } catch (Exception e) {
            throw new Exception("json文件解析失败");
        }
        return true;
    }

    @Override
    public void execlExport(Long slideId) throws Exception {
        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(MeasureResponseConstant.COLHEAD_KEY,
                MeasureResponseConstant.COLHEAD_VALUE);
        // 查询当前切片不为点类型的标注数据
        List<Properties> propertiesList = markingMapper.selectMeasureList(slideId);

        System.out.println(propertiesList);

        System.out.println();
        // 加点的记录
        QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper.eq("slide_id", slideId).eq("location_type", "Point");
        int marking = markingMapper.selectCount(markingQueryWrapper);
        Properties properties = new Properties();
        properties.setPoint_count((long) marking);
        properties.setMeasure_name("P");
        propertiesList.add(properties);
        // 生成excel文件
        ExcelTool excelTool = new ExcelTool(MeasureResponseConstant.EXCEL_TITLE, 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        excelTool.exportExcel(titleData, propertiesList, response.getOutputStream(), true, false);
    }

    /**
     * 封装socket发送数据
     *
     * @param annotationId
     * @param geometry
     * @param properties
     * @return
     */
    public Features socketData(String annotationId, JSONObject geometry, Properties properties) {
        Features features = new Features();
        features.setGeometry(geometry);
        features.setId(annotationId);
        features.setType("Feature");
        JSONObject jsonObject = (JSONObject) JSON.toJSON(properties);
        features.setProperties(jsonObject);
        return features;
    }


    /**
     * 统计不同类型点的数量
     *
     * @param locationType
     * @param marking
     * @return
     */
    public List<PointCount> updatePoint(String locationType, Marking marking) {
        List<PointCount> pointCountList = new ArrayList<>();
        if (Objects.equals(locationType, "Point")) {
            PointCount pointCounts = markingMapper.selectCategoryCount(marking);
            marking.setPoint_count(pointCounts.getPoint_count());
            markingMapper.updatePointCount(marking);
            pointCountList.add(pointCounts);
        }
        return pointCountList;
    }

    @SneakyThrows
    @Async
    public void exportJson(String fileUrl,String jsonString) {
        try {
            OutputStream outputStream = Files.newOutputStream(Paths.get(fileUrl));
            outputStream.write(jsonString.getBytes());
            // 关闭流
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新切片表中数据
     *
     * @param slideId 切片id
     * @return
     */
    public void updateSLide(Long slideId) {
        Slide slide = new Slide();
        slide.setSlideId(slideId);
        slide.setUpdateTime(new Date());
        slideMapper.updateById(slide);
    }

    public List<Map<String, String>> getTitleList(String[] colHeadKey, String[] colHeadValue) {
        // 定义表头
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < colHeadKey.length; i++) {
            Map<String, String> map = new HashMap<String, String>(1);
            map.put(colHeadKey[i], colHeadValue[i]);
            list.add(map);
        }
        return list;
    }


}
