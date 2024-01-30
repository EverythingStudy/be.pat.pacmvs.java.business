package cn.staitech.anno.controller;

import cn.staitech.anno.project.constants.Constants;
import cn.staitech.anno.service.ProjectStatisticsService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.projectstatistics.*;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "项目内统计", tags = "项目内统计")
@RestController
@RequestMapping("/withinTheProject")
public class ProjectStatisticsController {

    @Resource
    private ProjectStatisticsService projectStatisticsService;


    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "当前项目", notes = "当前项目")
    @GetMapping("/projectInfo")
    public R<ProjectInfoOut> projectInfo(@NotNull(message = "项目id为空！") @RequestParam(value = "projectId",required = false) @ApiParam(name="projectId",value = "项目id",required = true) Long projectId){
        ProjectInfoOut projectInfoOut=projectStatisticsService.projectInfor(projectId);
        return R.ok(projectInfoOut);
    }

    @ApiOperation(value = "图像状态")
    @PostMapping("/queryStatus")
    public R<List> queryStatus() {
        List<Map<String, String>> mapList = new ArrayList<>();
        if (LanguageUtils.isEn()) {
            Constants.STATUS_EN.keySet().forEach(k -> {
                Map<String, String> map = new HashMap<>(16);
                map.put("key", k);
                map.put("label", Constants.STATUS_EN.get(k));
                mapList.add(map);
            });
        } else {
            Constants.STATUS.keySet().forEach(k -> {
                Map<String, String> map = new HashMap<>(16);
                map.put("key", k);
                map.put("label", Constants.STATUS.get(k));
                mapList.add(map);
            });
        }
        return R.ok(mapList);
    }

    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "标注人员", notes = "标注人员")
    @GetMapping("/personnel")
    public R<List<labelingPersonnelOut>> labelingPersonnel(@NotNull(message = "项目id为空！") @RequestParam(value = "projectId",required = false) @ApiParam(name="projectId",value = "项目id",required = true) Long projectId){
        List<labelingPersonnelOut> projectInfoOut=projectStatisticsService.labelingPersonnel(projectId);
        return R.ok(projectInfoOut);
    }


    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "标签", notes = "标签")
    @GetMapping("/labels")
    public R<List<LabelOut>> labels(@NotNull(message = "项目id为空！") @RequestParam(value = "projectId",required = false) @ApiParam(name="projectId",value = "项目id",required = true) Long projectId){
        List<LabelOut> projectInfoOut=projectStatisticsService.label(projectId);
        return R.ok(projectInfoOut);
    }


    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "图像名称", notes = "图像名称")
    @GetMapping("/projectImage")
    public R<List<ProjectImageOut>> projectImage(@NotNull(message = "项目id为空！") @RequestParam(value = "projectId",required = false) @ApiParam(name="projectId",value = "项目id",required = true) Long projectId){
        List<ProjectImageOut> projectInfoOut=projectStatisticsService.projectImage(projectId);
        return R.ok(projectInfoOut);
    }

    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "单图标签统计")
    @PostMapping("/imageLabel")
    public R<PageMaster<ImageLabelOut>> imageLabel(@Validated @RequestBody ImageLabelIn imageLabelIn) {
        R<PageMaster<ImageLabelOut>> imageLabel = projectStatisticsService.imageLabel(imageLabelIn);
        return imageLabel;
    }

    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "单图标签统计导出")
    @PostMapping("/imageLabelExport")
    public void imageLabelExport(@RequestBody ImageLabelIn imageLabelIn,HttpServletResponse response)throws Exception {
       projectStatisticsService.imageLabelExport(imageLabelIn,response);
    }



    @ApiOperation(value = "多标签统计")
    @PostMapping("/projectLabel")
    public R<List<ProjectLabelOut>> projectLabel(@Validated @RequestBody ProjectLabelIn projectLabelIn) {
        List<ProjectLabelOut> itemList = projectStatisticsService.projectLabel(projectLabelIn);
        return R.ok(itemList);
    }

    @ApiOperation(value = "多用户统计")
    @PostMapping("/projectUser")
    public R<List<ProjectLabelOut>> projectUser(@Validated @RequestBody ProjectLabelIn projectLabelIn) {
        List<ProjectLabelOut> itemList = projectStatisticsService.projectUser(projectLabelIn);
        return R.ok(itemList);
    }

    @ApiOperation(value = "多标签统计导出")
    @PostMapping("/projectLabelExport")
    public void projectLabelExport(@RequestBody ProjectLabelIn projectLabelIn,HttpServletResponse response) throws Exception{
        projectStatisticsService.projectLabelExport(projectLabelIn,response);
    }

    @ApiOperation(value = "多用户统计导出")
    @PostMapping("/projectUserExport")
    public void projectUserExport(@RequestBody ProjectLabelIn projectLabelIn,HttpServletResponse response) throws Exception{
        projectStatisticsService.projectUserExport(projectLabelIn,response);
    }


}
