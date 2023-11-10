package cn.staitech.anno.controller;


import java.util.List;

import javax.annotation.Resource;

import cn.staitech.anno.domain.Slide;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.SlidePrediction;
import cn.staitech.anno.service.AlgorithmModelService;
import cn.staitech.anno.service.AlgorithmPredictionService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.service.SlidePredictionService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.service.remote.SlideImageService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.imagecsv.ImageCsvGetPagerVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvListVO;
import cn.staitech.anno.vo.predictionInfo.in.SetMainImageDataIn;
import cn.staitech.anno.vo.predictionInfo.in.SlideImagePagerVO;
import cn.staitech.anno.vo.predictionInfo.in.SlidePredictionIn;
import cn.staitech.anno.vo.predictionInfo.in.StartPredictionIn;
import cn.staitech.anno.vo.predictionInfo.out.SlidePredictionOut;
import cn.staitech.anno.vo.project.ProjectListVO;
import cn.staitech.anno.vo.project.in.ProjectListQueryIn;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 眼科切片预测表 前端控制器
 * </p>
 *
 * @author wanglibei
 * @since 2023-11-02
 */
@Api(value = "算法预测模块", tags = "算法预测模块")
@Slf4j
@RestController
@RequestMapping("/slidePrediction")
public class SlidePredictionController {

	@Resource
	private AlgorithmPredictionService algorithmPredictionService;

	@Resource
	private ProjectService projectService;

	@Resource
	private SlideImageService slideImageService;

	@Resource
	private AlgorithmModelService algorithmModelService;

	@Resource
	private SlidePredictionService slidePredictionService;

	@Resource
	private SlideService slideService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "算法项目列表分页查询")
	@PostMapping("/projectList")
	public R<PageMaster<List<ProjectListVO>>> getProjectList(@RequestBody @Validated ProjectListQueryIn req) {
		PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);
		Project project = new Project();
		BeanUtils.copyProperties(req, project);
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		project.setOrganizationId(organizationId);
		//判断当前角色是否为admin或者超级管理员
		if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
			project.setCreateBy(SecurityUtils.getUserId());
		}
		//项目列表来源于项目管理模块的项目类型为算法检测和图像拼接的项目,除了系统管理员外，每个用户只能看到自己参与的项目
		//项目类型:1标注2评审3标准训练集  6图像拼接  7算法预测
		Integer[] projectArray = {6,7};
		project.setProjectTypeArray(projectArray);
		List<ProjectListVO> list = projectService.selectProjectList(project);
		PageMaster pageMaster = new PageMaster<>(list);
		return R.ok(pageMaster);
	}


	@ApiOperation(value = "查询算法项目切片列表")
	@PostMapping("/slidePageList")
	public R<PageMaster<ImageCsvListVO>> slidePageList(@RequestBody SlideImagePagerVO req) {
		return R.ok(algorithmPredictionService.slidePageList(req));
	}


	@ApiOperation(value = "查询原始切片列表")
	@PostMapping("/originalSlideList")
	public R<SlidePredictionOut> getOriginalSlideList(@Validated @RequestBody SlidePredictionIn req) {
		SlidePredictionOut out = algorithmPredictionService.getOriginalSlideList(req);
		return R.ok(out);
	}


	@ApiOperation(value = "原始切片设置主图")
	@PostMapping("/setMainImage")
	public R<List<SlidePrediction>> setMainImage(@Validated @RequestBody SetMainImageDataIn req) {
		QueryWrapper<SlidePrediction> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("slide_id", req.getSlideId()).eq("del_flag", "0").eq("main_image", "1");
		//查询是否已经有主图了
		List<SlidePrediction> list = slidePredictionService.list(queryWrapper);
		if(CollectionUtils.isNotEmpty(list)){
			return R.fail(MessageSource.M("SETTING_MAIN_IMAGE_ERROR")); 
		}
		SlidePrediction slidePrediction = new SlidePrediction();
		slidePrediction.setSlidePredictionId(req.getSlidePredictionId());
		//是否是主图默认为2，1是，2否
		slidePrediction.setMainImage("1");
		slidePrediction.setUpdateBy(SecurityUtils.getUserId());
		slidePrediction.setUpdateTime(DateUtil.date());
		slidePredictionService.updateById(slidePrediction);
		//更改校验状态（0通过，1不通过）和提示语（提示语给为null）
		Slide slide= Slide.builder().slideId(req.getSlideId()).eyeMent("0").prompt(null).createBy(SecurityUtils.getUserId()).build();
		slideService.updateMent(slide);
		return R.ok();
	}


	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "启动算法/重算失败数据")
	@PostMapping("/startPrediction")
	public R startPrediction(@Validated @RequestBody StartPredictionIn req) {
		Project project = projectService.getById(req.getProjectId());
		//状态:1待启动，2进行中，3暂停，4已完成
		if(null != project && project.getStatus() == 3 ){
			return R.fail(MessageSource.M("OPERATE_ERROR"));
		}
		R r = algorithmPredictionService.startPrediction(req, project);
		return r;
	}


	/*@ApiOperation(value = "test")
    @PostMapping("/test")
    public R test(@RequestBody PredictionInfoVO predictionInfoVO) {
    	log.info("test:{}",JSONUtil.toJsonStr(predictionInfoVO));
    	slideImageService.uploadImage(predictionInfoVO, SecurityConstants.INNER);
        return R.ok();
    }*/

}

