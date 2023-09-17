package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Annotation;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.SlideAnnotationResult;
import cn.staitech.anno.domain.vo.ProjectMemberAddVO;
import cn.staitech.anno.domain.vo.ProjectMemberDeleteVO;
import cn.staitech.anno.domain.vo.ProjectMemberSelectVO;
import cn.staitech.anno.domain.vo.ProjectMemberUpdateVO;
import cn.staitech.anno.service.AnnotationService;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.anno.service.ProjectRoleService;
import cn.staitech.anno.utils.ProjectUtils;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysProjectRole;
import cn.staitech.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cn.staitech.anno.constant.ProjectMemberConstant.*;

/**
 * 项目成员配置
 *
 * @author wangfeng
 * @date 2023/04/04 10:30
 */
@ApiIgnore
@Api(tags = "项目成员配置")
@RestController
@RequestMapping("/projectMember")
public class ProjectMemberController extends BaseController {
    @Resource
    private ProjectMemberService projectMemberService;
    @Resource
    private ProjectRoleService projectRoleService;
    @Resource
    private AnnotationService annotationService;

    @Log(title = "项目成员表增加", businessType = BusinessType.INSERT)
    @ApiOperation(value = "项目成员表增加")
    @PostMapping("/addProjectMember")
    public R addProjectMember(@RequestBody ProjectMemberAddVO projectMemberAddVO) {

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        ProjectMember projectMember = ProjectMember.builder()
                .projectId(projectMemberAddVO.getProjectId())
                .userId(projectMemberAddVO.getUserId())
                .organizationId(sysUser.getOrganizationId())
                .build();

        List<ProjectMember> list = projectMemberService.select(projectMember);

        if (list.size() > 0) {
            return R.fail(INSERT_FAILURE_HAD_USER);
        }

        projectMember.setCreateBy(sysUser.getUserId());
        if (projectMemberService.save(projectMember) > 0) {
            //更新项目时间
            ProjectUtils.updateProjectStatus(projectMemberAddVO.getProjectId());
            return R.ok(INSERT_SUCCESS);
        }
        return R.fail(INSERT_FAILURE);
    }

    @Log(title = "项目成员表删除", businessType = BusinessType.DELETE)
    @ApiOperation(value = "项目成员表删除")
    @PostMapping("/delProjectMember")
    public R delProjectMember(@RequestBody ProjectMemberDeleteVO deleteVO) {
        // 获取项目ID
        Long projectId = deleteVO.getProjectId();

        ProjectMember projectMemberBy = projectMemberService.getLoginUserProjectRoleType(projectId);

        // 查询项目中是否添加当前用户
        if (projectMemberBy == null) {
            return R.fail(DISALLOW_PROJECT_NOT_EXIST + SecurityUtils.getUsername());
        }

        // 是否包含项目代表
        boolean isHaveRepresentation = false;
        // 遍历删除
        for (Long userId : deleteVO.getUserIds()) {
            // 查询是否是项目代表，项目代表不能删除
            if (projectMemberService.representationCount(projectId, userId) > 0) {
                isHaveRepresentation = true;
                continue;
            }

            ProjectMember projectMember = ProjectMember.builder()
                    .projectId(projectId)
                    .userId(userId)
                    .build();

            if (projectMemberService.delete(projectMember) > 0) {
                //更新项目时间
                ProjectUtils.updateProjectStatus(projectId);
                // 删除该用户在项目创建的所有标注
                Annotation annotation = Annotation.builder()
                        .projectId(projectId)
                        .createBy(userId)
                        .updateBy(userId)
                        .build();

                List<Annotation> annotationList = annotationService.selectAnnotationByProjectIdAndUserId(annotation);
                annotationService.deleteAnnotationByProjectIdAndUserId(annotation);

                List<SlideAnnotationResult> slideAnnotationResultVOList = new ArrayList<>();
                for (Annotation annotation1 : annotationList) {
                    SlideAnnotationResult slideAnnotationResultVO = new SlideAnnotationResult();
                    slideAnnotationResultVO.setSlideId(annotation1.getSlideId());
                    slideAnnotationResultVO.setUpdateBy(annotation1.getCreateBy());
                    slideAnnotationResultVO.setCategoryId(annotation1.getCategoryId());
                    if (!slideAnnotationResultVOList.contains(slideAnnotationResultVO)) {
                        slideAnnotationResultVOList.add(slideAnnotationResultVO);
                    }
                }
            }
        }

        if (deleteVO.getUserIds().length == 1 && isHaveRepresentation) {
            return R.ok(DISALLOW_DELETE_REPRESENTATION);
        }

        if (isHaveRepresentation) {
            return R.ok(DELETE_SUCCESS + DISALLOW_DELETE_REPRESENTATION);
        }

        return R.ok(DELETE_SUCCESS);
    }

