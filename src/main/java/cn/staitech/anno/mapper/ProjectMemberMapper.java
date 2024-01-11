package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.project.domain.SysUser;
import cn.staitech.anno.project.vo.ImageAnnoStatisticsVO;
import cn.staitech.anno.project.vo.ProjectMemberQuery;
import cn.staitech.anno.project.vo.ProjectUserAnnoStatisticsVO;
import cn.staitech.anno.project.vo.SelectProjectVO;

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
     */
    List<ProjectMember> selectByPrimaryKey(ProjectMember projectMember);


    List<ProjectMember> selectProject(ProjectMember projectMember);

    List<SelectProjectVO> getProjectListByPM(ProjectMember projectMember);

    List<SysUser> getUserListAll(ProjectMember projectMember);

    List<SysUser> getUserListByProjectId(ProjectMember projectMember);

    List<ImageAnnoStatisticsVO> getImageCount(ProjectMemberQuery query);

    List<ProjectUserAnnoStatisticsVO> getProjectUserAnnoStatistics(ProjectMemberQuery query);

    List<ProjectUserAnnoStatisticsVO> getProjectUserAnnoStatistics1(ProjectMemberQuery query);

    List<ProjectUserAnnoStatisticsVO> getProjectUserAnnoStatistics2(ProjectMemberQuery query);
}