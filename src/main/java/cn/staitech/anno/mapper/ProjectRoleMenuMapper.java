package cn.staitech.anno.mapper;


import cn.staitech.anno.domain.ProjectRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectRoleMenuMapper {
    
    int deleteByPrimaryKey(ProjectRoleMenu key);
    
    int insert(ProjectRoleMenu record);
    
    int insertSelective(ProjectRoleMenu record);
    
    List<ProjectRoleMenu> selectMenuId(Long id);
    
    
    List<ProjectRoleMenu> selectRoleId(Long id);
    
    ProjectRoleMenu selectMenuRole(ProjectRoleMenu sysProjectRoleMenuKey);
    
    int insertProjectRM(ProjectRoleMenu sysProjectRoleMenuKey);
    
    int insertPorRMList(@Param("projectRM") List<ProjectRoleMenu> sysProjectRoleMenuKey);
    
    int deletePorRMList(@Param("projectRM") List<ProjectRoleMenu> sysProjectRoleMenuKey);
    
    int updateStatus(ProjectRoleMenu sysProjectRoleMenuKey);
    
}