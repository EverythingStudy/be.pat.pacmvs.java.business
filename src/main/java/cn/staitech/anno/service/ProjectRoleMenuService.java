package cn.staitech.anno.service;

import cn.staitech.anno.domain.ProjectRoleMenu;

import java.util.List;


public interface ProjectRoleMenuService {
    
    int insertProjectRM(ProjectRoleMenu sysProjectRoleMenuKey);
    
    List<ProjectRoleMenu> selectRoleId(Long roleId);

    
}
