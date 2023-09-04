package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.ProjectRoleConstant;
import cn.staitech.anno.mapper.ProjectRoleMapper;
import cn.staitech.anno.service.ProjectRoleService;
import cn.staitech.system.api.domain.SysProjectRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色 业务层处理
 *
 * @author staitech
 */
@Service
public class ProjectRoleServiceImpl implements ProjectRoleService
{
    @Resource
    private ProjectRoleMapper projectRoleMapper;



    /**
     * 根据条件分页查询角色数据
     *
     * @param projectRole 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysProjectRole> selectProjectRoleList(SysProjectRole projectRole)
    {
        return projectRoleMapper.selectProjectRoleList(projectRole);
    }
    
    /**
     * 根据主键查询单条信息
     *
     * @param roleId 角色id
     * @return 角色数据信息
     */
    @Override
    public SysProjectRole selectProjectRole(Long roleId){
        return projectRoleMapper.selectProjectRole(roleId);
    }

    /**
     * 根据条件查询项目角色数据总记录数
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public Integer countProjectRole(SysProjectRole role) {
        return projectRoleMapper.countProjectRole(role);
    }

    @Override
    public int insertProjectRole(SysProjectRole role) {
        return projectRoleMapper.insertProjectRole(role);
    }

    @Override
    public int deleteProjectRoleById(Long roleId) {
        return projectRoleMapper.deleteProjectRoleById(roleId);
    }

    @Override
    public int deleteProjectRoleByIds(Long[] roleIds) {
        return projectRoleMapper.deleteProjectRoleByIds(roleIds);
    }
    
    @Override
    public List<SysProjectRole> selectByProjectId(Long projectId){
        return projectRoleMapper.selectByProjectId(projectId);
    }



    /**
     * 新建项目-添加3个默认角色
     * @param projectId 项目ID
     * @param createBy 创建人
     * @return 返回新添加的角色，重复添加的不更新，但是返回
     */
    public List<SysProjectRole> addProjectRoles(Long projectId,Long createBy)
    {
        List<SysProjectRole> list = new ArrayList<>(3);

        // 角色类型：1、项目代表；2、项目管理者；3、项目贡献者
        for (int i = 0; i < ProjectRoleConstant.ROLE_TYPE.length; i++) {
            // 构造参数
            SysProjectRole role = SysProjectRole
                    .builder()
                    .projectId(projectId)
                    .roleType(i+1)
                    .status(0)
                    .delFlag(0)
                    .build();

            // 校验该项目是否有角色
            List<SysProjectRole> srcRoleList = projectRoleMapper.selectProjectRoleList(role);

            // 无则插入
            if (srcRoleList.size()<1)
            {
                role.setRoleName(ProjectRoleConstant.ROLE_TYPE[i]);
                role.setCreateBy(createBy);
                projectRoleMapper.insertProjectRole(role);
                list.add(role);
            }else {
                list.add(srcRoleList.get(0));
            }
        }
        return list;
    }

}
