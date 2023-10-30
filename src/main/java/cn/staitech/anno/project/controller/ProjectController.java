package cn.staitech.anno.project.controller;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.project.domain.DownTask;
import cn.staitech.anno.project.service.ProjectService;
import cn.staitech.anno.project.vo.DownTaskIn;
import cn.staitech.anno.project.vo.ProjectIn;
import cn.staitech.anno.project.vo.ProjectVO;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/13 18:28:56
 */
@Slf4j
@Api(value = "项目接口", tags = "项目接口")
@RestController("ProjectControllerV1")
@Validated
@RestControllerAdvice
@RequestMapping("/intelligentAnno/project_v1")
public class ProjectController {
    @Resource
    private ProjectService projectService;
    @Resource
    private MarkingService markingService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<PageMaster<ProjectVO>> page(@RequestBody ProjectIn in) throws Exception {
        handleAuth(in);
        Page page = new Page(in.getPageNum(), in.getPageSize());
        projectService.pageProject(page, in);

        List<ProjectVO> list = page.getRecords();
        for (ProjectVO projectVO : list) {
            if (LanguageUtils.isEn()) {
                projectVO.setSpeciesVal(projectVO.getSpeciesValEn());
                projectVO.setPsName(projectVO.getPsNameEn());
                projectVO.setSpeciesValEn(projectVO.getSpeciesValEn());
                projectVO.setIndicatorName(projectVO.getIndicatorNameEn());
                projectVO.setProjectTypeName(MapConstant.getProjectTypeEn(projectVO.getProjectType()));
            } else {
                projectVO.setProjectTypeName(MapConstant.getProjectType(projectVO.getProjectType()));
            }
        }

        PageMaster<ProjectVO> pageMaster = PageMaster.of(list);
        pageMaster.setTotal(page.getTotal());
        return R.ok(pageMaster);
    }

    @RequiresPermissions("smartAnno:project:list")
    @ApiOperation(value = "列表查询")
    @PostMapping("/query")
    public R<List<ProjectVO>> query(@RequestBody ProjectIn in) throws Exception {
        handleAuth(in);
        return R.ok(projectService.queryProject(in));
    }


    @ApiOperation(value = "下载目录文件")
    @GetMapping("/downTaskByCode")
    public void downTaskByCode(@RequestParam("code") @ApiParam(name = "code", value = "下载任务编码", required = true) String code, HttpServletResponse response) throws Exception {
        markingService.downTaskByCode(code, response);
    }

    @ApiOperation(value = "项目导出json")
    @PostMapping("/jsonExport")
    @RequiresPermissions(value = {"smartAnno:project:slice:exportList", "smartAnno:project:slice:operation:exportJson", "smartAnno:project:export"}, logical = Logical.OR)
    public R<DownTask> jsonExport(@RequestBody DownTaskIn downTaskIN) throws Exception {
        return R.ok(markingService.projectJsonExport(downTaskIN.getProjectId(), downTaskIN.getSlideIds()));
    }

    /**
     * 管理员id为1
     * @param in
     */
    private void handleAuth(ProjectIn in) {
        Long userId = SecurityUtils.getUserId();
        if (userId > 1) {
            in.setUserId(userId);
        }
    }
}
