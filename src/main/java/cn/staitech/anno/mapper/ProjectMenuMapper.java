package cn.staitech.anno.mapper;


import cn.staitech.anno.domain.ProjectMenu;

import java.util.List;

public interface ProjectMenuMapper {
    int deleteByPrimaryKey(Long menuId);

    int insert(ProjectMenu record);

    int insertSelective(ProjectMenu record);
    
    ProjectMenu selectByPrimaryKey(Long menuId);
    
    List<ProjectMenu> selectList();

    int updateByPrimaryKeySelective(ProjectMenu record);

    int updateByPrimaryKey(ProjectMenu record);
}