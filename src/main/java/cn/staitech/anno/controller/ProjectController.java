package cn.staitech.anno.controller;

import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.mapper.ExamineScoreMapper;
import cn.staitech.anno.service.FileService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.file.Chunk;
import cn.staitech.anno.vo.project.InsertProjectVO;
import cn.staitech.anno.vo.project.ProjectListVO;
import cn.staitech.anno.vo.project.UpdateProjectStatusVO;
import cn.staitech.anno.vo.project.UpdateProjectVO;
import cn.staitech.anno.vo.project.in.ProjectIdsVO;
import cn.staitech.anno.vo.project.in.ProjectListQueryIn;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @Author wangfeng
 * @Date 2023/9/19 10:42
 * @desc 项目模块
 */
@Slf4j
@Api(tags = "项目模块接口2.0")
@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {
    @Resource
    private ProjectService projectService;
    @Resource
    private ExamineScoreMapper examineScoreMapper;
    @Resource
    private ProjectMemberService projectMemberService;
    @Resource
    private FileService fileService;
    @Resource
    private MarkingService markingService;

    /**
     * 项目状态列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "项目状态列表", notes = "项目状态列表")
    @Log(title = "项目状态列表", menu = "项目状态列表", subMenu = "项目状态列表", businessType = BusinessType.QUERY)
    @GetMapping("/projectStatus")
    public R<Map<Integer, String>> colorType() {
        Map<Integer, String> map;
        if (LanguageUtils.isEn()) {
            map = Container.PROJECT_STATUS_EN;
        } else {
            map = Container.PROJECT_STATUS;
        }
        return R.ok(map);
    }


    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "添加项目")
    @RequiresPermissions("projectConfig:projectList:create")
    @Log(title = "添加项目", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public R<String> addProject(@Validated @RequestBody InsertProjectVO req) {
        Project project = new Project();
        BeanUtils.copyProperties(req, project);

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        project.setCreateBy(sysUser.getUserId());
        project.setOrganizationId(sysUser.getOrganizationId());

        if (projectService.insertProject(project) > 0) {
            // 获取当前项目Id
            Long projectId = project.getProjectId();
            // 向项目成员表添加当前用户
            ProjectMember projectMember = ProjectMember.builder()
                    .userId(sysUser.getUserId())
                    .projectId(projectId)
                    .organizationId(sysUser.getOrganizationId())
                    .roleId(sysUser.getRoleId()).createBy(sysUser.getUserId()).build();
            projectMemberService.save(projectMember);
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "查询项目列表")
    @PostMapping("/list")
    public R<PageMaster<List<ProjectListVO>>> getProjectList(@RequestBody @Validated ProjectListQueryIn req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);
        Project project = new Project();
        BeanUtils.copyProperties(req, project);

        // 机构层级
        if (req.getOrganizationId() == null || req.getOrganizationId() < 1) {
            if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
                project.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
            }
        } else {
            project.setOrganizationId(req.getOrganizationId());
        }

        List<ProjectListVO> list = projectService.selectProjectList(project);
        PageMaster pageMaster = new PageMaster<>(list);
        return R.ok(pageMaster);
    }


    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "编辑项目")
    @RequiresPermissions("projectConfig:projectList:edit")
    @Log(title = "编辑项目", menu = "编辑项目", subMenu = "编辑项目", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public R<String> editProject(@Validated @RequestBody UpdateProjectVO req) {
        Project project = new Project();
        BeanUtils.copyProperties(req, project);

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        project.setUpdateBy(sysUser.getUserId());
        project.setUpdateTime(new Date());
        project.setOrganizationId(sysUser.getOrganizationId());
        if (projectService.updateById(project)) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }

    @ApiOperation(value = "取消完成")
    @Log(title = "取消完成", menu = "取消完成", subMenu = "取消完成", businessType = BusinessType.UPDATE)
    @PostMapping("/cancelCompleted")
    @Transactional(rollbackFor = Exception.class)
    public R<String> cancelCompleted(@Validated @RequestBody UpdateProjectVO req) {
        Project project = new Project();
        BeanUtils.copyProperties(req, project);
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        project.setUpdateBy(sysUser.getUserId());
        project.setUpdateTime(new Date());
        project.setOrganizationId(sysUser.getOrganizationId());
        if (projectService.updateById(project)) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }
    
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "批量项目")
    @RequiresPermissions("projectConfig:projectList:remove")
    @PostMapping(value = "/remove")
    public R remove(@RequestBody ProjectIdsVO request) {
        Integer processCount = projectService.projectRemove(request);
        if (processCount > 0) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        } else {
            return R.fail(MessageSource.M("REMOVE_PROJECT_ERROR"));
        }
    }


    @ApiOperation(value = "查询项目详情接口")
    @GetMapping(value = "/detail")
    @Log(title = "项目配置-详情", menu = "专题管理", subMenu = "专题创建", businessType = BusinessType.QUERY)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Long.class, paramType = "query", example = "1")})
    public R<ProjectListVO> selectOne(@RequestParam("projectId") Long projectId) {
        ProjectListVO resp = projectService.selectProjectById(projectId);
        return R.ok(resp);
    }


    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "编辑项目")
    @Log(title = "编辑项目", menu = "编辑项目", subMenu = "编辑项目", businessType = BusinessType.UPDATE)
    @PostMapping("/editStatus")
    @Transactional(rollbackFor = Exception.class)
    public R<String> editProjectStatus(@Validated @RequestBody UpdateProjectStatusVO req) {

        // 查询考核表中是否有未完成考试的考核信息
        QueryWrapper<ExamineScore> examineScoreQueryWrapper = new QueryWrapper<>();
        examineScoreQueryWrapper.eq("project_id", req.getProjectId()).eq("operate_status", "1");
        List<ExamineScore> examineScoreList = examineScoreMapper.selectList(examineScoreQueryWrapper);
        if (examineScoreList.size() > 0) {
            return R.fail(MessageSource.M("ERROR_PROJECT_PROMPT"));
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Project project = new Project();
        project.setProjectId(req.getProjectId());
        project.setStatus(req.getStatus());
        project.setUpdateBy(sysUser.getUserId());
        project.setOrganizationId(sysUser.getOrganizationId());

        if (projectService.updateById(project)) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "导入zip文件(大文件)")
    @ApiImplicitParams({@ApiImplicitParam(name = "specialId", value = "专题Id", required = true, dataType = "Long"), @ApiImplicitParam(name = "fileName", value = "文件名称", required = true, dataType = "String"), @ApiImplicitParam(name = "chunk", value = "分片Id", required = true, dataType = "Integer"), @ApiImplicitParam(name = "chunkTotal", value = "分片总数", required = true, dataType = "Integer"), @ApiImplicitParam(name = "chunkSize", value = "分片大小", required = true, dataType = "Long"), @ApiImplicitParam(name = "file", value = "分片文件", required = true, dataType = "file")})
    @PostMapping("/uploadZip")
    public R<String> uploadZip(
            @RequestParam("specialId") Long specialId,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunk") Integer chunk,
            @RequestParam("chunkTotal") Integer chunkTotal,
            @RequestParam("chunkSize") Long chunkSize,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Chunk chunkObj = new Chunk().setChunkNumber(chunk).setFile(file).setFileName(fileName).setTotalChunks(chunkTotal).setSpecialId(specialId).setChunkSize(chunkSize);
        String zipUrl = fileService.mergeChunk(chunkObj);
        if (!Optional.ofNullable(specialId).isPresent()) {
            return R.fail(MessageSource.M("ARGUMENT_INVALID"));
        }
        markingService.zipExport(zipUrl, specialId);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }
}
