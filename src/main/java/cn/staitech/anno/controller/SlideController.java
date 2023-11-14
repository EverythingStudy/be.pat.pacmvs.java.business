package cn.staitech.anno.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.service.SysUserService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.eyeslide.*;
import cn.staitech.anno.vo.image.out.ImageListOutVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvGetPagerVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvGetVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvListVO;
import cn.staitech.anno.vo.project.ProjectStatisticsVO;
import cn.staitech.anno.vo.slide.*;
import cn.staitech.anno.vo.topic.GetTopicListVO;
import cn.staitech.anno.vo.topic.TopicIdName;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 图像 信息操作处理
 *
 * @author staitech
 */
@Slf4j
@Api(value = "切片管理接口", tags = "切片管理接口")
@RestController
@RequestMapping("/slide")
public class SlideController extends BaseController {
    @Resource
    private SlideService slideService;
    @Resource
    private SysUserService sysUserService;

    @RequiresPermissions("special:project:slicelist")
    @ApiOperation(value = "查询切片操作")
    @PostMapping("/getSlideByPage")
    public R<PageMaster<Slide>> getSlideByPage(@RequestBody Map params) {
        try {
            Page<Slide> page = new Page<>(MapUtils.getInteger(params, "pageNum", 1), MapUtils.getInteger(params, "pageSize", 10));
            slideService.page(page);
            //构建分页对象
            PageMaster<Slide> pageMaster = PageMaster.of(page.getRecords());
            pageMaster.setTotal(page.getTotal());
            return R.ok(pageMaster);
        } catch (Exception e) {
            logger.error("查询切片操作异常：{}", e.getMessage());
            return R.fail(e.getMessage());
        }
    }


