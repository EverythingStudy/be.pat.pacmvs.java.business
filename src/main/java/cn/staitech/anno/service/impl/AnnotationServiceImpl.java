package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.*;
import cn.staitech.anno.mapper.AnnotationMapper;
import cn.staitech.anno.service.*;
import cn.staitech.anno.service.impl.manage.AnnotationManage;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.annotation.AnnotationBroadcastVO;
import cn.staitech.anno.vo.annotation.AnnotationJsonVO;
import cn.staitech.anno.vo.annotation.AnnotationPageVO;
import cn.staitech.anno.vo.annotation.AnnotationStateVO;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysProjectRole;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 标注 服务层实现 .
 *
 * @author staitech
 */
@Service
public class AnnotationServiceImpl implements AnnotationService {

    @Resource
    private AnnotationManage annotationManage;

    @Resource
    private AnnotationMapper annotationMapper;

    @Resource
    private ProjectMemberService projectMemberService;

    @Resource
    private ProjectRoleService projectRoleService;

    @Resource
    private ProjectMenuService projectMenuService;

    @Resource
    private ProjectRoleMenuService projectRoleMenuService;

    /**
     * 查询标注Roi .
     *
     * @param slideId 切片id
     * @return True || false
     */
    @Override
    public List<Annotation> selectAnnotationRoi(Long slideId) {
        return annotationMapper.selectAnnotationRoi(slideId);
    }

    /**
     * 批量删除RoI标注 .
     *
     * @param slideId 切片id
     * @return True || false
     */
    @Override
    public int batchDeleteRoi(Long slideId) {
        return annotationMapper.batchDeleteRoi(slideId);
    }

    /**
     * 批量插入标注信息 .
     *
     * @param annotationList 标注信息
     * @return True || false
     */
    @Override
    public int insertAnnotationList(List<Annotation> annotationList) {
        return annotationMapper.insertAnnotationList(annotationList);
    }

    /**
     * 查询标注信息 .
     *
     * @param annotationId 标注id
     * @return 图像信息
     */
    @Override
    public Annotation selectAnnotationById(Long annotationId) {
        return annotationMapper.selectAnnotationById(annotationId);
    }

    @Override
    public int annotationCount(Long slideId) {
        return annotationMapper.annotationCount(slideId);
    }

    @Override
    public List<AnnotationBroadcastVO> annotationUserCategory(AnnotationPageVO annotationPageVo) {
        return annotationMapper.annotationUserCategory(annotationPageVo);

    }

    @Override
    public AnnotationBroadcastVO annoUserCategory(Long annotationId) {
        return annotationMapper.annoUserCategory(annotationId);

    }

    /**
     * 新增标注 .
     *
     * @param annotation 新增标注
     * @return 结果
     */
    @Override
    public int insertAnnotation(Annotation annotation) {

        return annotationManage.insertAnnotation(annotation);
    }

    /**
     * 修改标注 .
     *
     * @param annotation 修改标注
     * @return 结果
     */
    @Override
    public int updateAnnotation(Annotation annotation) {
        return annotationManage.updateAnnotation(annotation);
    }


    /**
     * 修改标注描述 .
     *
     * @param annotation 修改标注描述
     * @return 结果
     */
    @Override
    public int updateAnnotationDescription(Annotation annotation) {
        return annotationMapper.updateAnnotationDescription(annotation);
    }

    /**
     * 删除标注 .
     *
     * @param annotationId 删除标注
     * @return 结果
     */
    @Override
    public int deleteAnnotationById(Long annotationId) {

        return annotationManage.deleteAnnotationById(annotationId);

    }

    /**
     * 通过项目ID，用户ID，批量删除标注 .
     *
     * @param annotation annotation
     * @return 结果
     */
    @Override
    public int deleteAnnotationByProjectIdAndUserId(Annotation annotation) {

        return annotationMapper.deleteAnnotationByProjectIdAndUserId(annotation);

    }

