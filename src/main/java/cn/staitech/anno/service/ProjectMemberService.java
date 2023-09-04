package cn.staitech.anno.service;


import cn.staitech.anno.domain.ProjectMember;

import java.util.List;

/**
 * 项目 服务层
 *
 * @author staitech
 */
public interface ProjectMemberService {
    public List<ProjectMember> selectByProjectId(Long projectId);

    public List<ProjectMember> selectByUserId(Long UserId);

    public int save(ProjectMember record);

    public int saveSelective(ProjectMember record);

    public int deleteByPrimaryKey(Integer imageId);

    public int updateByPrimaryKeySelective(ProjectMember record);

    public int updateByPrimaryKey(ProjectMember record);

    public int delete(ProjectMember record);

    int update(ProjectMember projectMember);

    List<ProjectMember> select(ProjectMember projectMember);

    /**
     * 查询用户id（结果去重的）
     * */
    List<ProjectMember> selectByPrimaryKey(ProjectMember projectMember);


    /**
     * 查询对应项目ID、用户ID是否项目代表总
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 符合条件的记录总数
     */
    int representationCount(Long projectId, Long userId);

    /**
     * 查询对应项目ID项目代表总
     * @param projectId 项目ID
     * @return 符合条件的记录总数
     */
    int representationCount(Long projectId);


    /**
     * 获取当前登录用户在某项目的角色
     * @param projectId 项目ID
     * @return  角色类型值
     */
    ProjectMember getLoginUserProjectRoleType(Long projectId);


    /**
     * 检查登录用户是否匹配对应角色
     * @param projectId 项目ID
     * @param roleType
     * @return
     */
    boolean checkLonginUserRoleType(int userRoleType,int roleType);
    
    /**
     * 根据用户和项目查询详情
     *
     * @param projectMember 参数配置信息
     * @return 参数配置信息
     */
    List<ProjectMember> selectProject(ProjectMember projectMember);

}