    /**
     * @param projectMemberUpdateVO
     * @return
     * @Validated
     */
    @Log(title = "项目成员表修改", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "项目成员表修改")
    @PostMapping("/updateProjectMember")
    public R updateProjectMember(@RequestBody ProjectMemberUpdateVO projectMemberUpdateVO) {
        // 获取项目ID
        Long projectId = projectMemberUpdateVO.getProjectId();

        ProjectMember projectMemberBy = projectMemberService.getLoginUserProjectRoleType(projectId);

        // 查询项目中是否添加当前用户
        if (projectMemberBy == null) {
            return R.fail(DISALLOW_PROJECT_NOT_EXIST + SecurityUtils.getUsername());
        }

        // 获取角色ID
        Long roleId = projectMemberUpdateVO.getRoleId();
        SysProjectRole sysProjectRole = projectRoleService.selectProjectRole(roleId);

        // 查询当前项目项目代表总数，项目代表至少保留1名  http://jira.shengtong.com/browse/ANNO-709
        if (projectMemberService.representationCount(projectId) == 1) {
            return R.fail(REPRESENTATION_MUST_HAS_ONE);
        }

        // 构造查询对象(修改前数据)
        ProjectMember getProjectMember = ProjectMember.builder()
                .userId(projectMemberUpdateVO.getUserId())
                .projectId(projectId)
                .build();
        // 查询修改前的成员信息内容
        List<ProjectMember> projectMembers = projectMemberService.select(getProjectMember);

        // 构造修改对象
        ProjectMember projectMember = ProjectMember.builder()
                .userId(projectMemberUpdateVO.getUserId())
                .projectId(projectId)
                .roleId(roleId)
                .updateBy(SecurityUtils.getUserId())
                .build();

        if (projectMemberService.update(projectMember) > 0) {
            //更新项目时间
            ProjectUtils.updateProjectStatus(projectId);
            return R.ok(UPDATE_SUCCESS);
        }
        return R.fail(UPDATE_FAILED);
    }

    @ApiOperation(value = "查询项目成员表列表")
    @PostMapping("/selectProjectMember")
    public R<List<ProjectMember>> selectProjectMember(@RequestBody ProjectMemberSelectVO projectMemberSelectVO) {
        // 获取项目ID
        Long projectId = projectMemberSelectVO.getProjectId();

        ProjectMember projectMemberBy = projectMemberService.getLoginUserProjectRoleType(projectId);

        // 查询项目中是否添加当前用户
        if (projectMemberBy == null) {
            return R.fail(DISALLOW_PROJECT_NOT_EXIST + SecurityUtils.getUsername());
        }
        // 构造查询条件 ProjectMember
        ProjectMember projectMember = ProjectMember.builder()
                .projectId(projectId)
                .roleId(projectMemberSelectVO.getRoleId())
                .userName(projectMemberSelectVO.getUserName())
                .build();

        // 查询
        List<ProjectMember> projectMemberList = projectMemberService.select(projectMember);
        return R.ok(projectMemberList);
    }

    @ApiOperation(value = "项目成员表列表")
    @PostMapping("/selectProjectMemberList")
    public R<List<ProjectMember>> selectProjectMemberList(@RequestBody ProjectMemberSelectVO projectMemberSelectVO) {
        ProjectMember projectMember = new ProjectMember();
        BeanUtils.copyProperties(projectMemberSelectVO, projectMember);
        return R.ok(projectMemberService.select(projectMember));
    }


    @ApiOperation(value = "查询项目当前用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, dataType = "Long", paramType = "query")})
    @PostMapping("/selectProjectMemberBy")
    public R<ProjectMember> selectProjectMemberBy(Long projectId) {

        if (!Optional.ofNullable(projectId).isPresent()) {
            return R.fail(DISALLOW_NOT_PROJECT);
        }
        ProjectMember projectMemberBy = projectMemberService.getLoginUserProjectRoleType(projectId);

        // 查询项目中是否添加当前用户
        if (projectMemberBy == null) {
            return R.fail(DISALLOW_PROJECT_NOT_EXIST + SecurityUtils.getUsername());
        }
        return R.ok(projectMemberBy);
    }
}
