package cn.staitech.anno.controller;

import cn.staitech.anno.domain.ProjectType;
import cn.staitech.anno.service.ProjectTypeService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 项目类型
 *
 * @author wangfeng
 * @date 2023/09/19
 */
@Api(value = "项目类型", tags = "项目类型")
@RestController
@RequestMapping("/projectType")
@Slf4j
public class ProjectTypeController extends BaseController {
    @Resource
    private ProjectTypeService projectTypeService;

    /**
     * 项目类型列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "项目类型列表", notes = "项目类型列表 - 王峰")
    @Log(title = "项目类型列表", menu = "项目类型", subMenu = "项目类型列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<ProjectType>> list() throws ExecutionException, InterruptedException {
        List<ProjectType> list = projectTypeService.list();
        List<ProjectType> snapList = list .stream().filter(person -> !"7".equals(person.getProjectTypeId())).collect(Collectors.toList());
        return R.ok(snapList);
    }

}
