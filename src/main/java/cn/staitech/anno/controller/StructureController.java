package cn.staitech.anno.controller;

import cn.staitech.anno.domain.organ.Organ;
import cn.staitech.anno.domain.structure.Structure;
import cn.staitech.anno.service.StructureService;
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

    /**
     * 轮次列表 .
     */
    // @RequiresPermissions("anno:round:list")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "结构列表", notes = "结构列表 - 王峰")
    @Log(title = "结构列表", menu = "结构", subMenu = "结构列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<Structure>> list() throws ExecutionException, InterruptedException {
        List<Structure> list = structureService.list();
        return R.ok(list);
    }
    
    /**
     * 种属-脏器-结构-列表 .
     */
    // @RequiresPermissions("anno:round:list")
    @ApiOperationSupport(author = "wanglibei")
    @Log(title = "结构列表", menu = "结构", subMenu = "结构列表", businessType = BusinessType.QUERY)
    @GetMapping("/getStructureList")
    public R<List<Structure>> getStructureList(@RequestParam(required = true, name = "speciesId") String speciesId,
            @RequestParam(required = true, name = "organId") String organId) throws ExecutionException, InterruptedException {
        List<Structure> list = structureService.getStructureList(speciesId,organId);
        return R.ok(list);
    }
    
    /**
     * 种属-脏器列表 .
     */
    // @RequiresPermissions("anno:round:list")
    @ApiOperationSupport(author = "wanglibei")
    @Log(title = "脏器列表", menu = "结构", subMenu = "脏器列表", businessType = BusinessType.QUERY)
    @GetMapping("/getOrganByspeciesId")
    public R<List<Organ>> getOrganByspeciesId(@RequestParam(required = true, name = "speciesId") String speciesId) throws ExecutionException, InterruptedException {
        List<Organ> list = structureService.getOrganBySpeciesId(speciesId);
        return R.ok(list);
    }

}
