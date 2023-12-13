package cn.staitech.anno.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.vo.organ.InsertOrganVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

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
        LambdaQueryWrapper<Organ> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organ::getOrganizationId, SecurityUtils.getLoginUser().getSysUser().getOrganizationId());

        List<Organ> list = organService.list(queryWrapper);
        return R.ok(list);
    }
    
    
    @Log(title = "脏器增加", businessType = BusinessType.INSERT)
    @ApiOperation(value = "脏器增加")
    @PostMapping("/add")
    public R<Organ> add(@RequestBody @Validated InsertOrganVO req) {
    	R<Organ> r = organService.add(req);
    	return r;
    }
}
