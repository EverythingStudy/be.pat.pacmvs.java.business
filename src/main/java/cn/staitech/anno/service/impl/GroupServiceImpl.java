package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Group;
import cn.staitech.anno.domain.vo.GroupListVO;
import cn.staitech.anno.domain.vo.GroupVO;
import cn.staitech.anno.domain.vo.ProjectGroupAddVO;
import cn.staitech.anno.mapper.GroupMapper;
import cn.staitech.anno.service.GroupService;
import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分组 服务层实现
 *
 * @author zmj
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupMapper groupMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSelective(Group group){
        //
        Group groupVerify=Group.builder().groupName("Group"+group.getGroupName()).gender(group.getGender()).
                specialId(group.getSpecialId()).delFlag(0).reasons(group.getReasons()).dosage(group.getDosage()).build();
        //根据组别和性别查询，判断分组是否存在
        List<GroupListVO> groupListVOS = groupMapper.selectAllGroup(groupVerify);
        if ( groupListVOS.isEmpty()) {
            group.setCreateBy(SecurityUtils.getUserId());
            group.setGroupName("Group"+group.getGroupName());
            //添加分组
            groupMapper.insertSelective(group);
            Long groupId=group.getGroupId();
            //查询所有项目id
            List<ProjectGroupAddVO> projectGroupAddVOS=groupMapper.selectProject(group.getSpecialId());
            if (!projectGroupAddVOS.isEmpty()){
                for (ProjectGroupAddVO groupAddVO:projectGroupAddVOS){
                    groupAddVO.setGroupId(groupId);
                    groupAddVO.setCreateBy(SecurityUtils.getUserId());
                }
                //项目分组表里给每个项目添加一条数据
                groupMapper.insertBatch(projectGroupAddVOS);}
            return 1;
        }
        return 0;
    }

    @Override
    public int selectGroup(Group group){
        return groupMapper.selectGroup(group);
    }

    @Override
    public List<GroupListVO> selectAllGroup(Group group){
        return groupMapper.selectAllGroup(group);
    }

    @Override
    public Long updateGroup(GroupVO group){
        return groupMapper.updateGroup(group);
    }

    @Override
    public int insertBatch(List<ProjectGroupAddVO> groupAddVOS){
        return groupMapper.insertBatch(groupAddVOS);
    }

    @Override
    public List<ProjectGroupAddVO> selectProject(Long specialId){
        return groupMapper.selectProject(specialId);
    }

    @Override
    public GroupListVO selectOneGroup(Group group){
        return groupMapper.selectOneGroup(group);
    }

    @Override
    public int selectSlide(Long groupId){
        return groupMapper.selectSlide(groupId);
    }

    @Override
    public List<ProjectGroupAddVO> selectProjectGroup(ProjectGroupAddVO projectGroupAddVO){
        return groupMapper.selectProjectGroup(projectGroupAddVO);
    }





}
