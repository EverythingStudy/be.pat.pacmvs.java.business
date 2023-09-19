package cn.staitech.anno.project.controller;

import cn.staitech.anno.project.service.ProjectService;
import cn.staitech.anno.project.vo.ProjectIN;
import cn.staitech.anno.project.vo.ProjectVO;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<PageMaster<ProjectVO>> page(@RequestBody ProjectIN in) throws Exception{
        handleAuth(in);
        Page page = new Page(in.getPageNum(), in.getPageSize());
        projectService.pageProject(page,in);
        PageMaster<ProjectVO> pageMaster = PageMaster.of(page.getRecords());
        pageMaster.setTotal(page.getTotal());
        return R.ok(pageMaster);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping("/query")
    public R<List<ProjectVO>> query(ProjectIN in) throws Exception{
        handleAuth(in);
        return R.ok(projectService.queryProject(in));
    }

    private void handleAuth(ProjectIN in){
        Long userId = SecurityUtils.getUserId();
        if (userId>0){
            in.setUserId(userId);
        }
    }

}
