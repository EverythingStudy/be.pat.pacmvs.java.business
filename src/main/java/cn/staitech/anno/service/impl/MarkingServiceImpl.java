package cn.staitech.anno.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.geojson.Properties;
import cn.staitech.anno.domain.geojson.*;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.ViewAddIn;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.domain.vo.BroadcastVO;
import cn.staitech.anno.domain.vo.marking.out.MarkingSelectListVo;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.project.constants.Constants;
import cn.staitech.anno.project.domain.DownTask;
import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.DownTaskMapper;
import cn.staitech.anno.project.mapper.MarkingMapperV1;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.service.DownTaskService;
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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static cn.staitech.anno.aspect.LogFileAspect.response;
import static cn.staitech.anno.constant.CommonConstant.*;

@Service
public class MarkingServiceImpl implements MarkingService {

    private static ExecutorService executor = ExecutorBuilder.create()
            .setCorePoolSize(1)
            .setMaxPoolSize(1)
            .setKeepAliveTime(0)
            .build();
    @Resource
    private SlideMapperV1 slideMapperV1;
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
    @Resource
    private ProjectMapperV1 projectMapperV1;
    @Resource
    private DownTaskMapper downTaskMapper;
    @Resource
    private MarkingMapperV1 markingMapperV1;
    @Resource
    private DownTaskService downTaskService;

    @Override
    public List<MarkingSelectListVo> selectList(Long slideId) throws Exception {
        Slide slideBy = slideMapperV1.selectById(slideId);
        if (!Optional.ofNullable(slideBy).isPresent()) {
            throw new Exception(MessageSource.M("SLIDE_ABNORMAL_NO_INFORMATION"));
        }
        List<MarkingSelectListVo> markingSelectListVoList = markingMapper.selectList(slideId);
        List<MarkingSelectListVo> pointCountList = markingMapper.selectPointCountList(slideId);
        markingSelectListVoList = Stream.of(markingSelectListVoList, pointCountList).flatMap(list -> list.stream().map(x -> (MarkingSelectListVo) x)).collect(Collectors.toList());
        return markingSelectListVoList;
    }

