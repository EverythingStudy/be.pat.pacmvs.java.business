package cn.staitech.anno.controller;

import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.RecentlyVisited;
import cn.staitech.anno.domain.vo.ProjectMemberAddVO;
import cn.staitech.anno.domain.vo.ProjectMemberDeleteVO;
import cn.staitech.anno.domain.vo.ProjectMemberSelectVO;
import cn.staitech.anno.domain.vo.ProjectMemberUpdateVO;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.anno.service.RecentlyVisitedService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.utils.ProjectUtils;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    private RecentlyVisitedService recentlyVisitedService;

    @Log(title = "项目成员表增加", businessType = BusinessType.INSERT)
    @ApiOperation(value = "项目成员表增加")
    @PostMapping("/addProjectMember")
    public R addProjectMember(@RequestBody ProjectMemberAddVO projectMemberAddVO) {
        // 当前用户
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        // 获取项目ID
        Long projectId = projectMemberAddVO.getProjectId();
        // 添加有效用户总数
        AtomicInteger sum = new AtomicInteger(0);

        // 遍历添加
        for (Long userId : projectMemberAddVO.getUserId()) {
            ProjectMember projectMember = ProjectMember.builder()
                    .projectId(projectId)
                    .userId(userId)
                    .organizationId(sysUser.getOrganizationId())
                    .build();

            List<ProjectMember> list = projectMemberService.select(projectMember);

            if (list.size() > 0) {
                continue;
            }

            projectMember.setCreateBy(sysUser.getUserId());
            projectMember.setCreateTime(new Date());
            if (projectMemberService.save(projectMember) > 0) {
                //更新项目时间
                ProjectUtils.updateProjectStatus(projectMemberAddVO.getProjectId());
            }

            sum.getAndIncrement();
        }

        if (sum.get() > 0) {
            return R.ok(MessageSource.M("INSERT_SUCCESS"));
        }

        return R.fail(MessageSource.M("INSERT_FAILURE"));
    }

    @Log(title = "项目成员表删除", businessType = BusinessType.DELETE)
    @ApiOperation(value = "项目成员表删除")
    @PostMapping("/delProjectMember")
    public R delProjectMember(@RequestBody ProjectMemberDeleteVO deleteVO) {
        // 获取项目ID
        Long projectId = deleteVO.getProjectId();

        // 遍历删除
        for (Long userId : deleteVO.getUserIds()) {
            ProjectMember projectMember = ProjectMember.builder()
                    .projectId(projectId)
                    .userId(userId)
                    .build();

            if (projectMemberService.delete(projectMember) > 0) {
                QueryWrapper<RecentlyVisited> recentlyVisitedQueryWrapper = new QueryWrapper<>();
                recentlyVisitedQueryWrapper.eq("project_id", projectId).eq("user_id", userId);
                recentlyVisitedService.remove(recentlyVisitedQueryWrapper);
                return R.ok(MessageSource.M("DELETE_SUCCESS"));
            }
        }
        return R.ok(MessageSource.M("DELETE_FAILURE"));
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
            return R.fail(MessageSource.M("DISALLOW_PROJECT_NOT_EXIST") + SecurityUtils.getUsername());
        }

        // 获取角色ID
        Long roleId = projectMemberUpdateVO.getRoleId();

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
            return R.ok(MessageSource.M("UPDATE_SUCCESS"));
        }
        return R.fail(MessageSource.M("UPDATE_FAILED"));
    }

    @ApiOperation(value = "查询项目成员表列表")
    @PostMapping("/selectProjectMember")
    public R<List<ProjectMember>> selectProjectMember(@RequestBody ProjectMemberSelectVO projectMemberSelectVO) {
        // 获取项目ID
        Long projectId = projectMemberSelectVO.getProjectId();

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
    public R<PageMaster<List<ProjectMember>>> selectProjectMemberList(@RequestBody ProjectMemberSelectVO req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);
        ProjectMember projectMember = new ProjectMember();
        BeanUtils.copyProperties(req, projectMember);
        List<ProjectMember> list = projectMemberService.select(projectMember);
        PageMaster pageMaster = new PageMaster<>(list);
        return R.ok(pageMaster);
    }

    @ApiOperation(value = "查询项目当前用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, dataType = "Long", paramType = "query")})
    @PostMapping("/selectProjectMemberBy")
    public R<ProjectMember> selectProjectMemberBy(Long projectId) {

        if (!Optional.ofNullable(projectId).isPresent()) {
            return R.fail(MessageSource.M("DISALLOW_NOT_PROJECT"));
        }
        ProjectMember projectMemberBy = projectMemberService.getLoginUserProjectRoleType(projectId);

        // 查询项目中是否添加当前用户
        if (projectMemberBy == null) {
            return R.fail(MessageSource.M("DISALLOW_PROJECT_NOT_EXIST") + SecurityUtils.getUsername());
        }
        return R.ok(projectMemberBy);
    }
}
