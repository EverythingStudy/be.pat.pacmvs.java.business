package cn.staitech.anno.controller;


import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.SlidePrediction;
import cn.staitech.anno.service.AlgorithmPredictionService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.service.SlidePredictionService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.imagecsv.ImageCsvListVO;
import cn.staitech.anno.vo.predictionInfo.in.SetMainImageDataIn;
import cn.staitech.anno.vo.predictionInfo.in.SlideImagePagerVO;
import cn.staitech.anno.vo.predictionInfo.in.SlidePredictionIn;
import cn.staitech.anno.vo.predictionInfo.in.StartPredictionIn;
import cn.staitech.anno.vo.predictionInfo.out.SlidePredictionOut;
import cn.staitech.anno.vo.project.ProjectListVO;
import cn.staitech.anno.vo.project.in.ProjectListQueryIn;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    private SlidePredictionService slidePredictionService;

    @Resource
    private SlideService slideService;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiOperation(value = "算法项目列表分页查询")
    @RequiresPermissions("algorithmDetectionInfo:list")
    @PostMapping("/projectList")
    public R<PageMaster<List<ProjectListVO>>> getProjectList(@RequestBody @Validated ProjectListQueryIn req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);
        Project project = new Project();
        BeanUtils.copyProperties(req, project);
        Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        project.setOrganizationId(organizationId);
        //判断当前角色是否为admin或者超级管理员
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
//            project.setCreateBy(SecurityUtils.getUserId());
        	project.setPartUserId(SecurityUtils.getUserId());
        }
        //项目列表来源于项目管理模块的项目类型为算法检测和图像拼接的项目,除了系统管理员外，每个用户只能看到自己参与的项目
        //项目类型:1标注2评审3标准训练集  6图像拼接  7算法预测
        Integer[] projectArray = {6, 7};
        project.setProjectTypeArray(projectArray);
        List<ProjectListVO> list = projectService.selectProjectList(project);
        PageMaster pageMaster = new PageMaster<>(list);
        return R.ok(pageMaster);
    }


    @ApiOperation(value = "查询算法项目切片列表")
    @RequiresPermissions("algorithmDetectionInfo:slice:list")
    @PostMapping("/slidePageList")
    public R<PageMaster<ImageCsvListVO>> slidePageList(@RequestBody SlideImagePagerVO req) {
        return R.ok(algorithmPredictionService.slidePageList(req));
    }


    @ApiOperation(value = "查询原始切片列表")
    @RequiresPermissions("algorithmDetectionInfo:slice:checkSlicesDetail")
    @PostMapping("/originalSlideList")
    public R<SlidePredictionOut> getOriginalSlideList(@Validated @RequestBody SlidePredictionIn req) {
        SlidePredictionOut out = algorithmPredictionService.getOriginalSlideList(req);
        return R.ok(out);
    }


    @ApiOperation(value = "原始切片设置主图")
    @RequiresPermissions("projectConfig:spliceImgConfig:setMainImg")
    @PostMapping("/setMainImage")
    public R<List<SlidePrediction>> setMainImage(@Validated @RequestBody SetMainImageDataIn req) {
        Slide selectFolderMent = slideService.selectFolderMent(req.getSlideId());
        QueryWrapper<SlidePrediction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("slide_id", req.getSlideId()).eq("del_flag", "0").eq("main_image", "1");
        //查询是否已经有主图了
        List<SlidePrediction> list = slidePredictionService.list(queryWrapper);
        SlidePrediction slidePrediction = new SlidePrediction();
        slidePrediction.setSlidePredictionId(req.getSlidePredictionId());
        //是否是主图默认为2，1是，2否
        //改为主图
        slidePrediction.setMainImage("1");
        slidePrediction.setUpdateBy(SecurityUtils.getUserId());
        slidePrediction.setUpdateTime(DateUtil.date());
        slidePredictionService.updateById(slidePrediction);
        //改为非主图
        if (CollectionUtils.isNotEmpty(list)){
            slidePrediction.setSlidePredictionId(list.get(0).getSlidePredictionId());
            slidePrediction.setMainImage("2");
            slidePredictionService.updateById(slidePrediction);
        }
        if (Objects.equals(selectFolderMent.getEyeMent(), "1") && Objects.equals(selectFolderMent.getPrompt(), "2")) {
            //更改校验状态（0通过，1不通过）和提示语（提示语给为null）
            Slide slide = Slide.builder().slideId(req.getSlideId()).eyeMent("0").prompt(null).createBy(SecurityUtils.getUserId()).build();
            slideService.updateMent(slide);
        }
        return R.ok();
    }


    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "启动算法/重算失败数据")
    @RequiresPermissions(value = {"algorithmDetectionInfo:slice:startAlgorithm", "algorithmDetectionInfo:slice:reStartErrorData"}, logical = Logical.OR)
    @PostMapping("/startPrediction")
    public R startPrediction(@Validated @RequestBody StartPredictionIn req) {
        Project project = projectService.getById(req.getProjectId());
        //状态:1待启动，2进行中，3暂停，4已完成
        if (null != project && project.getStatus() == 3) {
            return R.fail(MessageSource.M("OPERATE_ERROR"));
        }
        R r = algorithmPredictionService.startPrediction(req, project);
        return r;
    }
}
