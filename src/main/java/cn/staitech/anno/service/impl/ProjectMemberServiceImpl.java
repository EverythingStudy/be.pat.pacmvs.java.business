package cn.staitech.anno.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.mapper.ProjectMemberMapper;
import cn.staitech.anno.project.domain.SysUser;
import cn.staitech.anno.project.vo.ImageAnnoUserQueryIn;
import cn.staitech.anno.project.vo.ProjectPartUserVO;
import cn.staitech.anno.project.vo.SelectProjectVO;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.common.security.utils.SecurityUtils;

/**
 * 项目 服务层实现
 *
 * @author staitech
 */
@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Resource
    private ProjectMemberMapper projectMemberMapper;


    @Override
    public List<ProjectMember> selectByProjectId(Long projectId) {
        List<ProjectMember> projectMember = projectMemberMapper.selectByProjectId(projectId);
        return projectMember;
    }

    @Override
    public List<ProjectMember> selectByUserId(Long UserId) {
        List<ProjectMember> projectMember = projectMemberMapper.selectByUserId(UserId);
        return projectMember;
    }

    @Override
    public int save(ProjectMember projectMember) {
        return projectMemberMapper.insert(projectMember);
    }

    @Override
    public int saveSelective(ProjectMember record) {
        int insertSelective = projectMemberMapper.insertSelective(record);
        return insertSelective;
    }

    @Override
    public int deleteByPrimaryKey(Integer imageId) {
        int deleteByPrimaryKey = projectMemberMapper.deleteByPrimaryKey(imageId);
        return deleteByPrimaryKey;
    }

    @Override
    public int updateByPrimaryKey(ProjectMember record) {
        int updateByPrimaryKey = projectMemberMapper.updateByPrimaryKey(record);
        return updateByPrimaryKey;
    }

    @Override
    public int delete(ProjectMember record) {
        int delete = projectMemberMapper.delete(record);
        return delete;
    }

    @Override
    public int update(ProjectMember projectMember) {
        int update = projectMemberMapper.update(projectMember);
        return update;
    }

    @Override
    public List<ProjectMember> select(ProjectMember projectMember) {
        List<ProjectMember> projectMemberList = projectMemberMapper.select(projectMember);
        return projectMemberList;
    }

    @Override
    public int updateByPrimaryKeySelective(ProjectMember record) {
        int updateByPrimaryKeySelective = projectMemberMapper.updateByPrimaryKeySelective(record);
        return updateByPrimaryKeySelective;
    }

    /**
     * 查询用户id（结果去重的）
     */
    @Override
    public List<ProjectMember> selectByPrimaryKey(ProjectMember projectMember) {
        return projectMemberMapper.selectByPrimaryKey(projectMember);
    }


    /**
     * 查询对应项目ID、用户ID是否项目代表总
     *
     * @param projectId 项目ID
     * @param userId    用户ID
     * @return 符合条件的记录总数
     */
    @Override
    public int representationCount(Long projectId, Long userId) {
        ProjectMember representationMember = ProjectMember.builder()
                .projectId(projectId)
                .userId(userId)
                .build();

        List<ProjectMember> memberList = projectMemberMapper.select(representationMember);
        return memberList.size();
    }


    /**
     * 查询对应项目ID项目代表总数
     *
     * @param projectId 项目ID
     * @return 符合条件的记录总数
     */
    @Override
    public int representationCount(Long projectId) {
        ProjectMember representationMember = ProjectMember.builder()
                .projectId(projectId)
                .build();

        List<ProjectMember> memberList = projectMemberMapper.select(representationMember);
        return memberList.size();
    }


    /**
     * 获取当前登录用户在某项目的角色
     *
     * @param projectId 项目ID
     * @return 角色类型值
     */
    @Override
    public ProjectMember getLoginUserProjectRoleType(Long projectId) {

        ProjectMember representationMember = ProjectMember.builder()
                .projectId(projectId)
                .userId(SecurityUtils.getUserId())
                .build();

        return projectMemberMapper.selectBy(representationMember);
    }

    /**
     * 检查登录用户是否匹配对应角色
     *
     * @param userRoleType
     * @param roleType
     * @return
     */
    @Override
    public boolean checkLonginUserRoleType(int userRoleType, int roleType) {
        // 项目贡献者没有添加成员的权限
        return userRoleType == roleType;
    }

    @Override
    public List<ProjectMember> selectProject(ProjectMember projectMember) {
        return projectMemberMapper.selectProject(projectMember);
    }

	@Override
	public List<SelectProjectVO> getProjectListByPM(ProjectMember projectMember) {
		List<SelectProjectVO> list = projectMemberMapper.getProjectListByPM(projectMember);
		return list;
	}

	@Override
	public List<ProjectPartUserVO> getUserList(ImageAnnoUserQueryIn query) {
		List<SysUser> list =  new ArrayList<SysUser>();
		Long[] projectId = query.getProjectIds();
		ProjectMember projectMember = new ProjectMember();
		if(null == projectId){
	    	cn.staitech.system.api.domain.SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
			Long userId = sysUser.getUserId();
			Long organizationId = sysUser.getOrganizationId();
//			Long userId = 1L;
//	    	Long organizationId = 1L;
			
			
			//查询自己参与的项目列表
			projectMember.setUserId(userId);
			projectMember.setOrganizationId(organizationId);
			list = projectMemberMapper.getUserListAll(projectMember);
		}else{
			projectMember.setProjectIds(projectId);
			list = projectMemberMapper.getUserListByProjectId(projectMember);
		}
		
		List<ProjectPartUserVO> retList =  new ArrayList<ProjectPartUserVO>();
		if(CollectionUtils.isNotEmpty(list)){
			for(SysUser user:list){
				ProjectPartUserVO vo = new ProjectPartUserVO();
				vo.setUserId(user.getUserId());
				vo.setNickName(user.getNickName());
				vo.setUserName(user.getUserName());
				retList.add(vo);
			}
		}
		return retList;
	}

}
