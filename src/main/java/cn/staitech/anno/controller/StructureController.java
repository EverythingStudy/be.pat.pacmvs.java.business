package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.domain.Structure;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 结构
 *
 * @author wangfeng
 * @date 2023/09/10
 */
@Api(value = "结构", tags = "结构")
@RestController
@RequestMapping("/structure")
@Slf4j
public class StructureController extends BaseController {
    @Resource
    private StructureService structureService;
    @Resource
    private OrganService organService;

    /**
     * 轮次列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "结构列表", notes = "结构列表 - 王峰")
    @Log(title = "结构列表", menu = "结构", subMenu = "结构列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<Structure>> list() throws ExecutionException, InterruptedException {
        LambdaQueryWrapper<Structure> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Structure::getOrganizationId, SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        List<Structure> list = structureService.list(queryWrapper);
        return R.ok(list);
    }

    /**
     * 种属-脏器-结构-列表 .
     */
    @ApiOperationSupport(author = "wanglibei")
    @Log(title = "结构列表", menu = "结构", subMenu = "结构列表", businessType = BusinessType.QUERY)
    @ApiOperation(value = "结构列表new", notes = "结构列表")
    @GetMapping("/getStructureList")
    public R<List<Structure>> getStructureList(@RequestParam(required = true, name = "speciesId") String speciesId,
                                               @RequestParam(required = true, name = "organId") String organId) {
        List<Structure> list = structureService.getStructureList(speciesId, organId);
        return R.ok(list);
    }

    /**
     * 按种属ID查询-脏器列表 .
     *
     * @param speciesId 按种属ID查询
     * @return
     */
    @ApiOperationSupport(author = "wanglibei")
    @ApiOperation(value = "脏器列表", notes = "结构列表")
    @Log(title = "脏器列表", menu = "结构", subMenu = "脏器列表", businessType = BusinessType.QUERY)
    @GetMapping("/getOrganByspeciesId")
    public R<List<Organ>> getOrganByspeciesId(@RequestParam(required = true, name = "speciesId") String speciesId) {
        List<Organ> list = organService.getOrganBySpeciesId(speciesId);
        return R.ok(list);
    }

}
