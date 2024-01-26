package cn.staitech.anno.project.controller;

import cn.staitech.anno.project.constants.Constants;
import cn.staitech.anno.project.domain.Opt;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.service.OptService;
import cn.staitech.anno.project.service.SlideService;
import cn.staitech.anno.project.vo.*;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mugw
 * @version 1.0
 * @description 切片
 * @date 2023/9/14 09:02:35
 */
@Slf4j
@Api(value = "切片操作记录", tags = "切片操作记录")
@RestController("SlideControllerV1")
@Validated
@RestControllerAdvice
@RequestMapping("/intelligentAnno/slide_v1")
public class SlideController {
    @Resource
    private SlideService slideService;
    @Resource
    private OptService optService;

    @RequiresPermissions(value = {"smartAnno:project:slice", "smartAnnoInfo:slice"}, logical = Logical.OR)
    @ApiOperation(value = "智能标注-切片分页查询")
    @PostMapping("/page")
    public R<PageMaster<SlideVO>> page(@RequestBody SlideQueryIn in) throws Exception {
        Page page = new Page(in.getPageNum(), in.getPageSize());
        return R.ok(slideService.pageSlides(page, in));
    }

    @RequiresPermissions("smartAnno:project:slice:check")
    @ApiOperation(value = "查看标注数目")
    @PostMapping("/getSlideAnnoStatistics")
    public R<List<SlideAnnoStatisticsVO>> getSlideAnnoStatistics(@RequestBody SlideQueryIn in) throws Exception {
        return R.ok(slideService.getSlideAnnoStatistics(in));
    }

    @ApiOperation(value = "多用户统计")
    @PostMapping("/projectUserStatistics")
    public R<List<ProjectStatisticsOut>> projectUserStatistics(@RequestBody ProjectStatisticsIn in) throws Exception {
        return R.ok(slideService.projectUserStatistics(in));
    }

    @RequiresPermissions("smartAnno:project:slice:export")
    @ApiOperation(value = "标注数据导出")
    @GetMapping("/slideAnnoStatisticsExport")
    public void slideAnnoStatisticsExport(SlideQueryIn in) throws Exception {
        slideService.slideAnnoStatisticsExport(in);
    }

    @RequiresPermissions("smartAnno:project:slice:remarkList")
    @ApiOperation(value = "批量修改备注")
    @PostMapping("/updateRemarkBySlideIds")
    public R<Boolean> updateRemarkBySlideIds(@RequestBody SlideRemarkIn in) {
        Collection<Slide> slides = slideService.listByIds(in.getSlideIds());
        slides.forEach(slide -> {
            slide.setRemark(in.getRemark());
        });
        Boolean flag = slideService.updateBatchById(slides);
        return R.ok(flag);
    }

    @RequiresPermissions("smartAnno:project:slice:editList")
    @ApiOperation(value = "批量修改状态")
    @PostMapping("/updateStatusBySlideIds")
    public R<Boolean> updateStatusBySlideIds(@RequestBody SlideStatusIn in) {
        Long userId = SecurityUtils.getUserId();
        String userName = SecurityUtils.getUsername();
        List<Opt> optList = new ArrayList<>();
        Collection<Slide> slides = slideService.listByIds(in.getSlideIds());
        slides.forEach(slide -> {
            slide.setStatus(in.getStatus());
            slide.setUpdateBy(userId);
            slide.setUpdateTime(new Date());

            Opt opt = null;
            if (LanguageUtils.isEn()) {
                opt = Opt.builder().slideId(slide.getSlideId()).updateBy(userId).updateTime(new Date()).createTime(new Date())
                        .createBy(userId).optCode(in.getStatus()).optName(userName).optCode(Constants.STATUS_EN.get(in.getStatus())).build();
                optList.add(opt);
            } else {
                opt = Opt.builder().slideId(slide.getSlideId()).updateBy(userId).updateTime(new Date()).createTime(new Date())
                        .createBy(userId).optCode(in.getStatus()).optName(userName).optCode(Constants.STATUS.get(in.getStatus())).build();

            }
            optList.add(opt);
        });
        optService.saveBatch(optList);
        Boolean flag = slideService.updateBatchById(slides);
        return R.ok(flag);
    }

    @ApiOperation(value = "图像状态表查询")
    @PostMapping("/queryStatus")
    public R<List> queryStatus() {
        List<Map<String, String>> mapList = new ArrayList<>();
        if (LanguageUtils.isEn()) {
            Constants.STATUS_EN.keySet().forEach(k -> {
                Map<String, String> map = new HashMap<>(16);
                map.put("key", k);
                map.put("label", Constants.STATUS_EN.get(k));
                mapList.add(map);
            });
        } else {
            Constants.STATUS.keySet().forEach(k -> {
                Map<String, String> map = new HashMap<>(16);
                map.put("key", k);
                map.put("label", Constants.STATUS.get(k));
                mapList.add(map);
            });
        }
        return R.ok(mapList);
    }
}
