package cn.staitech.anno.controller;

import cn.staitech.anno.domain.round.Round;
import cn.staitech.anno.service.RoundService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 轮次
 *
 * @author wangfeng
 * @date 2023/09/10
 */
@Api(value = "轮次", tags = "轮次")
@RestController
@RequestMapping("/round")
@Slf4j
public class RoundController extends BaseController {
    @Resource
    private RoundService roundService;

    /**
     * 轮次列表 .
     */
    // @RequiresPermissions("anno:round:list")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "轮次列表", notes = "轮次列表 - 王峰")
    @Log(title = "查询轮次列表", menu = "图片管理", subMenu = "轮次列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<Round>> list() throws ExecutionException, InterruptedException {
        List<Round> list = roundService.list();
        return R.ok(list);
    }

}
