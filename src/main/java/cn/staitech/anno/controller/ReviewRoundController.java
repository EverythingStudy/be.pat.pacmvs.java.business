package cn.staitech.anno.controller;

import cn.staitech.anno.domain.ReviewRound;
import cn.staitech.anno.domain.reviewround.ReviewRoundBatchInVO;
import cn.staitech.anno.domain.reviewround.ReviewRoundInVO;
import cn.staitech.anno.domain.reviewround.ReviewRoundOutVO;
import cn.staitech.anno.service.ReviewRoundService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-18 17:15:58
 * @Description: 评审轮次
 */
@Api(value = "评审轮次", tags = "评审轮次")
@RestController
@RequestMapping("/reviewRound")
@Slf4j
public class ReviewRoundController {

    @Resource
    private ReviewRoundService reviewRoundService;


    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "查询评审轮次列表")
    @GetMapping("/list")
    public R<PageMaster<ReviewRoundOutVO>> list(@NotNull(message = "分页参数为空！") @RequestParam("pageNum") @ApiParam(name = "pageNum", value = "分页参数", required = true) Integer pageNum,
                                                      @NotNull(message = "分页参数为空！") @RequestParam("pageSize") @ApiParam(name = "pageSize", value = "分页参数", required = true) Integer pageSize,
                                                      @RequestParam("projectId") Long projectId) {
        return R.ok(reviewRoundService.pageReviewRound(pageNum, pageSize, projectId));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "添加评审轮次")
    @PostMapping("/add")
    public R add(@RequestBody ReviewRoundBatchInVO reviewRoundBatchInVO) {
        return R.ok(reviewRoundService.saveBatchByList(reviewRoundBatchInVO));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "批量删除评审轮次")
    @GetMapping(value = "/remove")
    public R remove(@RequestParam("reviewRoundIds") List<Long> reviewRoundIds) {
        return R.ok(reviewRoundService.removeByIds(reviewRoundIds));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "修改评审轮次")
    @PostMapping("/edit")
    public R edit(@RequestBody ReviewRoundInVO reviewRoundInVO) {
        ReviewRound reviewRound = new ReviewRound();
        BeanUtils.copyProperties(reviewRoundInVO, reviewRound);
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        reviewRound.setCreateBy(sysUser.getUserId());
        reviewRound.setOrganizationId(sysUser.getOrganizationId());
        return R.ok(reviewRoundService.updateById(reviewRound));
    }
}