    @Override
    public List<Features> selectListBy(Long slideId) throws Exception {
        Slide slideBy = slideMapperV1.selectById(slideId);
        if (!Optional.ofNullable(slideBy).isPresent()) {
            throw new Exception(MessageSource.M("SLIDE_ABNORMAL_NO_INFORMATION"));
        }
        return markingMapper.selectListBy(slideId);
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
    public Long insert(ViewAddIn req) throws Exception {
        cn.staitech.anno.project.domain.Slide slideBy = slideMapperV1.selectById(req.getSlide_id());
        if (slideBy == null) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }
        Marking marking = new Marking();
        BeanUtils.copyProperties(req, marking);
        // 判断切片状态是否是未开始
        if (Objects.equals(slideBy.getStatus(), "1")) {
            // 更新切片表中状态至切片中
            slideBy.setStatus("2");
            slideMapperV1.updateById(slideBy);
        }
//        if (Boolean.FALSE.equals(markIsNotFinish(slideBy.getStatus()))) {
//            throw new Exception(CommonConstant.UPDATE_ANNOTATION_CATEGORY_MESSAGE);
//        }

        // 获取规定的geoJson Id
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
        if (user != null) {
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
        marking.setProject_id(Long.valueOf(slideBy.getProjectId()));
        Image image = imageMapper.selectById(slideBy.getImageId());
        if (image != null) {
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
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        // 查询标注表中信息
        Slide slide = slideMapperV1.selectById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }
        // 更新前数据
        // 更新文件中的内容
        Marking marking = new Marking();
        BeanUtils.copyProperties(req, marking);
        if (req.getCreate_by() != null) {
            marking.setUpdate_by(req.getCreate_by());
            SysUser user = userMapper.selectUserById(req.getCreate_by());
            if (user != null) {
                marking.setAnnotation_owner(user.getUserName());
            }
        } else {
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
        if (req.getCategory_id() != null) {
            slideAttrService.saveAnnoCategory(slide.getSlideId(), Collections.singletonList(req.getCategory_id()));
        } else {
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
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        Marking markingBy = markingMapper.selectById(markingId);
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        Slide slide = slideMapperV1.selectById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
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
    public String slideJsonExport(Long slideId) throws Exception {
        if (!Optional.ofNullable(slideId).isPresent()) {
            try {
                throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Slide slideBy = slideMapperV1.selectById(slideId);
        if (!Optional.ofNullable(slideBy).isPresent()) {
            try {
                throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        String fileUrl = null;
        try {
            fileUrl = fileService.createFiles(slideId, FILE_SUFFIX_JSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 标注数据
        List<Features> features = markingMapper.selectLists(slideId);
        features.forEach(i -> i.setGeometry(GeometryUtil.updateYAxle(i.getGeometry())));

        // 查询项目详情
        JsonExport jsonExport = null;
        Project projectBy = projectMapperV1.selectById(slideBy.getProjectId());
        if (Objects.equals(projectBy.getProjectType(), "2")) {
            jsonExport = markingMapper.jsonExportSelect(slideId);
        } else {
            jsonExport = markingMapper.reviewJsonExportSelect(slideId);
        }

        // 项目信息
        GeoProject project = new GeoProject();
        // 种属编码 + 结构编码 + 数据库项目id
        String projectId = jsonExport.getSpeciesId() + GLIDE_LINE + jsonExport.getOrganId() + GLIDE_LINE + jsonExport.getProjectId();
        project.setProject_id(projectId);
        project.setProject_name(jsonExport.getProjectName());

        // 图像信息
        GeoImage image = new GeoImage();
        image.setImage_shape(jsonExport.getImageShape());
        image.setImage_type(jsonExport.getFormat());
        image.setImage_name(jsonExport.getImageName());
        image.setCreate_time(jsonExport.getCreateTime());
        // 获取切片中的geo_image_id,为空则使用以下规则进行生成（项目id + 十三位时间戳 + 两位随机数）
        String imageId = "";
        if (Objects.equals(slideBy.getGeoImageId(), "") || slideBy.getGeoImageId() == null) {
            imageId = jsonExport.getProjectId() + GLIDE_LINE + System.currentTimeMillis() + GLIDE_LINE + RandomUtils.RandomNumbers();
            Slide slides = new Slide();
            slides.setSlideId(slideId);
            slides.setGeoImageId(imageId);
            slideMapperV1.updateById(slides);
        } else {
            imageId = slideBy.getGeoImageId();
        }
        image.setImage_id(imageId);
        image.setImage_url(jsonExport.getImageUrl());

        // 作者信息
        GeoAttribute attribute = new GeoAttribute();
        attribute.setAuthor(SecurityUtils.getLoginUser().getSysUser().getUserName());
        attribute.setDepartment(SecurityUtils.getLoginUser().getSysUser().getDept());

        // 标签信息
        QueryWrapper<cn.staitech.anno.project.domain.Marking> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper.select("category_id").eq("slide_id", slideId).ne("category_id", 0).groupBy("category_id");
        List<cn.staitech.anno.project.domain.Marking> markingList = markingMapperV1.selectList(markingQueryWrapper);
        List<GeoLabel> categoryList = new ArrayList<>();
        if (markingList.size() > 0) {
            for (cn.staitech.anno.project.domain.Marking marking : markingList) {
                GeoLabel geoLabel = pathologicalIndicatorCategoryMapper.selectGeoLabel(marking.getCategoryId());
                categoryList.add(geoLabel);
            }
        }

        // 构建geoJson数据
        GeoJson geoJson = new GeoJson();
        geoJson.setFeatures(features);
        geoJson.setImage(image);
        geoJson.setProject(project);
        geoJson.setAttribute(attribute);
        geoJson.setLabel_info(categoryList);
        String jsonString = JSON.toJSONString(geoJson, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        // 写入文件
        exportJson(fileUrl, jsonString);
//            return R.ok(fileUrl);
//        });
        return fileUrl;
    }

    @Override
    public boolean zipExport(String zipUrl, Long projectId) throws Exception {
        StringBuilder sb;
        File file1 = new File(zipUrl);
        Map<String, String> ddlList = new HashMap<>(16);
        try {
            // 查询切片列表
            List<SlideRes> slideResList = slideMapper.selectImageList(projectId);
            // zip可以包含对个文件，如果只有一个文件，则只解析一个文件的，包含多个文件则分别解析
            // 必须指明读取的各式，不然会存在问题
            ZipFile zipFile = new ZipFile(file1, Charset.forName("gbk"));
            // 按流的方式读取文件，输入到管道中
            InputStream in = new BufferedInputStream(Files.newInputStream(file1.toPath()));
            // 字节流转换为压缩文件输入流，通常用来读取压缩文件
            ZipInputStream zp = new ZipInputStream(in);
            // 定义文件条目
            ZipEntry ze;
            Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
            // 循环压缩包中解压内容
            while (zipEnum.hasMoreElements()) {
                // 获取下一个元素
                ze = zipEnum.nextElement();
                sb = new StringBuilder();
                if (!ze.isDirectory()) {
                    long size = ze.getSize();
                    if (size > 0) {
                        //读取文件内容
                        BufferedReader bf = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze), StandardCharsets.UTF_8));
                        String line;
                        while ((line = bf.readLine()) != null) {
                            sb.append(line);
                        }
                        // 获取文件中的内容
                        org.json.JSONObject jsonObject = new org.json.JSONObject(sb.toString());
                        // 获取图像相关信息
                        org.json.JSONObject image = jsonObject.getJSONObject("image");
                        if (image != null) {
                            // 获取标注名称
                            String imageName = image.getString("image_name");

                            String geoImageId = image.getString("image_id");
                            if (imageName != null) {
                                // 写入数据库
                                writeMarking(slideResList, imageName, jsonObject, geoImageId);
                            }
                            //这里是对读取的文件内容进行处理
                            ddlList.put(ze.getName(), sb.toString());
                            bf.close();
                        }
                    }
                }
                zp.closeEntry();
            }
        } catch (Exception e) {
            throw new Exception("json文件解析失败");
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void writeMarking(List<SlideRes> slideResList, String imageName, org.json.JSONObject jsonObject, String geoImageId) throws Exception {

        Map<String, Long> categoryMap = new HashMap<>(16);
        for (SlideRes slideRes : slideResList) {
            // 获取数据库文件名称
            String slideImageName = slideRes.getImageName();
            // 判断名称相等，获取切片id
            if (Objects.equals(imageName, slideImageName)) {
                // 查询切片详情
                Slide slideBy = slideMapperV1.selectById(slideRes.getSlideId());
                Image image = imageMapper.selectById(slideBy.getImageId());
                org.json.JSONArray features = jsonObject.getJSONArray("features");
                JSONArray jsonArray = JSONArray.parseArray(String.valueOf(features));
                // 删除当前切片中所有标注
                QueryWrapper<cn.staitech.anno.project.domain.Marking> markingQueryWrapperBy = new QueryWrapper<>();
                markingQueryWrapperBy.eq("slide_id", slideBy.getSlideId());
                markingMapperV1.delete(markingQueryWrapperBy);
                // 更新切片表中json geoImageId
                Slide slides = new Slide();
                slides.setSlideId(slideBy.getSlideId());
                slides.setGeoImageId(geoImageId);
                slideMapperV1.updateById(slides);

                for (Object feature : jsonArray) {
                    JSONObject featureObject = (JSONObject) feature;
                    // 获取annotationId
                    String annotationId = featureObject.getString("id");
                    // 获取geometry数据
                    JSONObject geometry = featureObject.getJSONObject("geometry");
                    // 获取属性和自定义字段
                    JSONObject properties = featureObject.getJSONObject("properties");
                    Properties properties1 = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(properties)), Properties.class);
                    cn.staitech.anno.project.domain.Marking marking = new cn.staitech.anno.project.domain.Marking();
                    // 查询标签信息
                    if (!Objects.equals(properties1.getLabel_code(), "") && properties1.getLabel_code() != null) {
                        Long categoryId = categoryMap.get(properties1.getLabel_code());
                        if (categoryId == null) {
                            PathologicalIndicatorCategory pathologicalIndicatorCategory = pathologicalIndicatorCategoryMapper.selectProjectAndNumber(Long.valueOf(slideBy.getProjectId()), properties1.getLabel_code());
                            if (pathologicalIndicatorCategory != null) {
                                marking.setCategoryId(pathologicalIndicatorCategory.getCategoryId());
                                categoryMap.put(properties1.getLabel_code(), pathologicalIndicatorCategory.getCategoryId());
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
                    // 写入数据库
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
                    // 使用json文件中标注作者
                    marking.setCreateBy(Long.valueOf(properties1.getAnnotation_owner()));
                    marking.setGeometry(GeometryUtil.updateYAxle(geometry));
                    marking.setSlideId(slideRes.getSlideId());
                    marking.setCreateTime(new Date());
                    // 查询标注是否存在
                    QueryWrapper<cn.staitech.anno.project.domain.Marking> markingQueryWrapper = new QueryWrapper<>();
                    markingQueryWrapper
                            .eq("slide_id", marking.getSlideId())
                            .eq("measure_name", marking.getMeasureName())
                            .eq("number", marking.getNumber())
                            .eq("category_id", marking.getCategoryId())
                    ;
                    List<cn.staitech.anno.project.domain.Marking> markingList = markingMapperV1.selectList(markingQueryWrapper);
                    if (markingList.size() < 1) {
                        int res = markingMapperV1.insert(marking);
                        slideAttrService.saveAnnoUsers(marking.getSlideId(), Collections.singletonList(marking.getCreateBy()));
                        if (marking.getCategoryId() == null) {
                            slideAttrService.saveAnnoCategory(marking.getSlideId(), new ArrayList<>());
                        } else {
                            slideAttrService.saveAnnoCategory(marking.getSlideId(), Collections.singletonList(marking.getCategoryId()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void execlExport(Long slideId) throws Exception {
        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(CommonConstant.MEASURE_COLHEAD_KEY, CommonConstant.MEASURE_COLHEAD_VALUE);
        // 查询当前切片不为点类型的标注数据
        List<Properties> propertiesList = markingMapper.selectMeasureList(slideId);
        // 加点的记录
        QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper.eq("slide_id", slideId).eq("location_type", "Point");
        int marking = markingMapper.selectCount(markingQueryWrapper);
        Properties properties = new Properties();
        properties.setPoint_count(marking);
        properties.setMeasure_name("P");
        propertiesList.add(properties);
        // 生成excel文件
        ExcelTool excelTool = new ExcelTool(MessageSource.M("EXCEL_TITLE"), 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        excelTool.exportExcel(titleData, propertiesList, response.getOutputStream(), true, false);
    }

    @Override
    public DownTask projectJsonExport(Long projectId, List<Long> slideIds) throws Exception {

        Project projectBy = projectMapperV1.selectById(projectId);
        if (projectBy == null) {
            throw new Exception(MessageSource.M("NOT_FOND_PROJECT"));
        }
        Snowflake snowflake = new Snowflake();
        Long userId = SecurityUtils.getUserId();
        DownTask task = DownTask.builder().code(snowflake.nextIdStr()).status(Constants.DOWN_STATE_RUNNING).createTime(new Date()).updateTime(new Date()).updateBy(userId).createBy(userId).build();
        downTaskMapper.insert(task);
        // 执行任务
        // 查询所有的切片
        executor.submit(new TaskThread(task, projectId, projectBy.getProjectName(), slideIds));
        return task;

    }

    @Override
    public void batchDelete(Long slideId) {
        QueryWrapper<cn.staitech.anno.project.domain.Marking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("slide_id", slideId);
        markingMapperV1.delete(queryWrapper);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(CLEAN, new Features());
        NioWebSocketHandler.sendAll(slideId, broadcastVO);
    }

    @Override
    public void downTaskByCode(String code, HttpServletResponse response) throws Exception {
        DownTask downTask = downTaskService.getOne(Wrappers.query(DownTask.builder().code(code).build()));
        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(CommonConstant.EXPORT_COLHEAD_KEY, CommonConstant.EXPORT_COLHEAD_VALUE);
        List<Map<String, String>> res = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(String.valueOf(downTask.getPath()));
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            res.add((Map<String, String>) entry.getValue());
        }
        ExcelTool<Map<String, String>> excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downTask.getProjectName(), "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
        excelTool.exportExcel(titleData, res, response.getOutputStream(), true, false);
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

    //    @SneakyThrows
//    @Async
    public void exportJson(String fileUrl, String jsonString) {
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
        slideMapperV1.updateById(slide);
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

    class TaskThread implements Runnable {

        private final DownTask downTask;
        private final Long projectId;
        private final String projectName;
        private List<Long> slideIds;

        public TaskThread(DownTask downTask, Long projectId, String projectName, List<Long> slideIds) {
            this.downTask = downTask;
            this.projectId = projectId;
            this.projectName = projectName;
            this.slideIds = slideIds;
        }

        @Override
        public void run() {
            try {
                JSONObject jsonObject = new JSONObject();
                if (slideIds == null || slideIds.isEmpty()) {
                    QueryWrapper<Slide> queryWrapper = Wrappers.query();
                    queryWrapper.eq("project_id", projectId);
                    queryWrapper.select("slide_id");
                    List<Slide> slideList = slideMapperV1.selectList(queryWrapper);
                    slideIds = new ArrayList<>();
                    slideList.forEach(slide -> {
                        slideIds.add(slide.getSlideId());
                    });
                }
                if (slideIds != null && !slideIds.isEmpty()) {
                    for (Long slideId : slideIds) {
                        QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
                        markingQueryWrapper.eq("slide_id", slideId);
                        Integer markingCount = markingMapper.selectCount(markingQueryWrapper);
                        if (markingCount > 0) {
                            // 将文件生成在本地
                            String fileUrl = null;
                            try {
                                fileUrl = slideJsonExport(slideId);
                                fileUrl = fileUrl.replace(" ", "\\ ");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            Slide slideBy = slideMapperV1.selectById(slideId);
                            Image image = imageMapper.selectById(slideBy);
                            Map<String, String> map = new HashMap<>(16);
                            map.put(CommonConstant.PATH, fileUrl);
                            map.put(CommonConstant.IMAGE_URL, image.getImageUrl());
                            jsonObject.put(String.valueOf(slideId), map);
                        }
                    }
                }
                downTask.setProjectName(projectName);
                downTask.setPath(jsonObject);
                downTask.setProjectId(projectId);
                downTask.setStatus(Constants.DOWN_STATE_FINISH);
                downTaskMapper.updateById(downTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public Boolean markIsNotFinish(String status) {
//        return Objects.equals(status, "7");
//    }


}
