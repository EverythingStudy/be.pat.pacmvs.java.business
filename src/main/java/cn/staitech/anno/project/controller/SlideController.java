package cn.staitech.anno.project.controller;

import cn.hutool.core.map.MapUtil;
import cn.staitech.anno.project.domain.Opt;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.service.OptService;
import cn.staitech.anno.project.service.SlideService;
import cn.staitech.anno.project.vo.*;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;

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
    private static final Map<String, String> STATUS = MapUtil.builder(new HashMap<String, String>())
            .put("1", "未开始").put("2", "标注中")
            .put("3", "标注完成").put("4", "未复核")
            .put("5", "复核中").put("6", "已复核")
            .put("7", "已交付").build();
    @Resource
    private SlideService slideService;
    @Resource
    private OptService optService;

    @ApiOperation(value = "智能标注-切片分页查询")
    @PostMapping("/page")
    public R<PageMaster<SlideVO>> page(@RequestBody SlideQueryIN in) throws Exception {
        Page page = new Page(in.getPageNum(), in.getPageSize());
        return R.ok(slideService.pageSlides(page,in));
    }

    @ApiOperation(value = "查看标注数目")
    @PostMapping("/getSlideAnnoStatistics")
    public R<List<SlideAnnoStatisticsVO>> getSlideAnnoStatistics(@RequestBody SlideQueryIN in) throws Exception {
        return R.ok(slideService.getSlideAnnoStatistics(in));
    }
    @ApiOperation(value = "标注数据导出")
    @GetMapping("/slideAnnoStatisticsExport")
    public void slideAnnoStatisticsExport(SlideQueryIN in) throws Exception {
        slideService.slideAnnoStatisticsExport(in);
    }

    @ApiOperation(value = "批量修改备注")
    @PostMapping("/updateRemarkBySlideIds")
    public R<Boolean> updateRemarkBySlideIds(@RequestBody SlideRemarkIN in) throws Exception {
        Collection<Slide> slides = slideService.listByIds(in.getSlideIds());
        slides.forEach(slide -> {
            slide.setRemark(in.getRemark());
        });
        Boolean flag = slideService.updateBatchById(slides);
        return R.ok(flag);
    }

    @ApiOperation(value = "批量修改状态")
    @PostMapping("/updateStatusBySlideIds")
    public R<Boolean> updateStatusBySlideIds(@RequestBody SlideStatusIN in) throws Exception {
        Long userId = SecurityUtils.getUserId();
        String userName = SecurityUtils.getUsername();
        List<Opt> optList = new ArrayList<>();
        Collection<Slide> slides = slideService.listByIds(in.getSlideIds());
        slides.forEach(slide -> {
            slide.setStatus(in.getStatus());
            slide.setUpdateBy(userId);
            slide.setUpdateTime(new Date());
            Opt opt = Opt.builder().slideId(slide.getSlideId()).updateBy(userId).updateTime(new Date()).createTime(new Date())
                    .createBy(userId).optCode(in.getStatus()).optName(userName).optCode(STATUS.get(in.getStatus())).build();
            optList.add(opt);
        });
        optService.saveBatch(optList);
        Boolean flag = slideService.updateBatchById(slides);
        return R.ok(flag);
    }

    @ApiOperation(value = "图像状态表查询")
    @PostMapping("/queryStatus")
    public R<List> queryStatus() throws Exception {
        List<Map<String,String>> mapList = new ArrayList<>();
        STATUS.keySet().forEach(k->{
            Map<String,String> map = new HashMap<>();
            map.put("key",k);
            map.put("label",STATUS.get(k));
            mapList.add(map);
        });
        return R.ok(mapList);
    }
}
