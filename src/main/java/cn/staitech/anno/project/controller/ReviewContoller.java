package cn.staitech.anno.project.controller;

import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.service.ReviewService;
import cn.staitech.anno.project.vo.ReviewIN;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description 评审
 * @date 2023/9/15 13:06:04
 */
@Slf4j
@Api(value = "项目接口", tags = "项目接口")
@RestController("review")
@Validated
@RestControllerAdvice
@RequestMapping("/intelligentEvaluation/review")
public class ReviewContoller {

    @Resource
    private ReviewService reviewService;

    @ApiOperation(value = "新增评审")
    @PostMapping("/addReview")
    public R<Boolean> addReview(@RequestBody List<ReviewIN> reviews){

        return R.ok();
    }

    @ApiOperation(value = "按切片id查询评审列表")
    @GetMapping("/queryReview")
    public R<List<Review>> queryReview(@RequestParam("slideId") @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId){
        return R.ok(reviewService.list(Wrappers.query(Review.builder().slideId(slideId).build())));
    }
}
