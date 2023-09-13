package cn.staitech.anno.controller;

import cn.staitech.anno.domain.organ.Organ;
import cn.staitech.anno.service.OrganService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(value = "脏器", tags = "脏器")
@RestController
@RequestMapping("/organ")
@Slf4j
public class OrganController extends BaseController {
    @Resource
    private OrganService organService;

    /**
     * 脏器列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "脏器列表", notes = "脏器列表 - 王峰")
    @Log(title = "查询脏器列表", menu = "脏器", subMenu = "脏器", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<Organ>> list() throws ExecutionException, InterruptedException {
        List<Organ> list = organService.list();
        return R.ok(list);
    }

}
