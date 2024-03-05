package cn.staitech.anno.service.impl;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.MarkMeasure;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.MarkMeasureMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.mapper.SysUserMapper;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.service.SlideAttrService;
import cn.staitech.anno.service.FileService;
import cn.staitech.anno.service.MarkMeasureService;
import cn.staitech.anno.utils.*;
import cn.staitech.anno.vo.annotation.BroadcastVO;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.geojson.*;
import cn.staitech.anno.vo.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.vo.geojson.in.UpdateOperationIn;
import cn.staitech.anno.vo.geojson.in.ViewAddIn;
import cn.staitech.anno.vo.marking.MarkingSelectListVO;
import cn.staitech.anno.vo.marking.PointCount;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.utils.uuid.IdUtils;
import cn.staitech.common.redis.service.RedisService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.staitech.anno.constant.CommonConstant.*;

/**
 * <p>
 * 标注测量表 服务实现类
 * </p>
 *
 * @author wanglibei
 * @since 2023-12-05
 */
@Service
@Slf4j
public class MarkMeasureServiceImpl extends ServiceImpl<MarkMeasureMapper, MarkMeasure> implements MarkMeasureService {

    private static final ExecutorService ANN_EXECUTOR = ExecutorBuilder.create()
            .setCorePoolSize(Runtime.getRuntime().availableProcessors())
            .setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2)
            .setKeepAliveTime(0)
            .setWorkQueue(new LinkedBlockingQueue<Runnable>(4096))
            .build();

    @Resource
    private SlideMapperV1 slideMapperV1;
    @Resource
    private SlideAttrService slideAttrService;
    @Resource
    private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;
    @Resource
    private MarkMeasureMapper markMeasureMapper;
    @Resource
    private FileService fileService;
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private ProjectMapperV1 projectMapperV1;
    @Resource
    private RedisService redisService;

    @Override
    public PageResponse<MarkingSelectListVO> list(Long slideId, Integer pageNum, Integer pageSize, String measureFullName) throws Exception {
        Slide slideBy = slideMapperV1.selectById(slideId);
        if (!Optional.ofNullable(slideBy).isPresent()) {
            throw new Exception(MessageSource.M("SLIDE_ABNORMAL_NO_INFORMATION"));
        }
        Integer resPageNum = pageNum;
        if (pageNum > 0) {
            pageNum = pageNum - 1;
        } else {
            pageNum = 0;
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("slideId", slideId);
        map.put("measureFullName", measureFullName);
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum * pageSize);
        // 查询总数量
        Integer markingCount = markMeasureMapper.selectListCount(map);
        List<MarkingSelectListVO> pointCountList = markMeasureMapper.selectPointCountList(map);
        List<MarkingSelectListVO> markingSelectListVoList = markMeasureMapper.selectList(map);
        markingCount = markingCount + pointCountList.size();
        // 总页数
        int pageShow = (markingCount / pageSize) + 1;
        PageResponse<MarkingSelectListVO> resp = new PageResponse<>();
        // 查询考核评分表中信息
        if (markingSelectListVoList.size() < pageSize) {
            for (MarkingSelectListVO markingSelectListVO : pointCountList) {
                if (markingSelectListVoList.size() < pageSize) {
                    markingSelectListVoList.add(markingSelectListVO);
                }
            }
        }
        resp.setTotal(markingCount);
        resp.setList(markingSelectListVoList);
        resp.setPages(pageShow);
        resp.setPageNum(resPageNum);
        resp.setPageSize(pageSize);
        return resp;
    }

    @Override
    public List<Features> selectListBy(Long slideId) throws Exception {
        Slide slideBy = redisService.getCacheObject(CommonConstant.ANNO_SLIDE + slideId);
        if (null == slideBy) {
            slideBy = slideMapperV1.selectById(slideId);
            redisService.setCacheObject(CommonConstant.ANNO_SLIDE + slideId, slideBy, CommonConstant.SLIDE_CACHE_HOURS, TimeUnit.HOURS);
        }
        if (!Optional.ofNullable(slideBy).isPresent()) {
            throw new Exception(MessageSource.M("SLIDE_ABNORMAL_NO_INFORMATION"));
        }
        return markMeasureMapper.selectListBy(slideId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(ViewAddIn req) throws Exception {
        if (req.getGeometry() != null && !req.getGeometry().isEmpty()) {
            MarkingUtils.addVerify(req.getGeometry());
        } else {
            log.info("标注数据异常:" + req.getGeometry());
            return "更新失败，轮廓数据不能为空";
        }

        //加slide缓存
        cn.staitech.anno.project.domain.Slide slideBy = redisService.getCacheObject(CommonConstant.ANNO_SLIDE + req.getSlide_id());
        if (null == slideBy) {
            slideBy = slideMapperV1.selectById(req.getSlide_id());
            redisService.setCacheObject(CommonConstant.ANNO_SLIDE + req.getSlide_id(), slideBy, CommonConstant.SLIDE_CACHE_HOURS, TimeUnit.HOURS);
        }
        if (slideBy == null) {
            return MessageSource.M("NO_SLIDE_DATA");
        }

        MarkMeasure marking = trans2Marking(req);
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        // 获取规定的geoJson Id
        String annotationId = CustomizationIdUtils.getSdId();
        marking.setPerimeter(req.getPerimeter());
        marking.setArea(req.getArea());
        // 若未传入标注作者,使用当前登录用户为标注作者==>必传Create_by 无默认
        marking.setCreate_by(req.getCreate_by());
        marking.setAnnotation_type("Measure");
        marking.setOrganization_id(sysUser.getOrganizationId());
        marking.setCreate_time(new Date());
        //加用户缓存
        SysUser user = redisService.getCacheObject(CommonConstant.SYS_USER + req.getCreate_by());
        if (null == user) {
            user = userMapper.selectUserById(req.getCreate_by());
            redisService.setCacheObject(CommonConstant.SYS_USER + req.getCreate_by(), user, CommonConstant.SYS_USER_CACHE_HOURS, TimeUnit.DAYS);
        }
        if (user != null) {
            marking.setAnnotation_owner(user.getUserName());
        }
        // 查询
        Long number = 1L;
        QueryWrapper<MarkMeasure> markingQueryWrapper = new QueryWrapper<>();
        // 根据切片和测量轮廓名称查询最大值
        markingQueryWrapper.eq("slide_id", req.getSlide_id()).eq("measure_name", req.getMeasure_name()).orderByDesc("create_time").last("limit 1");
        MarkMeasure markingBy = markMeasureMapper.selectOne(markingQueryWrapper);
        if (markingBy != null) {
            if (markingBy.getNumber() != null) {
                number += markingBy.getNumber();
            }
        }
        marking.setNumber(number);
        marking.setProject_id(Long.valueOf(slideBy.getProjectId()));

        String markMeasureId = IdUtils.randomUUID().replace("-", "");
        marking.setMark_measure_id(markMeasureId);
        // 添加数据库，添加后返回自增id
        markMeasureMapper.insert(marking);

        Properties properties = markMeasureMapper.selectBy(marking.getMark_measure_id());
        Features features = MarkingUtils.socketData(annotationId, marking.getGeometry(), properties);
        // 如果是点类型，返回点的总数并返回
        List<PointCount> pointCountList = updatePoint(marking.getLocation_type(), marking);
        BroadcastVO broadcastVO = SendMessage.sendListMessages(CommonConstant.ANNO_TYPE_MEASURE, ADD_STATUS, features, pointCountList);
        NioWebSocketHandler.sendAll(req.getSlide_id(), broadcastVO);

        ANN_EXECUTOR.submit(new AnnCountThread(1, slideBy, marking));

        return marking.getMark_measure_id();
    }


    @Override
    public double operationCheck(UpdateOperationIn req) throws Exception {
        MarkMeasure markingBy = markMeasureMapper.selectById(req.getMarking_id());
        // 查询数据是否存在
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        Project project = projectMapperV1.selectById(markingBy.getProject_id());
        //验证集项目中不能修改他人轮廓
        if (!Objects.equals(markingBy.getCreate_by(), SecurityUtils.getUserId()) && Objects.equals(project.getProjectType(), "3")) {
            throw new Exception(MessageSource.M("MARKINGSERVICEIMPL_UPDATE_MAN"));
        }
        return MarkingUtils.updateOperationVerify(markingBy.getGeometry(), req.getGeometry(), req.getOperation());
    }

    @Override
    public JSONObject updateOperation(UpdateOperationIn req) throws Exception {
        MarkMeasure markingBy = markMeasureMapper.selectById(req.getMarking_id());
        // 查询数据是否存在
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        Project project = projectMapperV1.selectById(markingBy.getProject_id());
        //验证集项目中不能修改他人轮廓
        if (!Objects.equals(markingBy.getCreate_by(), SecurityUtils.getUserId()) && Objects.equals(project.getProjectType(), "3")) {
            throw new Exception(MessageSource.M("MARKINGSERVICEIMPL_UPDATE_MAN"));
        }

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Marking markingBys = MarkingUtils.updateVerify(markingBy.getGeometry(), req.getGeometry(), req.getOperation(), req.getCheck(), req.getResolution());
        if(markingBys.getException() != null){
            throw new Exception(markingBys.getException());
        }
        JSONObject jsonObject = JSONObject.parseObject(WktUtil.wktToJson(markingBys.getMarkingId()));
        MarkMeasure marking = new MarkMeasure();
        marking.setGeometry(jsonObject);
        marking.setArea(markingBys.getArea());
        marking.setPerimeter(markingBys.getPerimeter());
        marking.setMark_measure_id(req.getMarking_id());
        marking.setUpdate_by(sysUser.getUserId());
        marking.setUpdate_time(new Date());
        markMeasureMapper.updateById(marking);
        // 更新后查询数据并返回
        Properties properties = markMeasureMapper.selectBy(req.getMarking_id());
        Features features = MarkingUtils.socketData(markingBy.getAnnotation_id(), marking.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessagesByAnnoType(CommonConstant.ANNO_TYPE_MEASURE, UPDATE_STATUS, features);
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        return jsonObject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(MarkingUpdateIn req) throws Exception {
        // 查询标注表中信息==》先走缓存
        MarkMeasure markingBy = markMeasureMapper.selectById(req.getMarking_id());
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        // 查询切片表中信息==》先走缓存
        Slide slide = redisService.getCacheObject(CommonConstant.ANNO_SLIDE + markingBy.getSlide_id());
        if (null == slide) {
            slide = slideMapperV1.selectById(markingBy.getSlide_id());
            redisService.setCacheObject(CommonConstant.ANNO_SLIDE + markingBy.getSlide_id(), slide, CommonConstant.SLIDE_CACHE_HOURS, TimeUnit.HOURS);
        }
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }
        // 更新前数据
        MarkMeasure marking = updaeTrans2Marking(req);
        if (null != req.getUpdate_by()) {
            marking.setUpdate_by(req.getUpdate_by());
            //加用户缓存
            SysUser user = redisService.getCacheObject(CommonConstant.SYS_USER + req.getUpdate_by());
            if (null == user) {
                user = userMapper.selectUserById(req.getUpdate_by());
                redisService.setCacheObject(CommonConstant.SYS_USER + req.getUpdate_by(), user, CommonConstant.SYS_USER_CACHE_HOURS, TimeUnit.DAYS);
            }
            if (user != null) {
                marking.setAnnotation_update_owner(user.getUserName());
            }
        } else {
            marking.setUpdate_by(sysUser.getUserId());
            marking.setAnnotation_update_owner(sysUser.getUserName());
        }
        marking.setUpdate_time(new Date());
        marking.setPerimeter(req.getPerimeter());
        marking.setArea(req.getArea());
        List<PointCount> pointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
        // 修改轮廓时，轮廓为空
        if (req.getCategory_id() == null && req.getDescription() == null) {
            if (req.getGeometry() != null) {
                if (req.getGeometry().isEmpty()) {
                    log.info("标注数据异常:" + req.getGeometry() + "------------------------------------------------->");
                    throw new Exception("更新失败，轮廓数据不能为空");
                }
            } else {
                log.info("标注数据异常:" + req + "------------------------------------------------->");
                throw new Exception("修改标注数据异常，更新失败");
            }
        }
        markMeasureMapper.updateById(marking);
        // 判断标签
        if (req.getCategory_id() != null) {
            if (req.getCategory_id() != 0 && !req.getCategory_id().equals(markingBy.getCategory_id())) {
                markingBy.setCategory_id(req.getCategory_id());
                List<PointCount> newPointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
                pointCountList = Stream.of(pointCountList, newPointCountList).flatMap(Collection::stream).collect(Collectors.toList());
            }
        }
        Properties properties = markMeasureMapper.selectBy(marking.getMark_measure_id());
        Features features = MarkingUtils.socketData(markingBy.getAnnotation_id(), req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendListMessages(CommonConstant.ANNO_TYPE_MEASURE, UPDATE_STATUS, features, pointCountList);
        // 使用websocket发送数据
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        MarkMeasure markingNew = markMeasureMapper.selectById(req.getMarking_id());
        ANN_EXECUTOR.submit(new AnnCountThread(2, slide, markingNew));

        return markingBy.getMark_measure_id();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String markingId) throws Exception {
        if (!Optional.ofNullable(markingId).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        MarkMeasure markingBy = markMeasureMapper.selectById(markingId);
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        Slide slide = slideMapperV1.selectById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }
        Properties properties = markMeasureMapper.selectBy(markingId);
        Features features = MarkingUtils.socketData(markingBy.getAnnotation_id(), markingBy.getGeometry(), properties);
        List<PointCount> pointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
        BroadcastVO broadcastVO = SendMessage.sendListMessages(CommonConstant.ANNO_TYPE_MEASURE, DELETE_STATUS, features, pointCountList);
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        int res = markMeasureMapper.delete(markingId);
        updateSLide(slide.getSlideId());
        // 更新前数据
        slideAttrService.removeAnnoUsers(slide.getSlideId(), Collections.singletonList(markingBy.getCreate_by()));
        slideAttrService.removeAnnoCategory(slide.getSlideId(), Collections.singletonList(markingBy.getCategory_id()));
        return res;
    }

    @Override
    public String slideJsonExport(Long slideId, SysUser sysUser) throws Exception {
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
            fileUrl = fileService.createFiles(slideId, FILE_SUFFIX_JSON, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Features> features = markMeasureMapper.selectLists(slideId);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(features.size());
        ConcurrentLinkedQueue<Features> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        for (Features features1 : features) {
            cachedThreadPool.submit(new TaskGenerateJson(countDownLatch, features1, concurrentLinkedQueue));

        }
        countDownLatch.await();
        cachedThreadPool.shutdown();

        // 查询项目详情
        JsonExport jsonExport = null;
        Project projectBy = projectMapperV1.selectById(slideBy.getProjectId());
        if (Objects.equals(projectBy.getProjectType(), "2")) {
            jsonExport = markMeasureMapper.jsonExportSelect(slideId);
        } else {
            jsonExport = markMeasureMapper.reviewJsonExportSelect(slideId);
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
        attribute.setAuthor(sysUser.getUserName());
        attribute.setDepartment(sysUser.getDept());

        // 标签信息
        QueryWrapper<MarkMeasure> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper.select("category_id").eq("slide_id", slideId).ne("category_id", 0).groupBy("category_id");
        List<MarkMeasure> markingList = markMeasureMapper.selectList(markingQueryWrapper);
        List<GeoLabel> categoryList = new ArrayList<>();
        if (markingList.size() > 0) {
            for (MarkMeasure marking : markingList) {
                GeoLabel geoLabel = pathologicalIndicatorCategoryMapper.selectGeoLabel(marking.getCategory_id());
                categoryList.add(geoLabel);
            }
        }
        // 构建geoJson数据
        GeoJson geoJson = new GeoJson();
        List<Features> featuresList = new ArrayList<>(concurrentLinkedQueue);
        geoJson.setFeatures(featuresList);
        geoJson.setImage(image);
        geoJson.setProject(project);
        geoJson.setAttribute(attribute);
        geoJson.setLabel_info(categoryList);
        String jsonString = JSON.toJSONString(geoJson, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        // 写入文件
        exportJson(fileUrl, jsonString);
        return fileUrl;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void execlExport(Long slideId, HttpServletResponse response) throws Exception {
        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(CommonConstant.MEASURE_COLHEAD_KEY, CommonConstant.MEASURE_COLHEAD_VALUE);
        // 查询当前切片不为点类型的标注数据
        List<Properties> propertiesList = markMeasureMapper.selectMeasureList(slideId);
        // 加点的记录
        QueryWrapper<MarkMeasure> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper.eq("slide_id", slideId).eq("location_type", "Point");
        int marking = markMeasureMapper.selectCount(markingQueryWrapper);
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

    /**
     * 统计不同类型点的数量
     *
     * @param locationType
     * @param marking
     * @return
     */
    public List<PointCount> updatePoint(String locationType, MarkMeasure marking) {
        List<PointCount> pointCountList = new ArrayList<>();
        if (Objects.equals(locationType, "Point")) {
            PointCount pointCounts = markMeasureMapper.selectCategoryCount(marking);
            marking.setPoint_count(pointCounts.getPoint_count().intValue());
            markMeasureMapper.updatePointCount(marking);
            pointCountList.add(pointCounts);
        }
        return pointCountList;
    }

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

    public void process(Integer type, Slide slide, MarkMeasure marking) throws Exception {
        Long slideId = marking.getSlide_id();
        Long createBy = marking.getCreate_by();
        Long categoryId = marking.getCategory_id();

        // 判断切片状态是否是未开始
        if (Objects.equals(slide.getStatus(), "1")) {
            // 更新切片表中状态至切片中
            slide.setStatus("2");
            slideMapperV1.updateById(slide);
        }

        // 更新切片表中最新状态
        updateSLide(slideId);
        if (type == 2) {
            //修改
            slideAttrService.removeAnnoUsers(slideId, Collections.singletonList(createBy));
            slideAttrService.removeAnnoCategory(slideId, Collections.singletonList(categoryId));
        }
        slideAttrService.saveAnnoUsers(slideId, Collections.singletonList(createBy), SecurityUtils.getUserId());
        if (categoryId != null) {
            slideAttrService.saveAnnoCategory(slideId, Collections.singletonList(categoryId), SecurityUtils.getUserId());
        } else {
            slideAttrService.saveAnnoCategory(slideId, new ArrayList<>(), SecurityUtils.getUserId());
        }
    }

    private MarkMeasure trans2Marking(ViewAddIn view) {
        MarkMeasure marking = new MarkMeasure();
        marking.setSlide_id(view.getSlide_id());
        if (null != view.getCreate_by()) {
            marking.setCreate_by(view.getCreate_by());
        }
        if (StringUtils.isNotEmpty(view.getArea())) {
            marking.setArea(view.getArea());
        }
        if (StringUtils.isNotEmpty(view.getPerimeter())) {
            marking.setPerimeter(view.getPerimeter());
        }
        if (null != view.getCategory_id()) {
            marking.setCategory_id(view.getCategory_id());
        }
        if (StringUtils.isNotEmpty(view.getLocation_type())) {
            marking.setLocation_type(view.getLocation_type());
        }
        if (StringUtils.isNotEmpty(view.getDescription())) {
            marking.setDescription(view.getDescription());
        }

        if (null != view.getGeometry()) {
            marking.setGeometry(view.getGeometry());
        }
        if (null != view.getMeasure_type()) {
            marking.setMeasure_type(view.getMeasure_type());
        }

        if (StringUtils.isNotEmpty(view.getMeasure_relation())) {
            marking.setMeasure_relation(view.getMeasure_relation());
        }
        if (StringUtils.isNotEmpty(view.getMeasure_name())) {
            marking.setMeasure_name(view.getMeasure_name());
        }
        if (null != view.getMeasure_number()) {
            marking.setMeasure_number(view.getMeasure_number());
        }
        if (StringUtils.isNotEmpty(view.getRadius())) {
            marking.setRadius(view.getRadius());
        }

        if (null != view.getMean_distance()) {
            marking.setMean_distance(view.getMean_distance());
        }
        if (null != view.getMax_distance()) {
            marking.setMax_distance(view.getMax_distance());
        }
        if (null != view.getMin_distance()) {
            marking.setMin_distance(view.getMin_distance());
        }
        if (StringUtils.isNotEmpty(view.getInner_angle())) {
            marking.setInner_angle(view.getInner_angle());
        }
        if (StringUtils.isNotEmpty(view.getExterior_angle())) {
            marking.setExterior_angle(view.getExterior_angle());
        }
        if (StringUtils.isNotEmpty(view.getCenter_point())) {
            marking.setCenter_point(view.getCenter_point());
        }
        return marking;
    }

    private MarkMeasure updaeTrans2Marking(MarkingUpdateIn view) {
        MarkMeasure marking = new MarkMeasure();
        marking.setMark_measure_id(view.getMarking_id());
        if (null != view.getUpdate_by()) {
            marking.setUpdate_by(view.getUpdate_by());
        }
        if (StringUtils.isNotEmpty(view.getArea())) {
            marking.setArea(view.getArea());
        }
        if (StringUtils.isNotEmpty(view.getPerimeter())) {
            marking.setPerimeter(view.getPerimeter());
        }
        if (null != view.getCategory_id()) {
            marking.setCategory_id(view.getCategory_id());
        }
        if (StringUtils.isNotEmpty(view.getLocation_type())) {
            marking.setLocation_type(view.getLocation_type());
        }
        if (StringUtils.isNotEmpty(view.getDescription())) {
            marking.setDescription(view.getDescription());
        }

        if (null != view.getGeometry()) {
            marking.setGeometry(view.getGeometry());
        }
        if (null != view.getMeasure_type()) {
            marking.setMeasure_type(view.getMeasure_type().intValue());
        }

        if (StringUtils.isNotEmpty(view.getMeasure_relation())) {
            marking.setMeasure_relation(view.getMeasure_relation());
        }
        if (StringUtils.isNotEmpty(view.getMeasure_name())) {
            marking.setMeasure_name(view.getMeasure_name());
        }
        if (null != view.getMeasure_number()) {
            marking.setMeasure_number(view.getMeasure_number().intValue());
        }
        if (StringUtils.isNotEmpty(view.getRadius())) {
            marking.setRadius(view.getRadius());
        }

        if (null != view.getMean_distance()) {
            marking.setMean_distance(view.getMean_distance());
        }
        if (null != view.getMax_distance()) {
            marking.setMax_distance(view.getMax_distance());
        }
        if (null != view.getMin_distance()) {
            marking.setMin_distance(view.getMin_distance());
        }
        if (StringUtils.isNotEmpty(view.getInner_angle())) {
            marking.setInner_angle(view.getInner_angle());
        }
        if (StringUtils.isNotEmpty(view.getExterior_angle())) {
            marking.setExterior_angle(view.getExterior_angle());
        }
        if (StringUtils.isNotEmpty(view.getCenter_point())) {
            marking.setCenter_point(view.getCenter_point());
        }
        return marking;
    }

    public Image getImageById(Long imageId) {
        Image image = redisService.getCacheObject(CommonConstant.ANNO_IMAGE + imageId);
        if (null == image) {
            image = imageMapper.selectById(imageId);
            redisService.setCacheObject(CommonConstant.ANNO_IMAGE + imageId, image, CommonConstant.IMAGE_CACHE_HOURS, TimeUnit.HOURS);
        }
        return image;
    }

    class TaskGenerateJson implements Runnable {


        private final CountDownLatch countDownLatch;
        private final Features features;
        private final ConcurrentLinkedQueue<Features> concurrentLinkedQueue;

        public TaskGenerateJson(CountDownLatch countDownLatch, Features features, ConcurrentLinkedQueue<Features> concurrentLinkedQueue) {
            this.countDownLatch = countDownLatch;
            this.features = features;
            this.concurrentLinkedQueue = concurrentLinkedQueue;
        }

        @Override
        public void run() {
            features.setGeometry(GeometryUtil.updateYAxle(features.getGeometry()));
            concurrentLinkedQueue.add(features);
            countDownLatch.countDown();
        }
    }

    class AnnCountThread implements Runnable {
        // type 1:标注保存  2：标注修改
        private final Integer type;
        private final Slide slide;
        private final MarkMeasure marking;

        public AnnCountThread(Integer type, Slide slide, MarkMeasure marking) {
            this.type = type;
            this.slide = slide;
            this.marking = marking;
        }

        @Override
        public void run() {
            try {
                process(this.type, this.slide, this.marking);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