    /**
     * 批量删除组内切片
     */
    @RequiresPermissions("special:project:batchdel")
    @Log(title = "项目配置批量删除", menu = "专题管理", subMenu = "项目配置", businessType = BusinessType.DELETE)
    @ApiOperation(value = "批量删除组内切片")
    @PostMapping("/updateBatchByCondition")
    public R updateBatchByCondition(@RequestBody List<Slide> slideList) {
        try {
            List<Slide> list = new ArrayList<>();
            //process_flag状态处理
            for (Slide slide : slideList) {
                QueryWrapper queryWrapper = Wrappers.query()
                        .eq("process_flag", 0)
                        .eq("is_delete", 0)
                        .eq("group_id", slide.getGroupId())
                        .eq("image_id", slide.getImageId())
                        .eq("project_id", slide.getProjectId());
                Slide s = slideService.getOne(queryWrapper);
                if (s != null) {
                    list.add(slide);
                }
            }
            //需求要求，删除单条提示
            if (slideList.size() == 1 && list.size() == 0) {
                return R.fail(MessageSource.M("DELETE_FAILURE_SLIDE_USEING"));
            }
            if (!list.isEmpty()) {
                slideService.updateBatchByCondition(list);
            }
            return R.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    /**
     * 添加组内切片
     */
    @Log(title = "项目配置选择切片", menu = "专题管理", subMenu = "项目配置", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "添加组内切片")
    @PostMapping("/saveBatch")
    public R saveBatch(@RequestBody List<Slide> slideList) {
        List<Slide> list = new ArrayList<>();
        for (Slide slide : slideList) {
            // 已被分组的图片不能再次添加分组
            QueryWrapper queryWrapper = Wrappers.query()
                    .eq("is_delete", 0)
                    .eq("image_id", slide.getImageId());
            Slide s = slideService.getOne(queryWrapper);
            if (s == null || s.getIsDelete().equals(1)) {
                list.add(slide);
            }
        }
        boolean flag = slideService.saveBatch(list);
        if (flag) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 更改切片描述/备注接口
     */
    @ApiOperation(value = "更改切片描述/备注接口")
    @PostMapping("/updateDescription")
    public R<String> updateDescription(@Validated @RequestBody SlideDescriptionVO req) {
        for (Long id : req.getSlideId()) {
            Slide slide = new Slide();
            slide.setSlideId(id);
            slide.setDescription(req.getDescription());
            slide.setUpdateBy(SecurityUtils.getUserId());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(req.getRemark())) {
                slide.setRemark(req.getRemark());
            }
            slideService.updateDescription(slide);
        }
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperation(value = "查询组内切片报表摘要")
    @PostMapping("/getSlideByProjectAndGroup")
    public R<SlideReportSummaryVO> getSlideByProjectAndGroup(@RequestBody Map params) {
        return slideService.querySlideByProjectAndGroup(params);
    }

    @ApiOperation(value = "组内切片报表分页查询")
    @PostMapping("/pageSlideWithSubImage")
    public R<PageMaster<SlideReportVO>> pageSlideWithSubImage(@RequestBody Map params) {
        return slideService.pageSlideWithSubImage(params);
    }

    /**
     * 项目统计列表
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "项目内切片统计")
    @PostMapping("/pageSlideStatisticsByProject")
    public R<PageMaster<ProjectStatisticsVO>> pageSlideStatisticsByProject(@RequestBody Map params) {
        return slideService.pageSlideStatisticsByProject(params);
    }

    /**
     * 切片统计列表
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "切片统计报表分页查询")
    @PostMapping("/pageSlideStatistics")
    public R<PageMaster<SlideReportVO>> pageSlideStatistics(@RequestBody Map params) {
        return slideService.pageSlideStatistics(params);
    }

    @ApiOperation(value = "切片导出json")
    @GetMapping("/jsonExport")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "slideList", value = "切片id", dataTypeClass = List.class, paramType = "query", example = "1")})
    public R<String> jsonExport(
            @RequestParam("slideList") List<Long> slideList,
            @RequestParam(name = "status") @ApiParam(name = "status", value = "状态(1:本地导出,2:获取文件路径)") Integer status,
            @RequestParam(name = "projectId") @ApiParam(name = "projectId", value = "项目id") Long projectId
    ) throws Exception {
        slideService.jsonExport(slideList, projectId, status);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    /**
     * 查询某个项目或者review_round_id对应的已经绑定的topic
     */
    @ApiOperation(value = "查询某个项目或者review_round_id对应的已经绑定的topic列表")
    @PostMapping("/topicList")
    public R<List<TopicIdName>> topicList(@RequestBody GetTopicListVO getTopicListVO) {
        Slide slide = new Slide();
        QueryWrapper<Slide> queryWrapper = new QueryWrapper<>(slide);
        queryWrapper.select("distinct topic_id", "topic_name");
        queryWrapper.eq("project_id", getTopicListVO.getProjectId());
        if (getTopicListVO.getReviewRoundId() != null && getTopicListVO.getReviewRoundId() > 0) {
            queryWrapper.eq("review_round_id", getTopicListVO.getReviewRoundId());
        }
        queryWrapper.isNotNull("topic_id");
        queryWrapper.orderByDesc("topic_id");

        List<Slide> list = slideService.list(queryWrapper);
        List<TopicIdName> topicList = new ArrayList<>(list.size());
        for (Slide slideObj : list) {
            TopicIdName topic = new TopicIdName();
            BeanUtil.copyProperties(slideObj, topic);
            topicList.add(topic);
        }

        return R.ok(topicList);
    }


    /**
     * view查询切片列表-不分页
     */
    @ApiOperation(value = "查询标注项目切片列表")
    @PostMapping("/viewList")
    public R<List<ImageCsvListVO>> viewList(@RequestBody ImageCsvGetVO imageCsvGetVO) {
        return R.ok(slideService.pageSlides1(imageCsvGetVO));
    }


    /**
     * 查询切片列表-不分页
     */
    @ApiOperation(value = "查询标注项目切片列表")
    @PostMapping("/list")
    public R<List<ImageCsvListVO>> slidelist(@RequestBody ImageCsvGetVO imageCsvGetVO) {
        return R.ok(slideService.pageSlides(imageCsvGetVO));
    }


    /**
     * 查询切片列表-分页
     */
    @ApiOperation(value = "查询评审项目切片列表")
    @PostMapping("/pageList")
    public R<PageMaster<ImageCsvListVO>> pagelidelist(@RequestBody ImageCsvGetPagerVO imageCsvGetPagerVO) {
        return R.ok(slideService.pageReviewRoundSSlides(imageCsvGetPagerVO));
    }


    /**
     * 批量添加标注切片（旧-弃用）
     */
    @ApiOperation(value = "批量添加标注切片")
    @PostMapping("/addAnnoSlidesBatch")
    public R addAnnoSlidesBatch(@RequestBody AddSlideVO req) {
        // 先清空
        removeAll(req.getProjectId(), req.getReviewRoundId());
        // 再新增
        return R.ok(slideService.addAnnoSlidesBatch(req));
    }

    /**
     * 批量添加切片(新)
     */
    @ApiOperation(value = "批量添加标注切片")
    @PostMapping("/addSlidesBatch")
    public R addSlidesBatch(@RequestBody AddSlideIdsVO req) {
        return R.ok(slideService.addSlidesBatch(req));
    }

    /**
     * 根据ID批量删除切片
     *
     * @param request
     * @return
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "删除切片", menu = "切片管理", subMenu = "标注切片", businessType = BusinessType.DELETE)
    @ApiOperation(value = "逻辑批量删除切片")
    @PostMapping("/deleteBatchIds")
    public R deleteBatchIds(@RequestBody DelSlideIdsVO request) {
        // delSlidesBatchzr返回未删除个数
        if (slideService.delSlidesBatch(request.getSlideIds()) > 0) {
            return R.fail(MessageSource.M("IMAGE_ANNO_USING_FORBID_DELETE"));
        }
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    /**
     * 根据查询条件全部删除
     *
     * @param req
     * @return
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "删除切片", menu = "切片管理", subMenu = "标注切片", businessType = BusinessType.DELETE)
    @ApiOperation(value = "逻辑批量删除切片")
    @PostMapping("/deleteAll")
    public R deleteAll(@RequestBody DelSlideVO req) {
        if (removeAll(req.getProjectId(), req.getReviewRoundId())) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }

    /**
     * 清空
     *
     * @param projectId
     * @param reviewRoundId
     * @return
     */
    private boolean removeAll(Long projectId, Long reviewRoundId) {
        Slide slide = new Slide();
        slide.setProjectId(projectId);
        slide.setReviewRoundId(reviewRoundId);
        QueryWrapper<Slide> queryWrapper = new QueryWrapper<>(slide);

        if (slideService.remove(queryWrapper)) {
            return true;
        }
        return false;
    }

    /**
     * 上传人员
     */
    @ApiOperation(value = "拼接图象类项目---上传人员")
    @ApiOperationSupport(author = "zmj")
    @PostMapping("/uploadPersonnel")
    public R<List<SysUser>> uploadPersonnel() {
        List<SysUser> userList = sysUserService.userList();
        return R.ok(userList);
    }

    /**
     * 获取图片碎片
     */
    @ApiOperation(value = "拼接图象类项目---切片列表")
    @ApiOperationSupport(author = "zmj")
    @PostMapping("/folderList")
    public R<PageMaster<ImageListOutVO>> folderList(@RequestBody @Validated EyeSlideIn eyeSlideIn) {
        if (eyeSlideIn.getFolderName()==null && eyeSlideIn.getTopicName()==null && eyeSlideIn.getParams()==null && eyeSlideIn.getCreateBy()==null){
            return R.ok();
        }
        PageMaster<ImageListOutVO> eyeImage = slideService.eyeImage(eyeSlideIn);
        return R.ok(eyeImage);
    }

    /**
     * 获取拼接图象类项目---项目图片
     */
    @ApiOperation(value = "拼接图象类项目---项目图片")
    @ApiOperationSupport(author = "zmj")
    @PostMapping("/projectPictureList")
    public R<PageMaster<EyeProjectSlideOut>> projectPictureList(@RequestBody @Validated EyeProjectSlideIn eyeProjectSlideIn) {
        PageMaster<EyeProjectSlideOut> eyeProjectSlide = slideService.eyeProjectSlide(eyeProjectSlideIn);
        return R.ok(eyeProjectSlide);
    }

    /**
     * 获取拼接图象类项目---项目图片删除
     */
    @ApiOperation(value = "拼接图象类项目---项目图片删除")
    @ApiOperationSupport(author = "zmj")
    @PostMapping("/projectPictureDel")
    public R projectPictureDel(@RequestBody @Validated ProjectSlideDel projectSlideDel) {
        return slideService.deleteProjectImage(projectSlideDel);
    }


    /**
     * 保存文件夹和切片
     */
    @ApiOperation(value = "拼接图象类项目---保存文件夹和切片")
    @ApiOperationSupport(author = "zmj")
    @PostMapping("/eyeSave")
    public R eyeSave(@RequestBody @Validated EyeSaveSlide eyeSaveSlide) {
        return slideService.eyeFolder(eyeSaveSlide);
    }

    /**
     * 拼接图象类项目---是否有算法结果
     */
    @ApiOperation(value = "拼接图象类项目---是否有算法结果")
    @ApiOperationSupport(author = "zmj")
    @GetMapping("/algorithmResult")
    public R<Integer> algorithmResult(@RequestParam(name = "projectId") @ApiParam(name = "projectId", value = "项目id") Long projectId) {
        Integer num = slideService.algorithmResult(projectId);
        return R.ok(num);
    }


    /**
     * 拼接图象类项目---查询错误原因
     */
    @ApiOperation(value = "拼接图象类项目---查询错误原因")
    @ApiOperationSupport(author = "zmj")
    @GetMapping("/errorReason")
    public R<EyeErrorReasonOut> errorReason(@RequestParam(name = "slideId") @ApiParam(name = "slideId", value = "切片id") Long slideId) {
        EyeErrorReasonOut errorReason = slideService.errorReason(slideId);
        return R.ok(errorReason);
    }


    /**
     * 拼接图像-单个切片详细信息 .
     * 宽高、文件名、缩略图、宽高是 最大画布的:拼图的三倍宽高
     */
    @SneakyThrows
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "拼接图像单个切片", notes = "拼接图像单个切片 - 王峰")
    @GetMapping("/airepost/{slideId}")
    public R<SlideAirepostVO> selectById(@PathVariable("slideId") @ApiParam(value = "切片ID") Long slideId) {
        return R.ok(slideService.selectSlideAirepostVOById(slideId));
    }

}

