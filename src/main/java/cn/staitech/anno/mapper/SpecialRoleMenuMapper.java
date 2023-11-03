package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.special.SpecialRoleMenu;

import java.util.List;

/**
 * @Description ：专题角色和菜单关联 数据层
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialRoleMenuMapper
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/5/30 星期二 17:20
 */
public interface SpecialRoleMenuMapper {
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int checkMenuExistRole(Long menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleMenuByRoleId(Long roleId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteRoleMenu(Long[] ids);

    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    int batchRoleMenu(List<SpecialRoleMenu> roleMenuList);
}
