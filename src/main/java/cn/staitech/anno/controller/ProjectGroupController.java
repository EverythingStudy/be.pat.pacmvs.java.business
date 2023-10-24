package cn.staitech.anno.controller;

import cn.staitech.anno.domain.projectgroup.in.RemoveProjectGroupIn;
import cn.staitech.anno.domain.projectgroup.out.ProjectGroupListOut;
import cn.staitech.anno.service.ProjectGroupService;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author wudi
 * @Date 2023/5/30 16:42
 * @desc 项目分组模块
 */
@Slf4j
@Api(tags = "项目分组模块接口2.0")
@RestController
@RequestMapping("/projectGroup")
public class ProjectGroupController extends BaseController {

    @Autowired
    private ProjectGroupService projectGroupService;

    /**
     * 项目分组列表
     *
     * @param projectId
     * @return
     */
    @ApiOperation(value = "获得项目分组列表")
    @RequiresPermissions("special:project:grouplist")
    @GetMapping("/list")
    @Log(title = "项目配置-项目分组列表", menu = "专题管理", subMenu = "专题创建", businessType = BusinessType.QUERY)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Long.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "reasons", value = "移走原因", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageResponse<ProjectGroupListOut>> projectGroupList(@RequestParam("projectId") Long projectId,
                                                                 @RequestParam("reasons") int reasons,
                                                                 @RequestParam("pageNum") int pageNum,
                                                                 @RequestParam("pageSize") int pageSize) {
        PageResponse<ProjectGroupListOut> resp = projectGroupService.projectGroupList(projectId, reasons, pageNum, pageSize);
        return R.ok(resp);
    }

    @ApiOperation(value = "项目分组删除", notes = "项目分组删除")
    @RequiresPermissions("special:project:groupdel")
    @Log(title = "项目配置-项目分组删除", menu = "专题管理", subMenu = "专题创建", businessType = BusinessType.DELETE)
    @PutMapping("/removeProjectGroup")
    public R removeProjectGroup(@RequestBody RemoveProjectGroupIn req) {

        return projectGroupService.removeProjectGroup(req);

    }

    @ApiOperation(value = "清空分组内切片", notes = "清空分组内切片")
    @RequiresPermissions("special:project:clear")
    @Log(title = "项目配置-清空切片", menu = "专题管理", subMenu = "专题创建", businessType = BusinessType.DELETE)
    @PutMapping("/cleanProjectGroup")
    public R cleanProjectGroup(@RequestBody RemoveProjectGroupIn req) {

        return projectGroupService.cleanProjectGroup(req);

    }

}
