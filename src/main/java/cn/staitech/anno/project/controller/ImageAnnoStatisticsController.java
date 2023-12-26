package cn.staitech.anno.project.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.project.service.ImageAnnoStatisticsService;
import cn.staitech.anno.project.vo.ImageAnnoStatisticsVO;
import cn.staitech.anno.project.vo.ImageAnnoUserQueryIn;
import cn.staitech.anno.project.vo.ProjectPartUserVO;
import cn.staitech.anno.project.vo.SelectProjectVO;
import cn.staitech.anno.project.vo.SlideQueryIn;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: SlideController
* @Description:
* @author wanglibei
* @date 2023年12月25日
* @version V1.0
 */
@Slf4j
@Api(value = "标注统计-图像标注统计", tags = "标注统计-图像标注统计")
@RestController("imageAnnoStatistics")
@Validated
@RestControllerAdvice
@RequestMapping("/imageAnnoStatistics")
public class ImageAnnoStatisticsController {
    @Resource
    private ImageAnnoStatisticsService imageAnnoStatisticsService;
    @Resource
    private ProjectMemberService projectMemberService;
    

    //@RequiresPermissions(value = {"smartAnno:project:slice", "smartAnnoInfo:slice"}, logical = Logical.OR)
    @ApiOperation(value = "标注统计-图像标注统计分页查询")
    @PostMapping("/page")
    public R<PageResponse<ImageAnnoStatisticsVO>> page(@RequestBody SlideQueryIn req) throws Exception {
        PageResponse<ImageAnnoStatisticsVO> resp = imageAnnoStatisticsService.pageSlides(req);
        return R.ok(resp);
    }

    

    //@RequiresPermissions("smartAnno:project:slice:export")
    @ApiOperation(value = "标注统计-图像标注统计导出")
    @PostMapping("/exportImageAnnoStatistics")
    public void exportImageAnnoStatistics(SlideQueryIn req, HttpServletResponse response) throws Exception {
    	imageAnnoStatisticsService.slideAnnoStatisticsExport(req,response);
    }
    
    
    //@RequiresPermissions("smartAnno:project:slice:export")
    @ApiOperation(value = "项目列表")
    @PostMapping("/getProjectList")
    public R<List<SelectProjectVO>> getProjectList() throws Exception {
    	cn.staitech.system.api.domain.SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
    	Long userId = sysUser.getUserId();
    	Long organizationId = sysUser.getOrganizationId();
    	
    	//查询自己参与的项目列表
    	ProjectMember projectMember = new ProjectMember();
    	projectMember.setUserId(userId);
    	projectMember.setOrganizationId(organizationId);
    	List<SelectProjectVO> list = projectMemberService.getProjectListByPM(projectMember);
    	return R.ok(list);
    }
    
  //@RequiresPermissions("smartAnno:project:slice:export")
    @ApiOperation(value = "项目成员列表")
    @PostMapping("/getUserList")
    public R<List<ProjectPartUserVO>> getUserList(@RequestBody ImageAnnoUserQueryIn query) throws Exception {
    	List<ProjectPartUserVO> list = projectMemberService.getUserList(query);
    	return R.ok(list);
    }
}
