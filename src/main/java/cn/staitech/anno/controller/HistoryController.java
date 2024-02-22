package cn.staitech.anno.controller;

import cn.staitech.anno.service.HistoryService;
import cn.staitech.anno.vo.history.Cursor;
import cn.staitech.anno.vo.history.HistoryDTO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: wangfeng
 * @create: 2024-2-20 18:07:38
 * @Description: 颜色
 */
@Api(value = "标注编辑-历史记录", tags = "标注编辑-历史记录")
@RestController
@RequestMapping("/history")
@Slf4j
public class HistoryController {

    @Resource
    HistoryService historyService;

    /**
     * 撤消、恢复 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "标注编辑-历史记录", notes = "标注编辑-历史记录 - 王峰")
    @Log(title = "标注编辑-历史记录", menu = "标注编辑-历史记录", subMenu = "撤消、恢复", businessType = BusinessType.QUERY)
    @PostMapping("/process")
    public R<String> process(@Validated @RequestBody HistoryDTO dto) {
        historyService.process(dto);
        return R.ok();
    }

    /**
     * 获取撤消、恢复状态 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "标注编辑-历史记录", notes = "标注编辑-历史记录 - 王峰")
    @Log(title = "标注编辑-历史记录", menu = "标注编辑-历史记录", subMenu = "获取撤消、恢复状态,游标可移动次数", businessType = BusinessType.QUERY)
    @PostMapping("/index")
    public R<Cursor> index(@Validated @RequestBody HistoryDTO dto) {
        if (dto.getUserId() == null) {
            dto.setUserId(SecurityUtils.getLoginUser().getSysUser().getUserId());
        }
        return R.ok(historyService.getCursor(dto));
    }

    /**
     * 清空
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "标注编辑-历史记录", notes = "标注编辑-历史记录 - 王峰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID(非必传，但建议传)", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "slideId", value = "切片ID(非必传，但建议传)", required = false, dataType = "Long", paramType = "query")})
    @Log(title = "标注编辑-历史记录", menu = "标注编辑-历史记录", subMenu = "清空", businessType = BusinessType.QUERY)
    @GetMapping("/clean")
    public R<String> clean(@RequestParam @ApiParam(name = "userId", value = "用户ID(非必传，但建议传)", required = false) Long userId,
                           @RequestParam @ApiParam(name = "slideId", value = "切片ID(非必传，但建议传)", required = false) Long slideId) {

        System.out.println("slideId = " + slideId);

        if (userId == null) {
            userId = SecurityUtils.getLoginUser().getSysUser().getUserId();
        }

        historyService.clearSessionList(userId);
        return R.ok();
    }
}
