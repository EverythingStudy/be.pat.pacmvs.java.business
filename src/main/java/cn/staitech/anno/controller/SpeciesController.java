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

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import cn.staitech.anno.domain.Species;
import cn.staitech.anno.service.SpeciesService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.Species.InsertSpeciesVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 轮次
 *
 * @author wangfeng
 * @date 2023/09/10
 */
@Api(value = "种属", tags = "种属")
@RestController
@RequestMapping("/species")
@Slf4j
public class SpeciesController extends BaseController {
    @Resource
    private SpeciesService speciesService;

    /**
     * 轮次列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "种属列表", notes = "种属列表 - 王峰")
    @Log(title = "种属列表", menu = "种属", subMenu = "种属列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<Species>> list() throws ExecutionException, InterruptedException {
        LambdaQueryWrapper<Species> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Species::getOrganizationId, SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        
        List<Species> list = speciesService.list(queryWrapper);
        return R.ok(list, MessageSource.M("OPERATE_SUCCEED"));
    }
    
    @Log(title = "种属增加", businessType = BusinessType.INSERT)
    @ApiOperation(value = "种属增加")
    @PostMapping("/add")
    public R<Species> add(@RequestBody @Validated InsertSpeciesVO req) {
    	R<Species> r = speciesService.add(req);
    	return r;
    }

}
