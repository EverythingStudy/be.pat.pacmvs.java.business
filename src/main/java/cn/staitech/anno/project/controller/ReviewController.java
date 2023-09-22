package cn.staitech.anno.project.controller;

import cn.hutool.core.io.IoUtil;
import cn.staitech.anno.constant.ExportConstant;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.reviewround.ReviewRoundOutVO;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.project.domain.DownTask;
import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.service.DownTaskService;
import cn.staitech.anno.project.service.ReviewService;
import cn.staitech.anno.project.service.SlideService;
import cn.staitech.anno.project.vo.*;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description 评审
 * @date 2023/9/15 13:06:04
 */
@Slf4j
@Api(value = "评审接口", tags = "项目评审接口")
@RestController("review")
@Validated
@RestControllerAdvice
@RequestMapping("/intelligentEvaluation/review")
public class ReviewController {

    @Resource
    private ReviewService reviewService;
    @Resource
    private DownTaskService downTaskService;
    @Resource
    private SlideMapper slideMapper;
    @Resource
    private SlideService slideService;

    @ApiOperation(value = "viewer新增评审")
    @PostMapping("/insertReview")
    public R<String> insertReview(@Validated @RequestBody ReviewIN reviews) throws Exception {
        reviewService.insert(reviews);
        return R.ok("操作成功");
    }

    @ApiOperation(value = "viewer编辑评审")
    @PutMapping("/updateReview")
    public R<String> updateReview(@Validated @RequestBody ReviewUP reviews) throws Exception {
        reviewService.update(reviews);
        return R.ok("操作成功");
    }

    @ApiOperation(value = "viewer按切片id查询评审列表")
    @GetMapping("/queryReview")
    public R<List<Review>> queryReview(@RequestParam("slideId") @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId){
        return R.ok(reviewService.list(Wrappers.query(Review.builder().slideId(slideId).build())));
    }
    @ApiOperation(value = "评审数据导出")
    @PostMapping("/exportReview")
    public R<DownTask> exportReview(@RequestBody DownTaskIN in) throws Exception{
        return R.ok(reviewService.csvExportReview(in.getProjectId(),in.getSlideIds()));
    }

    @ApiOperation(value = "下载任务状态查询")
    @GetMapping("/queryDownTaskByCode")
    public R<DownTask> queryDownTaskByCode(@RequestParam("code") @ApiParam(name = "code", value = "下载任务编码", required = true) String code)throws Exception{
        return R.ok(downTaskService.getOne(Wrappers.query(DownTask.builder().code(code).build())));
    }

    @ApiOperation(value = "下载目录文件")
    @GetMapping("/downTaskByCode")
    public void downTaskByCode(@RequestParam("code") @ApiParam(name = "code", value = "下载任务编码", required = true) String code, HttpServletResponse httpServletResponse)throws Exception{
        DownTask downTask = downTaskService.getOne(Wrappers.query(DownTask.builder().code(code).build()));
        //httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
        httpServletResponse.setContentType("text/plain;charset=utf-8");
        //name是下载对话框的名称，不支持中文，想用中文名称需要进行utf8编码
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downTask.getProjectName(), "utf-8") +".txt");
        ServletOutputStream out = null;
        out = httpServletResponse.getOutputStream();
        // 查询切片列表
        QueryWrapper<Slide> slideQueryWrapper = new QueryWrapper<>();
        slideQueryWrapper.eq("project_id", downTask.getProjectId());
        List<Slide> slideList = slideMapper.selectList(slideQueryWrapper);
        for(Slide slide:slideList){
            Map<String,String> pathMap = (Map<String, String>) downTask.getPath().get(slide.getSlideId().toString());
            if(pathMap  != null){
                String p = pathMap.get(ExportConstant.PATH);
                out.write((p+"\n").getBytes());
            }
        }
        IoUtil.close(out);
    }

    @ApiOperation(value = "智能评审-查询评审轮次列表")
    @GetMapping("/pageReviewRound")
    public R<PageMaster<ReviewRoundOutVO>> pageReviewRound(@NotNull(message = "分页参数为空！") @RequestParam("pageNum") @ApiParam(name = "pageNum", value = "分页参数", required = true) Integer pageNum,
                                                @NotNull(message = "分页参数为空！") @RequestParam("pageSize") @ApiParam(name = "pageSize", value = "分页参数", required = true) Integer pageSize,
                                                ReviewRoundIN params) {
        Page page = new Page(pageNum,pageSize);
        return R.ok(reviewService.pageReviewRound(page, params));
    }

    @ApiOperation(value = "智能标注-切片分页查询")
    @GetMapping("/page")
    public R<PageMaster<ReviewSlideVO>> pageReviewSlide(@NotNull(message = "分页参数为空！") @RequestParam("pageNum") @ApiParam(name = "pageNum", value = "分页参数", required = true) Integer pageNum,
                                                        @NotNull(message = "分页参数为空！") @RequestParam("pageSize") @ApiParam(name = "pageSize", value = "分页参数", required = true) Integer pageSize,
                                                        ReviewSlideIN in){
        Page page = new Page(pageNum,pageSize);
        return R.ok(slideService.pageReviewSlide(page,in));
    }
}
