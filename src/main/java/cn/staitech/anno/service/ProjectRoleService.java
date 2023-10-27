package cn.staitech.anno.service;

import cn.staitech.system.api.domain.SysProjectRole;

import java.util.List;

/**
 * 角色业务层
 *
 * @author staitech
 */
public interface ProjectRoleService {

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<SysProjectRole> selectProjectRoleList(SysProjectRole role);

    /**
     * 根据主键查询单条信息
     *
     * @param roleId 角色id
     * @return 角色数据信息
     */
    SysProjectRole selectProjectRole(Long roleId);

    /**
     * 根据条件分页查询角色数据总记录数
     *
     * @param role
     * @return
     */
    public Integer countProjectRole(SysProjectRole role);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertProjectRole(SysProjectRole role);


    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteProjectRoleById(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    public int deleteProjectRoleByIds(Long[] roleIds);

    /**
     * 根据项目id查询数据
     *
     * @param projectId 项目id
     * @return 结果
     */
    List<SysProjectRole> selectByProjectId(Long projectId);

    /**
     * 新建项目-添加3个默认角色
     *
     * @param projectId 项目ID
     * @param createBy  创建人
     * @return 返回新添加的角色，重复添加的不更新，但是返回
     */
    public List<SysProjectRole> addProjectRoles(Long projectId, Long createBy);
}
