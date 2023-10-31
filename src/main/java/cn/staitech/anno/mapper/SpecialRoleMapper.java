package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.special.SpecialRole;
import cn.staitech.anno.domain.special.SpecialRoleQueryVO;

import java.util.List;

/**
 * @Description ：专题角色 数据层
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialRoleMapper
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/5/30 星期二 15:49
 */
public interface SpecialRoleMapper {
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    List<SpecialRole> selectRoleList(SpecialRoleQueryVO role);

    /**
     * 根据专题id查询角色信息
     *
     * @param specialId 角色信息
     * @return 角色数据集合信息
     */
    List<SpecialRole> selectRoleListBySpecialId(Long specialId);

    /**
     * 校验角色名称是否唯一
     *
     * @param specialRole
     * @return 角色信息
     */
    SpecialRole checkRoleNameUnique(SpecialRole specialRole);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleKey 角色权限
     * @return 角色信息
     */
    SpecialRole checkRoleKeyUnique(SpecialRole roleKey);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int insertRole(SpecialRole role);

    /**
     * 修改角色信息
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
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleById(Long roleId);
}
