package cn.staitech.anno.service.impl.manage;

import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExaminationManage {
    @Resource
    private ProjectMemberService projectMemberService;


    /**
     * 查询用户在项目中的角色
     */
    public List<ProjectMember> projectMemberList(Slide slide) {
        ProjectMember projectMember = new ProjectMember();
        projectMember.setUserId(SecurityUtils.getUserId());
        projectMember.setProjectId(slide.getProjectId());
        List<ProjectMember> projectMemberList = projectMemberService.select(projectMember);

        return projectMemberList;
    }
}
