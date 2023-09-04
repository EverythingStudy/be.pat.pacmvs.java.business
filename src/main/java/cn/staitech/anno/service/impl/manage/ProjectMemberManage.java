package cn.staitech.anno.service.impl.manage;

import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.vo.ProjectListVO;
import cn.staitech.anno.domain.vo.ProjectMemberVO;
import cn.staitech.anno.mapper.ProjectMemberMapper;
import cn.staitech.anno.service.GetUserInformationService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ROI 无属性标注
 */
@Slf4j
@Service
public class ProjectMemberManage {
    @Resource
    private SlideService slideService;
    @Resource
    private ProjectService projectService;
    @Resource
    private ProjectMemberMapper projectMemberMapper;
    @Resource
    private GetUserInformationService getUserInformationService;

    /**
     * 根据slideId查询所有标注类别
     *
     * @param slideId 标注ID
     * @return 标注类别列表
     */
    public List<ProjectMemberVO> list(Long slideId) {
        //查询切片是否存在
        Slide slide = slideService.selectById(slideId);

        if (slide == null) {
            return null;
        }

        //查询projectId
        Long projectId = slide.getProjectId();

        //查询项目信息
//        ProjectListVO project = projectService.selectProjectById(projectId);

        ProjectMember pm = new ProjectMember();
//        pm.setProjectId(project.getProjectId());
        pm.setProjectId(projectId);

        //查询项目成员
        List<ProjectMember> pmList = projectMemberMapper.select(pm);
        List<ProjectMemberVO> list = new ArrayList<>();

        for (ProjectMember projectMember : pmList) {

            ProjectMemberVO projectMemberVO = new ProjectMemberVO();
            BeanUtils.copyProperties(projectMember, projectMemberVO);

            projectMemberVO.setUserName(getUserInformationService.selectById(projectMember.getUserId()).getUserName());
            list.add(projectMemberVO);
        }


        return list;
    }

}





