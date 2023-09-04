package cn.staitech.anno.controller;

import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目 信息操作处理  .
 *
 * @author 标明开发该类模块的作者
 * @version 标明该类模块的版本
 * @see ProjectMenuController
 */
@ApiIgnore
@Api(tags = "项目接口")
@RestController
@RequestMapping("/projectMenu")
public class ProjectMenuController extends BaseController {
    
    
    @Resource
    private ProjectMemberService projectMemberService;
    
    @ApiOperation(value = "根据用户id查询项目菜单")
    @GetMapping("/byUserId")
    public List<ProjectMember> byUserId(Long userId) {
        List<ProjectMember> projectMemberList = projectMemberService.selectByUserId(userId);
        return projectMemberList;
    }
    
    
}
