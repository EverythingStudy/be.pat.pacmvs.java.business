package cn.staitech.anno.service;

import cn.staitech.anno.domain.special.SpecialRole;
import cn.staitech.anno.domain.special.SpecialRoleUser;
import cn.staitech.anno.domain.vo.special.SpecialRoleQueryVO;

import java.util.List;

/**
 * @Description ：专题角色业务层
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialRoleService
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/5/30 星期二 15:42
 */
public interface SpecialRoleService {
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    List<SpecialRole> selectRoleList(SpecialRoleQueryVO role);

    /**
     * 根据用户id查询专题角色列表
     *
     * @param userId
     * @return
     */
    List<SpecialRoleUser> querySpecialRoleListByUserId(Long userId);

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean checkRoleNameUnique(SpecialRole role);

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean checkRoleKeyUnique(SpecialRole role);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int insertRole(SpecialRole role);

    /**
     * 新增角色菜单信息
     *
     * @param role
     * @return
     */
    public int insertRoleMenu(SpecialRole role);

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int updateRole(SpecialRole role);

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SpecialRole selectRoleById(Long roleId);

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int updateRoleStatus(Long roleId);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int countUserRoleByRoleId(Long roleId);

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleById(Long roleId);
}
