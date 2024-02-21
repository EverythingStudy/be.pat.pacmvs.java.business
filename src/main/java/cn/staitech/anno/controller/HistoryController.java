package cn.staitech.anno.controller;

import cn.staitech.anno.service.HistoryService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 撤消 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "标注编辑-历史记录", notes = "标注编辑-历史记录 - 王峰")
    @Log(title = "标注编辑-历史记录", menu = "标注编辑-历史记录", subMenu = "撤消", businessType = BusinessType.QUERY)
    @GetMapping("/undo")
    public R<String> undo() {
        // slideId、userId、traceId

        // bizType

        return R.ok();
    }

    /**
     * 恢复 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "标注编辑-历史记录", notes = "标注编辑-历史记录 - 王峰")
    @Log(title = "标注编辑-历史记录", menu = "标注编辑-历史记录", subMenu = "恢复", businessType = BusinessType.QUERY)
    @GetMapping("/redo")
    public R<String> redo() {
        return R.ok();
    }


    /**
     * 获取撤消、恢复状态,游标可移动次数 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "标注编辑-历史记录", notes = "标注编辑-历史记录 - 王峰")
    @Log(title = "标注编辑-历史记录", menu = "标注编辑-历史记录", subMenu = "获取撤消、恢复状态,游标可移动次数", businessType = BusinessType.QUERY)
    @GetMapping("/index")
    public R<String> index() {
        return R.ok();
    }


    /**
     * 清空
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "标注编辑-历史记录", notes = "标注编辑-历史记录 - 王峰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID(非必传，但建议传)", required = false, dataType = "Long", paramType = "query")})
    @Log(title = "标注编辑-历史记录", menu = "标注编辑-历史记录", subMenu = "清空", businessType = BusinessType.QUERY)
    @GetMapping("/clean")
    public R<String> clean(@RequestParam @ApiParam(name = "userId", value = "用户ID(非必传，但建议传)", required = false) Long userId) {
        userId = userId > 0 ? userId : SecurityUtils.getLoginUser().getSysUser().getUserId();
        historyService.remove(userId);
        return R.ok();
    }

}
