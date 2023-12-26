package cn.staitech.anno.controller;

import cn.staitech.anno.constant.Container;
import cn.staitech.anno.service.LabelStatisticsService;
import cn.staitech.anno.utils.*;
import cn.staitech.anno.vo.image.out.ImageListOutVO;
import cn.staitech.anno.vo.labelprojectstatistics.*;
import cn.staitech.anno.vo.project.ProjectDelVO;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.system.api.domain.SysUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "标签、项目统计", tags = "标签、项目统计")
@RestController
@RequestMapping("/labelProjectStatistics")
public class LabelStatisticsController {

    @Resource
    private LabelStatisticsService labelStatisticsService;



    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "下拉框---项目列表")
    @PostMapping("/projectList")
    public R<List<ProjectListOut>> projectLists(){
        List<ProjectListOut> projectList=labelStatisticsService.projectList();
        return R.ok(projectList);
    }

    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "下拉框---标签集")
    @PostMapping("/labelSet")
    public R<List<LabelSetOut>> labelSet(@RequestBody LabelSetIn labelSetIn){
        List<LabelSetOut> labelSetOuts=labelStatisticsService.projectLabelSet(labelSetIn);
        return R.ok(labelSetOuts);
    }

    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "下拉框---标签")
    @PostMapping("/label")
    public R<List<LabelOut>> label(@RequestBody LabelIn labelIn){
            List<LabelOut> labelOuts=labelStatisticsService.labelList(labelIn);
        return R.ok(labelOuts);
    }


    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "标签统计")
    @PostMapping("/projectLabel")
    public R<PageMaster<ProjectLabelOut>> projectLabel(@RequestBody ProjectLabelIn projectLabelIn){
        List<ProjectLabelOut> projectLabelOuts=labelStatisticsService.projectLabelList(projectLabelIn);
        ProjectDelVO projectDelVO=ProjectUtils.pagingLabel(projectLabelIn);
        int pageSize = projectDelVO.getPageSize();
        int pageNum = projectDelVO.getPageNum();
        boolean flag1 = projectDelVO.getFlag();
        List<ProjectLabelOut> result = projectDelVO.getResultList();
        for (int i = pageNum * pageSize; i < pageNum * pageSize + pageSize; i++) {
            if (i <  projectLabelOuts.size()) {
                result.add( projectLabelOuts.get(i));
            }
        }
        PageMaster<ProjectLabelOut> pageMaster = new PageMaster<>(result);
        if (flag1) {
            pageNum++;
        }
        pageMaster.setPageNum(pageNum);
        pageMaster.setPageSize(pageSize);
        pageMaster.setTotal(projectLabelOuts.size());
        return R.ok(pageMaster);

    }


    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "标签统计导出")
    @PostMapping("/labelExport")
    public void export(@RequestBody ProjectLabelIn projectLabelIn,HttpServletResponse response) throws Exception {
        labelStatisticsService.labelExport(projectLabelIn,response);
    }


    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "下拉框---创建者")
    @PostMapping("/userList")
    public R<List<ProjectCreateByOut>> userList(@RequestBody LabelSetIn labelSetIn){
        List<ProjectCreateByOut> userList=labelStatisticsService.userList(labelSetIn);
       return R.ok(userList);
    }


    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "项目状态列表", notes = "项目状态列表")
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


    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "项目统计")
    @PostMapping("/projectStatistics")
    public R<PageMaster<ProjectLabelOut>> projectStatistics(@RequestBody ProjectListIn projectListIn){
        R<PageMaster<ProjectLabelOut>> itemList=labelStatisticsService.itemList(projectListIn);
        return itemList;
    }

    @ApiOperationSupport(author = "ZMJ")
    @ApiOperation(value = "项目统计导出")
    @PostMapping("/projectExport")
    public void projectExport(@RequestBody ProjectListIn projectListIn, HttpServletResponse response) throws Exception {
        labelStatisticsService.projectExport(projectListIn,response);
    }


}
