package cn.staitech.anno.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.exception.AnnoException;
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
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.project.service.SlideAttrService;
import cn.staitech.anno.service.FileService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.utils.*;
import cn.staitech.anno.vo.annotation.BroadcastVO;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.geojson.*;
import cn.staitech.anno.vo.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.vo.geojson.in.UpdateOperationIn;
import cn.staitech.anno.vo.geojson.in.ViewAddIn;
import cn.staitech.anno.vo.marking.Marking;
import cn.staitech.anno.vo.marking.MarkingSelectListVO;
import cn.staitech.anno.vo.marking.PointCount;
import cn.staitech.anno.vo.slide.SlideRes;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.redis.service.RedisService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.operation.overlay.OverlayOp;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static cn.staitech.anno.aspect.LogFileAspect.response;
import static cn.staitech.anno.constant.CommonConstant.*;
import static org.reflections.Reflections.log;

@Service
@Slf4j
public class MarkingServiceImpl implements MarkingService {

	// GeometryFactory工厂，参数一：数据精度 参数二空间参考系SAID
	private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
	// 熟知文本WKT阅读器，可以将WKT文本转换为Geometry对象
	private static final WKTReader wktReader = new WKTReader(geometryFactory);
	private static final int BATCH_SIZE = 5000;

	private static final ExecutorService executor = ExecutorBuilder.create().setCorePoolSize(Runtime.getRuntime().availableProcessors()).setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2).setKeepAliveTime(0).build();
	private static final ExecutorService annExecutor = ExecutorBuilder.create()
			.setCorePoolSize(Runtime.getRuntime().availableProcessors())
			.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2)
			.setKeepAliveTime(0)
			.setWorkQueue(new LinkedBlockingQueue<Runnable>(4096))
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
	private MarkingServiceV1 markingServiceV1;
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
	@Autowired
	private RedisService redisService;

	@Override
	public PageResponse<MarkingSelectListVO> selectList(Long slideId, Integer pageNum, Integer pageSize, String measureFullName) throws Exception {
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

		Map<String, Object> map = new HashMap<>();
		map.put("slideId", slideId);
		map.put("measureFullName", measureFullName);
		map.put("pageSize", pageSize);
		map.put("pageNum", pageNum * pageSize);
		// 查询总数量
		Integer markingCount = markingMapper.selectListCount(map);
		List<MarkingSelectListVO> pointCountList = markingMapper.selectPointCountList(map);
		List<MarkingSelectListVO> markingSelectListVoList = markingMapper.selectList(map);
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


	//@Async
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String insert(ViewAddIn req) throws Exception {
		//TODO  imageId projectId 前端出入可进一步优化
		if (req.getGeometry() != null) {
			if (req.getGeometry().isEmpty()) {
				log.info("标注数据异常:" + req.getGeometry() + "------------------------------------------------->");
				return "更新失败，轮廓数据不能为空";
			}else{
				MarkingUtils.addVerify(req.getGeometry());
			}
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

		//BeanUtils.copyProperties(req, marking);
		Marking marking = trans2Marking(req);

		// 获取规定的geoJson Id
		String annotationId = CustomizationIdUtils.getSdId();
		marking.setAnnotation_id(annotationId);
		/*if (req.getArea() != null) {
            Double area = new Double(req.getArea()) * MICRON;
            marking.setArea(String.valueOf(area));
        }
        if (req.getPerimeter() != null) {
            Double perimeter = new Double(req.getPerimeter()) * MICRON;
            marking.setPerimeter(String.valueOf(perimeter));
        }*/
//		Image image = getImageById(slideBy.getImageId().longValue());
//		if (req.getArea() != null) {
//			Double area = 0.0;
//			if(null != image && StringUtils.isNotEmpty(image.getResolutionX())){
//				area = new Double(req.getArea()) * Double.valueOf(image.getResolutionX()) * Double.valueOf(image.getResolutionX());
//			}else{
//				area = new Double(req.getArea()) * MICRON;
//			}
//			marking.setArea(String.valueOf(area));
//		}
//		if (req.getPerimeter() != null) {
//			Double perimeter = 0.0;
//			if(null != image && StringUtils.isNotEmpty(image.getResolutionX())){
//				perimeter = new Double(req.getPerimeter()) * Double.valueOf(image.getResolutionX());
//			}else{
//				perimeter = new Double(req.getPerimeter()) * MICRON;
//			}
//			marking.setPerimeter(String.valueOf(perimeter));
//		}
		marking.setArea(req.getArea());
		marking.setPerimeter(req.getPerimeter());
		// 若未传入标注作者,使用当前登录用户为标注作者==>必传Create_by 无默认
		marking.setCreate_by(req.getCreate_by());
		marking.setAnnotation_type("Draw");
		marking.setOrganization_id(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
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
		int number = 1;
		QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
		// 根据切片和测量轮廓名称查询最大值
		markingQueryWrapper.eq("slide_id", req.getSlide_id()).eq("measure_name", req.getMeasure_name()).orderByDesc("create_time").last("limit 1");
		Marking markingBy = markingMapper.selectOne(markingQueryWrapper);
		if (markingBy != null) {
			if (markingBy.getNumber() != null) {
				number += markingBy.getNumber();
			}
		}
		marking.setNumber(number);
		marking.setProject_id(Long.valueOf(slideBy.getProjectId()));
		//加image缓存
		/*Image image = redisService.getCacheObject(CommonConstant.ANNO_IMAGE+slideBy.getImageId());
		if(null == image){
			image = imageMapper.selectById(slideBy.getImageId());
			redisService.setCacheObject(CommonConstant.ANNO_IMAGE+slideBy.getImageId(), image, CommonConstant.IMAGE_CACHE_HOURS, TimeUnit.HOURS);
		}
		if (image != null) {
			marking.setImage_id(image.getImageId());
			marking.setImage_url(image.getImageUrl());
		}*/

		// 添加数据库，添加后返回自增id
		markingMapper.insert(marking);

		Properties properties = markingMapper.selectBy(marking.getMarking_id());
		Features features = MarkingUtils.socketData(annotationId, marking.getGeometry(), properties);
		// 如果是点类型，返回点的总数并返回
		List<PointCount> pointCountList = updatePoint(marking.getLocation_type(), marking);
		//        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features, pointCountList);
		BroadcastVO broadcastVO = SendMessage.sendListMessages(CommonConstant.ANNO_TYPE_DRAW,ADD_STATUS, features, pointCountList);

		NioWebSocketHandler.sendAll(req.getSlide_id(), broadcastVO);

		//TODO 多线程处理
		annExecutor.submit(new AnnCountThread(1, slideBy, marking));

		// 更新切片表中最新状态
		/* updateSLide(marking.getSlide_id());
        slideAttrService.saveAnnoUsers(req.getSlide_id(), Collections.singletonList(marking.getCreate_by()));
        slideAttrService.saveAnnoCategory(req.getSlide_id(), Collections.singletonList(marking.getCategory_id()));*/
		return marking.getMarking_id();
	}


	/**
	 * 首次校验：校验轮廓是否需要二次校验
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public double operationCheck(UpdateOperationIn req) throws Exception {
		Marking markingBy = markingMapper.selectById(req.getMarking_id());
		// 查询数据是否存在
		if (!Optional.ofNullable(markingBy).isPresent()) {
			throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
		}
		Project project = projectMapperV1.selectById(markingBy.getProject_id());
		// 验证集项目中不能修改他人轮廓
		/*if (!Objects.equals(markingBy.getCreate_by(), SecurityUtils.getUserId()) && Objects.equals(project.getProjectType(), "3")) {
			throw new Exception(MessageSource.M("MARKINGSERVICEIMPL_UPDATE_MAN"));
		}*/
		return MarkingUtils.updateOperationVerify(markingBy.getGeometry(), req.getGeometry(), req.getOperation());
	}

	/**
	 * 	二次校验
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject updateOperation(UpdateOperationIn req) throws Exception {
		Marking markingBy = markingMapper.selectById(req.getMarking_id());
		// 查询数据是否存在
		if (!Optional.ofNullable(markingBy).isPresent()) {
			throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
		}
		Project project = projectMapperV1.selectById(markingBy.getProject_id());
		// 验证集项目中不能修改他人轮廓
		/*if (!Objects.equals(markingBy.getCreate_by(), SecurityUtils.getUserId()) && Objects.equals(project.getProjectType(), "3")) {
			throw new Exception(MessageSource.M("MARKINGSERVICEIMPL_UPDATE_MAN"));
		}*/
		// 合并 - 校验飞点 TODO: MarkingUtils.updatePolygonPoint(jsonObject);
		cn.staitech.anno.project.domain.Marking markingBys = MarkingUtils.updateVerify(markingBy.getGeometry(), req.getGeometry(), req.getOperation(), req.getCheck(), req.getResolution());
		JSONObject jsonObject = JSONObject.parseObject(WktUtil.wktToJson(markingBys.getMarkingId()));
		// 校验合并后的图形是否正常
		MarkingUtils.updatePoint(jsonObject);

		cn.staitech.anno.project.domain.Marking marking = new cn.staitech.anno.project.domain.Marking();
		marking.setGeometry(jsonObject);
		marking.setArea(markingBys.getArea());
		marking.setPerimeter(markingBys.getPerimeter());
		marking.setMarkingId(req.getMarking_id());
		marking.setUpdateBy(SecurityUtils.getUserId());
		marking.setUpdateTime(new Date());
		markingMapperV1.updateById(marking);
		// 更新后查询数据并返回
		Properties properties = markingMapper.selectBy(req.getMarking_id());
		Features features = MarkingUtils.socketData(markingBy.getAnnotation_id(), marking.getGeometry(), properties);
		// BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features);
		BroadcastVO broadcastVO = SendMessage.sendOneMessagesByAnnoType(CommonConstant.ANNO_TYPE_DRAW,UPDATE_STATUS, features);
		NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
		return jsonObject;
	}

	//@Async
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String update(MarkingUpdateIn req) throws Exception {
		// 查询标注表中信息==》先走缓存
		//		Marking markingBy = redisService.getCacheObject(CommonConstant.ANNO_MARKING+req.getMarking_id());
		Marking markingBy = markingMapper.selectById(req.getMarking_id());
		//		if(null == markingBy){
		//			redisService.setCacheObject(CommonConstant.ANNO_MARKING+req.getMarking_id(), markingBy, CommonConstant.MARKING_CACHE_HOURS, TimeUnit.HOURS);
		//		}
		if (!Optional.ofNullable(markingBy).isPresent()) {
			throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
		}
		Project project = projectMapperV1.selectById(markingBy.getProject_id());
		//验证集项目中不能修改他人轮廓
		/*if (!Objects.equals(markingBy.getCreate_by(), SecurityUtils.getUserId()) && Objects.equals(project.getProjectType(), "3")) {
			throw new Exception(MessageSource.M("MARKINGSERVICEIMPL_UPDATE_MAN"));
		}*/
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
		//BeanUtils.copyProperties(req, marking);
		Marking marking = updaeTrans2Marking(req);
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
			marking.setUpdate_by(SecurityUtils.getLoginUser().getSysUser().getUserId());
			marking.setAnnotation_update_owner(SecurityUtils.getLoginUser().getSysUser().getUserName());
		}
		marking.setUpdate_time(new Date());
		/*if (req.getArea() != null && !"".equals(req.getArea())) {
			Double area = new Double(req.getArea()) * MICRON;
			marking.setArea(String.valueOf(area));
		}
		if (req.getPerimeter() != null && !"".equals(req.getPerimeter())) {
			Double perimeter = new Double(req.getPerimeter()) * MICRON;
			marking.setPerimeter(String.valueOf(perimeter));
		}*/
//		Image image = getImageById(slide.getImageId().longValue());
//		if (req.getArea() != null) {
//			Double area = 0.0;
//			if(null != image && StringUtils.isNotEmpty(image.getResolutionX())){
//				area = new Double(req.getArea()) * Double.valueOf(image.getResolutionX()) * Double.valueOf(image.getResolutionX());
//			}else{
//				area = new Double(req.getArea()) * MICRON;
//			}
//			marking.setArea(String.valueOf(area));
//		}
//		if (req.getPerimeter() != null) {
//			Double perimeter = 0.0;
//			if(null != image && StringUtils.isNotEmpty(image.getResolutionX())){
//				perimeter = new Double(req.getPerimeter()) * Double.valueOf(image.getResolutionX());
//			}else{
//				perimeter = new Double(req.getPerimeter()) * MICRON;
//			}
//			marking.setPerimeter(String.valueOf(perimeter));
//		}
		marking.setArea(req.getArea());
		marking.setPerimeter(req.getPerimeter());
		List<PointCount> pointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
		// 修改轮廓时，轮廓为空
		if (req.getCategory_id() == null && req.getDescription() == null) {
			if (req.getGeometry() != null) {
				if (req.getGeometry().isEmpty()) {
					log.info("标注数据异常:" + req.getGeometry() + "------------------------------------------------->");
					throw new Exception("更新失败，轮廓数据不能为空");
				}
				//				else{
				//					// 将图形进行合并
				//					String location = updateVerify(markingBy.getGeometry(),req.getGeometry(),req.getOperation());
				//					JSONObject jsonObject = JSONObject.parseObject(location);
				//					marking.setGeometry(jsonObject);
				//				}
			} else {
				log.info("标注数据异常:" + req + "------------------------------------------------->");
				throw new Exception("修改标注数据异常，更新失败");
			}
		}
		markingMapper.updateById(marking);
		// 判断标签
		if (req.getCategory_id() != null) {
			if (req.getCategory_id() != 0 && !req.getCategory_id().equals(markingBy.getCategory_id())) {
				markingBy.setCategory_id(req.getCategory_id());
				List<PointCount> newPointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
				pointCountList = Stream.of(pointCountList, newPointCountList).flatMap(Collection::stream).collect(Collectors.toList());
			}
		}
		Properties properties = markingMapper.selectBy(marking.getMarking_id());
		Features features = MarkingUtils.socketData(markingBy.getAnnotation_id(), req.getGeometry(), properties);
		// BroadcastVO broadcastVO = SendMessage.sendOneMessages2(UPDATE_STATUS, features, pointCountList);
		BroadcastVO broadcastVO = SendMessage.sendListMessages(CommonConstant.ANNO_TYPE_DRAW,UPDATE_STATUS, features, pointCountList);

		// 使用websocket发送数据
		NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);

		/*// 更新切片表中数据
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
        }*/

		//TODO 多线程处理
		Marking markingNew = markingMapper.selectById(req.getMarking_id());
		annExecutor.submit(new AnnCountThread(2, slide, markingNew));

		return markingBy.getMarking_id();
	}


	@Override
	public int updatePointCount(Marking marking) {
		return markingMapper.updatePointCount(marking);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int delete(String markingId) throws Exception {
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
		Features features = MarkingUtils.socketData(markingBy.getAnnotation_id(), markingBy.getGeometry(), properties);
		List<PointCount> pointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
		//        BroadcastVO broadcastVO = SendMessage.sendOneMessages(DELETE_STATUS, features, pointCountList);
		BroadcastVO broadcastVO = SendMessage.sendListMessages(CommonConstant.ANNO_TYPE_DRAW,DELETE_STATUS, features, pointCountList);

		NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
		int res = markingMapper.delete(markingId);
		updateSLide(slide.getSlideId());
		// 更新前数据
		slideAttrService.removeAnnoUsers(slide.getSlideId(), Collections.singletonList(markingBy.getCreate_by()));
		slideAttrService.removeAnnoCategory(slide.getSlideId(), Collections.singletonList(markingBy.getCategory_id()));
		return res;
	}


	class TaskGenerateJson implements Runnable {


		private CountDownLatch countDownLatch;
		private Features features;
		private ConcurrentLinkedQueue<Features> concurrentLinkedQueue;

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

		List<Features> features = markingMapper.selectLists(slideId);
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		CountDownLatch countDownLatch = new CountDownLatch(features.size());
		ConcurrentLinkedQueue<Features> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
		for (Features features1 : features) {
			cachedThreadPool.submit(new TaskGenerateJson(countDownLatch, features1, concurrentLinkedQueue));

		}
		countDownLatch.await();
		cachedThreadPool.shutdown();

		//        features.forEach(i -> i.setGeometry(GeometryUtil.updateYAxle(i.getGeometry())));

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
		attribute.setAuthor(sysUser.getUserName());
		attribute.setDepartment(sysUser.getDept());

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


	/**
	 * 根据标签生成多个json文件
	 *
	 * @param slideId
	 * @param sysUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public String slideLabelJsonExport(Long slideId, SysUser sysUser) throws InterruptedException {
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
		// 查询分组后得标签列表（RO）
		// 根据RO标签获取唯一标识符号，并获取考核区域和标注区域得列表
		// 查询标注列表使用使用in查询出相应标注
		List<cn.staitech.anno.domain.PathologicalIndicatorCategory> categoryLists = markingMapper.selectCategory(slideId);
		// 循环标签列表
		List<String> fileUrlList = new ArrayList<>();

		for (PathologicalIndicatorCategory category : categoryLists) {
			// 查询标注路轮廓为ROE（标注考核）的标签
			Map<String, Object> categoryMap = new HashMap<>();
			categoryMap.put("categoryCode", category.getCategoryCode());
//			categoryMap.put("organizationId", sysUser.getOrganizationId());
			PathologicalIndicatorCategory pathologicalIndicatorCategory = pathologicalIndicatorCategoryMapper.selectRoe(categoryMap);
			if (pathologicalIndicatorCategory != null) {
				String fileUrl = null;
				try {
					fileUrl = fileService.createFiles(slideId, FILE_SUFFIX_JSON, category.getStructureId());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				QueryWrapper<PathologicalIndicatorCategory> pathologicalIndicatorCategoryQueryWrapper = new QueryWrapper<>();
				pathologicalIndicatorCategoryQueryWrapper.eq("category_code", category.getCategoryCode());
				// 根据categoryCode唯一标识查询一组类别数据
				List<PathologicalIndicatorCategory> categories = pathologicalIndicatorCategoryMapper.selectList(pathologicalIndicatorCategoryQueryWrapper);
				List<Long> categoryIdList = new ArrayList<>();
				if (categories.size() > 0) {
					for (PathologicalIndicatorCategory indicatorCategory : categories) {
						categoryIdList.add(indicatorCategory.getCategoryId());
					}
				}
				// 查询标注表中数据
				QueryWrapper<cn.staitech.anno.project.domain.Marking> markingQueryWrapper = new QueryWrapper<>();
				markingQueryWrapper.eq("category_id", pathologicalIndicatorCategory.getCategoryId()).eq("slide_id",slideId);
				int markingCountRes = markingMapperV1.selectCount(markingQueryWrapper);
				// 考核区域标签的数据大于0才可生成考题
				if (markingCountRes > 0) {
					Map<String, Object> map = new HashMap<>();
					map.put("slideId", slideId);
					map.put("categoryIdList", categoryIdList);
					List<Features> features = markingMapper.selectFilterCategoryLists(map);
					ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
					// 使用多线程更新标注y轴
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
					attribute.setAuthor(sysUser.getUserName());
					attribute.setDepartment(sysUser.getDept());
					//			// 标签信息
					List<GeoLabel> categoryList = new ArrayList<>();
					for (PathologicalIndicatorCategory i : categories) {
						GeoLabel geoLabel = new GeoLabel();
						geoLabel.setLabel_code(i.getStructureId());
						geoLabel.setLabel_color(i.getRgb());
						geoLabel.setLabel_name(i.getCategoryName());
						categoryList.add(geoLabel);
					}
					//
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
					fileUrlList.add(fileUrl);
				}
			}
		}
		return fileUrlList.stream().map(String::valueOf).collect(Collectors.joining(","));
	}


	@Override
	public boolean zipExport(String zipUrl, Long projectId) throws Exception {
		File file1 = new File(zipUrl);
		try {
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
						parseJson(bf, newBf, slideResList);
						bf.close();
					}
				}
				zp.closeEntry();
			}
		} catch (Exception e) {
			throw new Exception("json文件解析失败");
		}
		return true;
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
					QueryWrapper<cn.staitech.anno.project.domain.Marking> markingQueryWrapperBy = new QueryWrapper<>();
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

	@Override
	public void execlExport(Long slideId, HttpServletResponse response) throws Exception {
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
		//在标注类项目，切片列表页面，批量导出JSON时，去掉未交付的图片，不允许随时导出未交付的json
		if (Objects.equals(projectBy.getProjectType(), "1") && CollectionUtil.isNotEmpty(slideIds)) {
			List<Long> slideIdList = slideIds.stream().filter(e -> {
				Slide slideBy = slideMapperV1.selectById(e);
				if (Objects.equals(slideBy.getStatus(), "7")) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			// 执行任务
			// 查询所有的切片
			executor.submit(new TaskThread(task, projectId, projectBy.getProjectName(), slideIdList, SecurityUtils.getLoginUser().getSysUser()));

		} else {
			// 执行任务
			// 查询所有的切片
			executor.submit(new TaskThread(task, projectId, projectBy.getProjectName(), slideIds, SecurityUtils.getLoginUser().getSysUser()));
		}

		return task;

	}

	@Override
	public void batchDelete(Long slideId) {
		QueryWrapper<cn.staitech.anno.project.domain.Marking> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("slide_id", slideId);
		markingMapperV1.delete(queryWrapper);
		// BroadcastVO broadcastVO = SendMessage.sendOneMessages(CLEAN, new Features());
		BroadcastVO broadcastVO = SendMessage.sendOneMessagesByAnnoType(CommonConstant.ANNO_TYPE_DRAW,CLEAN, new Features());
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
		private SysUser sysUser;

		public TaskThread(DownTask downTask, Long projectId, String projectName, List<Long> slideIds, SysUser sysUser) {
			this.downTask = downTask;
			this.projectId = projectId;
			this.projectName = projectName;
			this.slideIds = slideIds;
			this.sysUser = sysUser;
		}

		@Override
		public void run() {
			try {
				JSONObject jsonObject = new JSONObject();
				if (slideIds == null || slideIds.isEmpty()) {
					QueryWrapper<Slide> queryWrapper = Wrappers.query();
					queryWrapper.eq("project_id", projectId);
					queryWrapper.select("slide_id", "status");
					List<Slide> slideList = slideMapperV1.selectList(queryWrapper);

					slideIds = new ArrayList<>();
					// 在标注类项目，切片列表页面，批量导出JSON时，去掉未交付的图片，不允许随时导出未交付的json
					Project project = projectMapperV1.selectById(projectId);
					if (Objects.equals(project.getProjectType(), "1") && CollectionUtil.isNotEmpty(slideList)) {
						List<Slide> slideLists = slideList.stream().filter(s -> Objects.equals(s.getStatus(), "7")).collect(Collectors.toList());
						slideLists.forEach(slide -> {
							slideIds.add(slide.getSlideId());
						});
					} else {
						slideList.forEach(slide -> {
							slideIds.add(slide.getSlideId());
						});
					}
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
								fileUrl = slideJsonExport(slideId, sysUser);
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


	public void process(Integer type, Slide slide, Marking marking) throws Exception {

		Long slideId = marking.getSlide_id();
		String markIngId = marking.getMarking_id();
		// String annotationId = marking.getAnnotation_id();
		Long createBy = marking.getCreate_by();
		Long categoryId = marking.getCategory_id();

		//增加缓存
		redisService.setCacheObject(CommonConstant.ANNO_MARKING + markIngId, marking, CommonConstant.MARKING_CACHE_HOURS, TimeUnit.HOURS);

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
		slideAttrService.saveAnnoUsers(slideId, Collections.singletonList(createBy));
		if (categoryId != null) {
			slideAttrService.saveAnnoCategory(slideId, Collections.singletonList(categoryId));
		} else {
			slideAttrService.saveAnnoCategory(slideId, new ArrayList<>());
		}
	}

	class AnnCountThread implements Runnable {
		// type 1:标注保存  2：标注修改
		private final Integer type;
		private final Slide slide;
		private final Marking marking;


		public AnnCountThread(Integer type, Slide slide, Marking marking) {
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


	private Marking trans2Marking(ViewAddIn view) {
		Marking marking = new Marking();
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


	private Marking updaeTrans2Marking(MarkingUpdateIn view) {
		Marking marking = new Marking();
		marking.setMarking_id(view.getMarking_id());
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

	public Image getImageById(Long imageId){
		Image image = redisService.getCacheObject(CommonConstant.ANNO_IMAGE+imageId);
		if(null == image){
			image = imageMapper.selectById(imageId);
			redisService.setCacheObject(CommonConstant.ANNO_IMAGE+imageId, image, CommonConstant.IMAGE_CACHE_HOURS, TimeUnit.HOURS);
		}
		return image;
	}
}
