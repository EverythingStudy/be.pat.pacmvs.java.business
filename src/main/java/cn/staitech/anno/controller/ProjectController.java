package cn.staitech.anno.controller;

import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.RecentlyVisited;
import cn.staitech.anno.domain.file.Chunk;
import cn.staitech.anno.domain.project.ProjectPo;
import cn.staitech.anno.domain.project.in.OperateProjectIn;
import cn.staitech.anno.domain.project.in.ProjectIdsVO;
import cn.staitech.anno.domain.project.in.ProjectListQueryIn;
import cn.staitech.anno.domain.project.in.ProjectRemoveIn;
import cn.staitech.anno.domain.project.out.*;
import cn.staitech.anno.domain.projectgroup.ProjectGroup;
import cn.staitech.anno.domain.vo.ProjectListVO;
import cn.staitech.anno.domain.vo.project.InsertProjectVO;
import cn.staitech.anno.domain.vo.project.UpdateProjectStatusVO;
import cn.staitech.anno.domain.vo.project.UpdateProjectVO;
import cn.staitech.anno.mapper.ExamineScoreMapper;
import cn.staitech.anno.service.*;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.annotation.RequiresSpecialPermissions;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


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
    @Autowired
    private ProjectExtService projectExtService;

    @Resource
    private FileService fileService;

    @Resource
    private MarkingService markingService;

    @Resource
    private RecentlyVisitedService recentlyVisitedService;


    @GetMapping("getSystemDictOld")
    public R<List<SystemDictOut>> getSystemDictOld() {
        List<SystemDictOut> systemDict = projectExtService.getSystemDictOld();
        return R.ok(systemDict);
    }


    @ApiOperation(value = "获得系统、脏器下拉框数据")
    @GetMapping("/getSystemDict")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictId", value = "系统脏器类型代码:查询系统类型的时候传0", dataTypeClass = Long.class, paramType = "query", example = "1")})
    public R<List<SystemDictOut>> getSystemDict(@RequestParam("dictId") Long dictId) {
        List<SystemDictOut> systemDict = projectExtService.getSystemDict(dictId);
        return R.ok(systemDict);
    }


    @ApiOperation(value = "项目编辑")
    @RequiresPermissions("special:project:edit")
    @PostMapping("/operateProject")
    @Log(title = "项目配置-编辑", menu = "专题管理", subMenu = "专题创建", businessType = BusinessType.OTHER)
    public R operateProject(@Validated @RequestBody OperateProjectIn req) {
        return projectExtService.operateProject(req);

    }

    @ApiOperation(value = "查询项目详情接口")
    @RequiresPermissions("special:project:details")
    @GetMapping(value = "/{projectId}")
    @Log(title = "项目配置-详情", menu = "专题管理", subMenu = "专题创建", businessType = BusinessType.QUERY)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Long.class, paramType = "query", example = "1")})
    public R<ProjectInfoOut> getProjectById(@RequestParam("projectId") Long projectId) {
        ProjectInfoOut resp = projectExtService.getProjectById(projectId);
        return R.ok(resp);
    }

    @ApiOperation(value = "删除项目接口")
    @RequiresPermissions("special:project:remove")
    @PostMapping("/projectDel")
    @Log(title = "项目配置-删除", menu = "专题管理", subMenu = "专题创建", businessType = BusinessType.DELETE)
    public R projectRemove(@RequestBody ProjectRemoveIn req) {
        return projectExtService.projectRemove(req);

    }

    @ApiOperation(value = "获得项目导航列表")
    @GetMapping("/getNavigationBar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specialId", value = "专题id", dataTypeClass = Long.class, paramType = "query", example = "1")})
    public R<NavigationBarQueryOut> getNavigationBar(@RequestParam("specialId") Long specialId) {
        NavigationBarQueryOut resp = projectExtService.getNavigationBar(specialId);
        return R.ok(resp);
    }

    @ApiOperation(value = "根据用户id查询项目列表（包含下级分组）接口")
    @GetMapping("/queryProjectWithGroupByUserId")
    @Log(title = "智能阅片", menu = "智能阅片", subMenu = "切片列表", businessType = BusinessType.QUERY)
    public R queryProjectWithGroupByUserId() {
        try {
            return projectExtService.queryProjectWithGroupByUserId(SecurityUtils.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id查询项目列表")
    @GetMapping("/queryProjectByUserId")
    @Log(title = "智能阅片", menu = "智能阅片", subMenu = "切片列表", businessType = BusinessType.QUERY)
    public R<List<ProjectPo>> queryProjectByUserId(@RequestParam(required = false, name = "specialId") Long specialId,
                                                   @RequestParam(required = false, name = "projectName") String projectName) {
        try {
            return projectExtService.queryProjectByUserId(SecurityUtils.getUserId(), projectName, specialId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "根据项目id查询分组列表")
    @GetMapping("/queryGroupByProjectId")
    @Log(title = "智能阅片", menu = "智能阅片", subMenu = "切片列表", businessType = BusinessType.QUERY)
    public R queryGroupByProjectId(@RequestParam(required = false, name = "projectId") Long projectId,
                                   @RequestParam(required = false, name = "groupName") String groupName) {
        try {
            return projectExtService.queryGroupByProjectId(projectId, groupName, null);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "根据项目id查询分组列表(按需求定制)")
    @GetMapping("/queryGroupByProjectIdCustom")
    @Log(title = "智能阅片", menu = "智能阅片", subMenu = "切片列表", businessType = BusinessType.QUERY)
    public R queryGroupByProjectIdCustom(@RequestParam(required = false, name = "projectId") Long projectId, @RequestParam(required = false, name = "reasons") Long reasons,
                                         @RequestParam(required = false, name = "groupName") String groupName) {
        try {
            if (reasons == null) {
                //添加移走原因,'1给药结束安乐死、2恢复期结束安乐死'
                ProjectGroup pg1 = new ProjectGroup();
                pg1.setProjectId(projectId);
                pg1.setGroupName(MessageSource.M("REMOVE_REASON_1"));
                pg1.setGroupId(1L);
                ProjectGroup pg2 = new ProjectGroup();
                pg2.setProjectId(projectId);
                pg2.setGroupName(MessageSource.M("REMOVE_REASON_2"));
                pg2.setGroupId(2L);
                List<ProjectGroup> temp = new ArrayList<>();
                temp.add(pg1);
                temp.add(pg2);
                return R.ok(temp);
            } else {
                return projectExtService.queryGroupByProjectId(projectId, groupName, reasons);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 项目列表
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "获得智能阅片下项目列表")
    @RequiresSpecialPermissions("read-special:project:list")
    @PostMapping("/getProjectList")
    public R<PageResponse<ProjectListQueryOut>> getViewImageProjectList(@RequestBody @Validated ProjectListQueryIn req) {
        PageResponse<ProjectListQueryOut> resp = projectExtService.getProjectList(req);
        return R.ok(resp);
    }

    @ApiOperation(value = "组间报告")
    @GetMapping("/getInterGroupReport")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Long.class, paramType = "query", example = "1")})
    public R<InterGroupReportOut> getInterGroupReport(@RequestParam("projectId") Long projectId) {

        return projectExtService.getInterGroupReport(projectId);

    }

    @ApiOperation(value = "是否交付：true-已交付；false-未全部交付")
    @GetMapping("/getCreateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specialId", value = "专题id:必填", dataTypeClass = Long.class, paramType = "query", example = "1")})
    public R<Boolean> getCreateStatus(@RequestParam("specialId") Long specialId) {
        return projectExtService.getCreateStatus(specialId);

    }

    @ApiOperation(value = "是否存在分组true-存在分组；false-不存在分组")
    @GetMapping("/getSpecialGroup")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specialId", value = "专题id:必填", dataTypeClass = Long.class, paramType = "query", example = "1")})
    public R<Boolean> getSpecialGroup(@RequestParam("specialId") Long specialId) {
        return projectExtService.getSpecialGroup(specialId);

    }
        @ApiOperation(value = "是否已经点击自动创建")
    @GetMapping("/getCreateInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specialId", value = "专题id:必填", dataTypeClass = Long.class, paramType = "query", example = "1")})
    public R<CreateStatusOut> getCreateInfo(@RequestParam("specialId") Long specialId) {
        return projectExtService.getCreateSt(specialId);
    }


    // 以下为新版：------------------------------------------------------------------

    /**
     * 项目状态列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "项目状态列表", notes = "项目状态列表")
    @Log(title = "项目状态列表", menu = "项目状态列表", subMenu = "项目状态列表", businessType = BusinessType.QUERY)
    @GetMapping("/projectStatus")
    public R<Map<Integer, String>> colorType() {
        Map<Integer, String> map = null;
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
        Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        project.setOrganizationId(organizationId);
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

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "批量项目")
    @RequiresPermissions("projectConfig:projectList:remove")
    @PostMapping(value = "/remove")
    public R remove(@RequestBody ProjectIdsVO request) {
        List<Long> idList = request.getProjectIds();
        AtomicInteger processCount = new AtomicInteger(0);
        for (Long projectId : idList) {
            Project project = new Project();
            project.setProjectId(projectId);
            // 只能删除项目状态是未启动的项目。
            project.setStatus(1);
            QueryWrapper queryWrapper = new QueryWrapper<>(project);

            Project delProject = projectService.getOne(queryWrapper);
            if (delProject != null) {
                projectService.removeById(projectId);

                QueryWrapper<RecentlyVisited> recentlyVisitedQueryWrapper = new QueryWrapper<>();
                recentlyVisitedQueryWrapper.eq("project_id", projectId);
                recentlyVisitedService.remove(recentlyVisitedQueryWrapper);
                processCount.getAndIncrement();
            }
        }

        if (processCount.get() > 0) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        } else {
            return R.fail(MessageSource.M("OPERATE_ERROR"));
        }
    }


    @ApiOperation(value = "查询项目详情接口")
    // @RequiresPermissions("special:project:details")
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
