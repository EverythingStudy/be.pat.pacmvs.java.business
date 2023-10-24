//package cn.staitech.anno.controller;
//
//import cn.staitech.anno.domain.special.SpecialRole;
//import cn.staitech.anno.domain.special.SpecialRoleUser;
//import cn.staitech.anno.domain.vo.special.*;
//import cn.staitech.anno.service.SpecialRoleUserService;
//import cn.staitech.anno.utils.MessageSource;
//import cn.staitech.anno.utils.PageMaster;
//import cn.staitech.common.core.domain.R;
//import cn.staitech.common.log.annotation.Log;
//import cn.staitech.common.log.enums.BusinessType;
//import cn.staitech.common.security.annotation.RequiresPermissions;
//import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
//import com.github.xiaoymin.knife4j.annotations.ApiSupport;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author gjt.
// */
//@Slf4j
////@Validated
//@RestController
//@ApiSupport(author = "gjt")
//@RequestMapping("/specialRoleUser")
//@Api(value = "专题角色用户", tags = "专题角色用户")
//public class SpecialRoleUserController {
//    @Resource
//    private SpecialRoleUserService specialRoleUserService;
//
//    @ApiOperation(value = "查询用户所参与的专题", hidden = true)
//    @GetMapping("/selectUserId")
//    public List<SpecialRoleUser> selectUserId(Long userId) {
//        List<SpecialRoleUser> specialRoleUsers = specialRoleUserService.selectUserId(userId);
//        return specialRoleUsers;
//    }
//
//    @ApiOperation(value = "查询用户专题详情")
//    @ApiOperationSupport(author = "gjt")
//    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long", paramType = "query")})
//    @PostMapping("/selectById")
//    public R<SpecialRoleUserSelectResVo> selectById(@Validated @RequestBody SpecialSelectByIn req) {
//        return R.ok(specialRoleUserService.selectUserSpecialBy(req));
//    }
//
//    @ApiOperation(value = "查询专题下的角色")
//    @ApiOperationSupport(author = "gjt")
//    @ApiImplicitParams({@ApiImplicitParam(name = "specialId", value = "专题id", required = true, dataType = "Long", paramType = "query")})
//    @GetMapping("/selectSpecialRole")
//    public R<List<SpecialRole>> selectSpecialRole(Long specialId) {
//        return R.ok(specialRoleUserService.selectSpecialRole(specialId));
//    }
//
//    @ApiOperationSupport(author = "gjt")
//    @Log(menu = "专题管理", subMenu = "用户配置", title = "查询", businessType = BusinessType.QUERY)
//    @ApiOperation(value = "查询专题信息")
//    @PostMapping("/selectList")
//    public R<PageMaster<SpecialRoleUserSelectResVo>> selectList(@Validated @RequestBody SpecialRoleUserSelectVo req) {
//        return R.ok(specialRoleUserService.selectList(req));
//    }
//
//    @ApiOperationSupport(author = "gjt")
//    @Log(menu = "专题管理", subMenu = "用户配置", title = "新增", businessType = BusinessType.INSERT)
//    @RequiresPermissions("special:users:add")
//    @ApiOperation(value = "专题添加角色")
//    @PostMapping("/insert")
//    public R<String> insert(@Validated @RequestBody SpecialRoleUserVo req) {
//        specialRoleUserService.insert(req);
//        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
//
//    }
//
//    @ApiOperationSupport(author = "gjt")
//    @Log(menu = "专题管理", subMenu = "用户配置", title = "编辑", businessType = BusinessType.UPDATE)
//    @RequiresPermissions("special:users:edit")
//    @ApiOperation(value = "修改专题角色")
//    @PostMapping("/update")
//    public R<String> update(@Validated @RequestBody SpecialRoleUserVo req) {
//        specialRoleUserService.updateRole(req);
//        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
//    }
//
//    @ApiOperationSupport(author = "gjt")
//    @Log(title = "专题管理", menu = "用户配置", subMenu = "启用禁用", businessType = BusinessType.UPDATE)
//    @RequiresPermissions("special:subject:enable")
//    @ApiOperation(value = "修改专题用户状态")
//    @PostMapping("/updateStatus")
//    public R<String> updateStatus(@Validated @RequestBody SpecialRoleUserStatusVo req) {
//        specialRoleUserService.updateStatus(req);
//        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
//    }
//
//}
