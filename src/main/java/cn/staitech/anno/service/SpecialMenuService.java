package cn.staitech.anno.service;

import cn.staitech.anno.vo.special.RouterVo;
import cn.staitech.anno.vo.special.SpecialMenu;
import cn.staitech.anno.vo.special.SpecialMenuQuery;
import cn.staitech.anno.vo.special.TreeSelect;

import java.util.List;

/**
 * @Description ：菜单 业务层
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialMenuService
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/7 星期三 19:37
 */
public interface SpecialMenuService {
    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SpecialMenu> selectMenuList(Long userId);

    /**
     * 根据角色id查询专题权限列表
     *
     * @param roleId
     * @return
     */
    List<SpecialMenu> querySpecialRolePermsByRoleId(Long roleId);

    List<SpecialMenu> selectMenuListBySpecialId(Long userId, Long specialId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SpecialMenu> selectMenuList(SpecialMenuQuery menu, Long userId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SpecialMenu> selectMenuListBySpecialId(SpecialMenuQuery menu, Long userId, Long specialId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SpecialMenu> selectMenuTreeByUserId(Long userId, Long specialId);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SpecialMenu selectMenuById(Long menuId);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildMenuTreeSelect(List<SpecialMenu> menus);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    List<SpecialMenu> buildMenuTree(List<SpecialMenu> menus);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVo> buildMenus(List<SpecialMenu> menus);
}
