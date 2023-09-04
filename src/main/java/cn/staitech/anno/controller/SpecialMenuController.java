package cn.staitech.anno.controller;

import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.special.SpecialMenu;
import cn.staitech.anno.domain.vo.special.RouterVo;
import cn.staitech.anno.domain.vo.special.SpecialMenuQuery;
import cn.staitech.anno.service.SpecialMenuService;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.domain.AjaxResult;
import cn.staitech.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.*;

import static cn.staitech.anno.constant.R.ResponseConstant.OPERATE_SUCCEED;

/**
 * @Description ：专题菜单信息
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialMenuController
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/6 星期二 10:36
 */
@Slf4j
@Api(tags = "专题菜单信息")
@RestController
@RequestMapping("/specialMenu")
public class SpecialMenuController {
    @Resource
    private SpecialMenuService specialMenuService;

    @Resource
    private SpecialService specialService;

    /**
     * 获取用户菜单列表
     *
     * @param menu
     * @return
     */
    @ApiIgnore
    @ApiOperation(value = "获取用户菜单列表", notes = "YangLei")
//    @RequiresPermissions("special:menu:list")
    @GetMapping("/list")
    public R<Object> list(SpecialMenuQuery menu) {
        Long userId = SecurityUtils.getUserId();
        R<Object> objectR = R.ok();
        objectR.setData(specialMenuService.selectMenuList(menu, userId));
        objectR.setMsg(OPERATE_SUCCEED);
        return objectR;
    }

    /**
     * 根据角色id查询专题权限列表
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "根据角色id查询专题权限列表")
    @GetMapping("/roleId")
    public List<SpecialMenu> querySpecialRolePermsByUserId(Long roleId) {
        List<SpecialMenu> specialRoleUserList = specialMenuService.querySpecialRolePermsByRoleId(roleId);
        return specialRoleUserList;
    }

    /**
     * 获取菜单下拉树列表
     *
     * @param menu
     * @return
     */
    @ApiOperation(value = "获取菜单下拉树列表", notes = "YangLei")
    @GetMapping("/treeSelect")
    public R<Object> treeSelect(SpecialMenuQuery menu) {
        Long userId = SecurityUtils.getUserId();
        List<SpecialMenu> menus = specialMenuService.selectMenuList(menu, 1L);
        R<Object> objectR = R.ok();
        objectR.setData(specialMenuService.buildMenuTreeSelect(menus));
        objectR.setMsg(OPERATE_SUCCEED);
        return objectR;
    }

    /**
     * 获取角色菜单列表树
     *
     * @param roleId
     * @return
     */
    @ApiIgnore
    @ApiOperation(value = "获取角色菜单列表树", notes = "YangLei")
    @GetMapping(value = "/roleMenuTreeSelects/{roleId}")
    public R<Object> roleMenuTreeSelects(@ApiParam(name = "roleId", value = "角色ID") @PathVariable("roleId") Long roleId) {
        List<Long> menuListByRoleId = specialMenuService.selectMenuListByRoleId(roleId);
        ArrayList<SpecialMenu> menus = new ArrayList<>();
        for (Long menuId : menuListByRoleId) {
            menus.add(specialMenuService.selectMenuById(menuId));
        }
        R<Object> objectR = R.ok();
        objectR.setData(specialMenuService.buildMenuTreeSelect(menus));
        objectR.setMsg(OPERATE_SUCCEED);
        return objectR;
    }

    /**
     * 获取角色菜单列表树
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "获取角色菜单列表树", notes = "YangLei")
    @GetMapping(value = "/roleMenuTreeSelect/{roleId}")
    public R<Object> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        Long userId = SecurityUtils.getUserId();
        List<SpecialMenu> menus = specialMenuService.selectMenuList(1L);
        R<Object> ok = R.ok();
        HashMap hashMap = new HashMap();
        hashMap.put("checkedKeys", specialMenuService.selectMenuListByRoleId(roleId));
        hashMap.put("menus", specialMenuService.buildMenuTreeSelect(menus));
        ok.setData(hashMap);
        ok.setMsg(OPERATE_SUCCEED);
        return ok;
    }

    /**
     * 获取路由信息
     *
     * @return
     */
    @ApiIgnore
    @ApiOperation(value = "获取路由信息", notes = "YangLei")
    @GetMapping("/getRouters/{specialId}")
    public AjaxResult getRouters(@ApiParam(name = "specialId", value = "专题ID") @PathVariable("specialId") Long specialId) {
        Long userId = SecurityUtils.getUserId();
        List<SpecialMenu> menus = specialMenuService.selectMenuTreeByUserId(userId, specialId);
        return AjaxResult.success(specialMenuService.buildMenus(menus));
    }

    /**
     * 获取专题菜单列表
     *
     * @return
     */
    @ApiOperation(value = "获取专题菜单列表", notes = "YangLei")
    @GetMapping("/getList/{specialId}")
    public R<HashMap> getList(@ApiParam(name = "specialId", value = "专题ID") @PathVariable("specialId") Long specialId) {
        Long userId = SecurityUtils.getUserId();
        List<SpecialMenu> list = specialMenuService.selectMenuListBySpecialId(userId,specialId);
        Special special = specialService.selectSpecialById(specialId);
        List<Object> permissions = new ArrayList<>();
        list.forEach(o -> {
            permissions.add(o.getPerms());
        });
        List<SpecialMenu> menus = specialMenuService.selectMenuTreeByUserId(userId, specialId);
        List<RouterVo> buildMenus = specialMenuService.buildMenus(menus);
        HashMap<String, Object> map = new HashMap<>();
        map.put("menus", buildMenus);
        map.put("permissions", permissions);
        map.put("specialName", special.getSpecialName());
        return R.ok(map, OPERATE_SUCCEED);
    }
}
