package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ProjectRoleMenu;
import cn.staitech.anno.mapper.ProjectRoleMenuMapper;
import cn.staitech.anno.service.ProjectRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProjectRoleMenuServiceImpl implements ProjectRoleMenuService {
    
    @Resource
    private ProjectRoleMenuMapper projectRoleMenuMapper;
    
    
    @Override
    public int insertProjectRM(ProjectRoleMenu projectRoleMenu) {
        return projectRoleMenuMapper.insertProjectRM(projectRoleMenu);
    }
    
    @Override
    public List<ProjectRoleMenu> selectRoleId(Long roleId){
        return projectRoleMenuMapper.selectRoleId(roleId);
        
    }
    



}
