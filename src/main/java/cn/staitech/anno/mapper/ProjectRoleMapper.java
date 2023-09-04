package cn.staitech.anno.mapper;

import cn.staitech.system.api.domain.SysProjectRole;

import java.util.List;

/**
 * 项目角色表 数据层
 * @author wangfeng
 * 2023-03-25
 */
public interface ProjectRoleMapper {
    /**
     * 根据条件分页查询项目角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<SysProjectRole> selectProjectRoleList(SysProjectRole role);

    /**
     * 根据条件查询项目角色数据总记录数
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public Integer countProjectRole(SysProjectRole role);


    /**
     * 根据主键查询单条信息
     *
     * @param roleId 角色id
     * @return 角色数据信息
     */
    SysProjectRole selectProjectRole(Long roleId);

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateProjectRole(SysProjectRole role);

    /**
     * 新增角色信息
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



}
