package cn.staitech.anno.controller;


import cn.staitech.anno.constant.ProjectConstant;
import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.image.in.ImageListVO;
import cn.staitech.anno.domain.vo.SlideDescriptionVo;
import cn.staitech.anno.domain.vo.image.ProjectStatisticsVo;
import cn.staitech.anno.domain.vo.image.SlideReportSummaryVo;
import cn.staitech.anno.domain.vo.image.SlideReportVo;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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
    private ImageService imageService;

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
                return R.fail("当前切片处理中，禁止删除");
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
            //已被分组的图片不能再次添加分组
            QueryWrapper queryWrapper = Wrappers.query()
                    //.eq("project_id",slide.getProjectId())
                    //.eq("group_id",slide.getGroupId())
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
     * 更改切片描述接口
     */
    @ApiOperation(value = "更改切片描述接口")
    @PostMapping("/updateDescription")
    public R<String> updateDescription(@Validated @RequestBody SlideDescriptionVo req) {
        for (Long id : req.getSlideId()) {
            Slide slide = new Slide();
            slide.setSlideId(id);
            slide.setDescription(req.getDescription());
            slide.setUpdateBy(SecurityUtils.getUserId());
            slideService.updateDescription(slide);
        }
        return R.ok(ResponseConstant.OPERATE_SUCCEED);
    }

    @ApiOperation(value = "查询组内切片报表摘要")
    @PostMapping("/getSlideByProjectAndGroup")
    public R<SlideReportSummaryVo> getSlideByProjectAndGroup(@RequestBody Map params) {
        return slideService.querySlideByProjectAndGroup(params);
    }

    @ApiOperation(value = "组内切片报表分页查询")
    @PostMapping("/pageSlideWithSubImage")
    public R<PageMaster<SlideReportVo>> pageSlideWithSubImage(@RequestBody Map params) {
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
    public R<PageMaster<ProjectStatisticsVo>> pageSlideStatisticsByProject(@RequestBody Map params) {
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
    public R<PageMaster<SlideReportVo>> pageSlideStatistics(@RequestBody Map params) {
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
        return R.ok("操作成功");
    }


    // =======================================================================================================


    /**
     * 通过切片ID查询对应的图像列表 .
     * 原 ProjectController.java  imageList接口 - anno:annotation:image
     */
    @ApiOperation(value = "通过切片ID查询对应的图像（切片）列表")
    @GetMapping("/list")
    public R<List<ImageListVO>> listByProjectId(
            @RequestParam @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail(ProjectConstant.SLIDE_ID_NOT_NULL);
        }
        //获取切片信息
        Slide list = slideService.selectById(slideId);
        if (!Optional.ofNullable(list).isPresent()) {
            return R.fail(ProjectConstant.IMAGE_NOT_EXIST);
        }
        Long projectId = list.getProjectId();
        //根据项目id获取图像信息
        List<ImageListVO> image = imageService.selectImageListByPorjectId(projectId);
        return R.ok(image);
    }


    /**
     * 批量添加切片
     */
    @ApiOperation(value = "通过切片ID查询对应的图像（切片）列表")
    @GetMapping("/add")
    public R add(
            @RequestParam @ApiParam(name = "projectId", value = "项目ID", required = true) Long projectId,
            @RequestParam @ApiParam(name = "topicIds", value = "专题目ID", required = true) List<Long> topicIds) {
        return R.ok(slideService.addAnnoSlidesBatch(projectId, topicIds));
    }

}