    /**
     * 通过项目ID，用户ID，查询标注 .
     *
     * @param annotation 标注
     * @return 结果
     */
    @Override
    public List<Annotation> selectAnnotationByProjectIdAndUserId(Annotation annotation) {
        return annotationMapper.selectAnnotationByProjectIdAndUserId(annotation);
    }


    /**
     * 查询图像信息 .
     *
     * @param slideId 切片id
     * @return SlideViewer
     */
    @Override
    public SlideViewer selectImageBySlideId(Long slideId) {
        return annotationMapper.selectImageBySlideId(slideId);
    }

    /**
     * 修改标注状态 .
     *
     * @param viewerAnnotationVo 标注状态
     * @return int
     */
    @Override
    public int updateViewerAnnotation(AnnotationStateVO viewerAnnotationVo) {
        return annotationMapper.updateViewerAnnotation(viewerAnnotationVo);
    }

    @Override
    public List<AnnotationJsonVO> selectAnnotationJson(Long slideId) {
        return annotationMapper.selectAnnotationJson(slideId);
    }

    /**
     * 根据projectId 查询用户 .
     *
     * @param projectId 项目id
     * @return
     */
    @Override
    public List<Annotation> selectByProjectId(Long projectId) {
        return annotationMapper.selectByProjectId(projectId);
    }

    /**
     * 根据annotation 查询标注信息 .
     *
     * @param annotation 查询条件
     * @return
     */
    @Override
    public List<Annotation> selectByCondition(Annotation annotation) {
        return annotationMapper.selectByCondition(annotation);
    }

    /**
     * 切片id查询unable以外标注 .
     *
     * @param slideId 切片id
     * @return 结果
     */
    public Integer selectAnnotationCount(Long slideId) {
        return annotationMapper.selectAnnotationCount(slideId);
    }

    /**
     * 通过切片id查询所有标注 .
     *
     * @param slideId 切片id
     * @return 结果
     */
    @Override
    public List<Annotation> selectSlideBy(Long slideId) {
        return annotationMapper.selectSlideBy(slideId);
    }

    /**
     * 查询已导入的标注
     *
     * @param annotation
     * @return
     */
    @Override
    public List<Annotation> queryUploadAnnotation(Annotation annotation) {
        return annotationMapper.queryUploadAnnotation(annotation);
    }

    /**
     * 查询当前用户在项目中的权限 .
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public boolean getPermission(Long projectId, String permission) throws Exception {
        ProjectMember projectMember = new ProjectMember();

        projectMember.setProjectId(projectId);

        projectMember.setUserId(SecurityUtils.getUserId());

        List<ProjectMember> projectMemberList = projectMemberService.selectProject(projectMember);


        if (projectMemberList.size() != 1) {
            throw new Exception(MessageSource.M("USER_INFO_ERROR"));
        }
        ProjectMember projectMemberBy = projectMemberList.get(0);

        Long roleId = null;
        if (projectMemberBy != null) {
            roleId = projectMemberBy.getRoleId();
        }
        int roleType = 0;
        if (roleId != null) {
            SysProjectRole projectRoleBy = projectRoleService.selectProjectRole(roleId);
            if (projectRoleBy != null) {
                roleType = projectRoleBy.getRoleType();
            }
        }
        // 获取所有权限
        List<ProjectMenu> sysProjectMenuList = projectMenuService.selectList();
        List<ProjectRoleMenu> projectRoleMenuKeyList = projectRoleMenuService.selectRoleId(roleId);
        List<String> permissionsList = new ArrayList<>();
        for (ProjectMenu projectMenu : sysProjectMenuList) {
            if (roleType == 1) {
                permissionsList.add(projectMenu.getPerms());
            } else {
                for (ProjectRoleMenu projectRoleMenuKey : projectRoleMenuKeyList) {
                    if (Objects.equals(projectMenu.getMenuId(), projectRoleMenuKey.getMenuId())) {
                        permissionsList.add(projectMenu.getPerms());
                    }
                }
            }
        }
        return permissionsList.stream().filter(StringUtils::hasText).anyMatch(x -> PatternMatchUtils.simpleMatch(x, permission));
    }

}
