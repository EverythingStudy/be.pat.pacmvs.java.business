package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.ProjectMember;

import java.util.List;

public interface ProjectMemberMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(ProjectMember record);

    int insertSelective(ProjectMember record);

    List<ProjectMember> selectByProjectId(Long projectId);

    int updateByPrimaryKeySelective(ProjectMember record);

    int updateByPrimaryKey(ProjectMember record);

    int delete(ProjectMember record);
    int update(ProjectMember projectMember);

    List<ProjectMember> select(ProjectMember projectMember);
    
    ProjectMember selectBy(ProjectMember projectMember);

    List<ProjectMember> selectByUserId(Long userId);

    ProjectMember selectUserBy(ProjectMember projectMember);

    /**
     * 查询用户id（结果去重的）
     * */
    List<ProjectMember> selectByPrimaryKey(ProjectMember projectMember);
    
    
    List<ProjectMember> selectProject(ProjectMember projectMember);
}