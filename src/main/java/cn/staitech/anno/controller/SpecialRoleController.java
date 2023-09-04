package cn.staitech.anno.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.staitech.anno.domain.special.SpecialRole;
import cn.staitech.anno.domain.special.SpecialRoleUser;
import cn.staitech.anno.domain.vo.special.SpecialRoleInsertVO;
import cn.staitech.anno.domain.vo.special.SpecialRoleUpdateVO;
import cn.staitech.anno.domain.vo.special.SpecialRoleQueryVO;
import cn.staitech.anno.service.SpecialRoleService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.exception.ServiceException;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.system.api.domain.SysRole;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static cn.staitech.anno.constant.R.ResponseConstant.OPERATE_SUCCEED;
import static cn.staitech.common.core.utils.PageUtils.startPage;

/**
 * @Description ：专题角色
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialRoleController
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/5/30 星期二 14:23
 */
@Api(tags = "专题角色配置")
@RestController
@RequestMapping("/specialRole")
public class SpecialRoleController {
    @Resource
    private SpecialRoleService specialRoleService;

    /**
     * 角色列表
     *
     * @param role
     * @return
     */
    @ApiOperationSupport(author = " YangLei")
    @ApiOperation(value = "角色列表", notes = "专题角色列表", response = SysRole.class)
    @RequiresPermissions("special:roles:list")
    @PostMapping("/list")
    public R<PageMaster<SpecialRole>> list(@Validated @RequestBody SpecialRoleQueryVO role) {
        startPage(role.getPageNum(), role.getPageSize());
        List<SpecialRole> list = specialRoleService.selectRoleList(role);
        PageMaster<SpecialRole> pageMaster = new PageMaster<>(list);
        return R.ok(pageMaster, OPERATE_SUCCEED);
    }

    /**
     * 根据用户id查询专题角色列表
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据用户id查询专题角色列表")
    @GetMapping("/userId")
    public List<SpecialRoleUser> querySpecialRoleListByUserId(Long userId) {
        List<SpecialRoleUser> specialRoleUserList = specialRoleService.querySpecialRoleListByUserId(userId);
        return specialRoleUserList;
    }

    /**
     * 根据角色ID获取详细信息
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "根据角色ID获取详细信息", notes = "YangLei", response = SysRole.class)
    @GetMapping(value = "/{roleId}")
    public R<SpecialRole> getInfo(@ApiParam(name = "roleId", value = "角色ID") @PathVariable Long roleId) {
        return R.ok(specialRoleService.selectRoleById(roleId), OPERATE_SUCCEED);
    }

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    @ApiOperation(value = "新增角色", notes = "YangLei", response = SysRole.class)
    @RequiresPermissions("special:roles:add")
    @Log(title = "角色管理", menu = "专题管理", subMenu = "专题角色", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Integer> add(@Validated @RequestBody SpecialRoleInsertVO role) {
        SpecialRole specialRole = new SpecialRole();
        BeanUtils.copyProperties(role, specialRole);
        if (!specialRoleService.checkRoleNameUnique(specialRole)) {
            return R.fail("当前角色已存在");
        }
        if (!specialRoleService.checkRoleKeyUnique(specialRole)) {
            return R.fail("当前权限标识已存在");
        }
        return R.ok(specialRoleService.insertRole(specialRole), OPERATE_SUCCEED);
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @ApiOperation(value = "修改角色", notes = "YangLei", response = SysRole.class)
    @RequiresPermissions("special:roles:edit")
    @Log(title = "角色管理", menu = "专题管理", subMenu = "专题角色", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public R<Integer> edit(@Validated @RequestBody SpecialRoleUpdateVO role) {
//        roleService.checkRoleAllowed(role);
//        roleService.checkRoleDataScope(role.getRoleId());
        SpecialRole specialRole = new SpecialRole();
        BeanUtils.copyProperties(role, specialRole);
        SpecialRole roleById = specialRoleService.selectRoleById(specialRole.getRoleId());
        if (ObjectUtil.isNull(roleById)) {
            throw new ServiceException("修改角色'" + role.getRoleName() + "'失败，数据不存在");
        }
        if (!specialRoleService.checkRoleNameUnique(specialRole)) {
            return R.fail("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        if (!specialRoleService.checkRoleKeyUnique(specialRole)) {
            return R.fail("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return R.ok(specialRoleService.updateRole(specialRole), OPERATE_SUCCEED);
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @return
     */
    @ApiOperation(value = "修改角色状态", notes = "YangLei", response = SysRole.class)
    @RequiresPermissions("special:roles:enable")
    @Log(title = "角色管理", menu = "专题管理", subMenu = "专题角色", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Integer> changeStatus(@ApiParam(name = "roleId", value = "角色ID") @RequestParam("roleId") Long roleId) {
        return R.ok(specialRoleService.updateRoleStatus(roleId), OPERATE_SUCCEED);
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "删除角色", notes = "YangLei")
    @RequiresPermissions("special:roles:remove")
    @Log(title = "角色管理", menu = "专题管理", subMenu = "专题角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/del/{roleId}")
    public R<Integer> remove(@ApiParam(name = "roleId", value = "角色ID") @PathVariable Long roleId) {
        return R.ok(specialRoleService.deleteRoleById(roleId), OPERATE_SUCCEED);
    }
}
