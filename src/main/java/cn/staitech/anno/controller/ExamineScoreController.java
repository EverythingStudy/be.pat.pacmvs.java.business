package cn.staitech.anno.controller;


import cn.staitech.anno.constant.ExamineScoreConstant;
import cn.staitech.anno.constant.ExportConstant;
import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.examineScore.ExamineScoreAddVO;
import cn.staitech.anno.domain.examineScore.ExamineScoreExportVO;
import cn.staitech.anno.domain.examineScore.ExamineScoreExportInsertVo;
import cn.staitech.anno.domain.examineScore.SelectExaminationListVO;
import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.service.ProjectService;
import cn.staitech.anno.service.ExamineScoreService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.staitech.anno.aspect.LogFileAspect.response;

/**
 * @author gjt
 * @since 2023-09-25
 */
@Api(value = "标注考核-评分列表", tags = "标注考核-评分列表")
@RestController
@RequestMapping("/examineScore")
public class ExamineScoreController {

    @Resource
    private ExamineScoreService examineScoreService;

    @Resource
    private ProjectService projectService;


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "考题列表查询")
    @GetMapping("/selectExaminationList")
    public R<List<SelectExaminationListVO>> selectExaminationList(
            @NotNull(message = "参数异常,未传入项目id") @RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId,
            @RequestParam(value = "imageName", required = false) @ApiParam(name = "imageName", value = "切片编号") String imageName) throws Exception {
        return R.ok(examineScoreService.selectExaminationList(projectId, imageName));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "查询考题详情")
    @GetMapping("/selectExaminationBy")
    public R<SelectExaminationListVO> selectExaminationBy(
            @NotNull(message = "参数异常,未传入项目题库id") @RequestParam(value = "questionProjectId") @ApiParam(name = "questionProjectId", value = "项目题库id", required = true) Long questionProjectId) {
        return R.ok(examineScoreService.selectExaminationBy(questionProjectId));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "评分列表查询")
    @GetMapping("/selectListBy")
    public R<PageResponse<ExamineScore>> selectListBy(
            @NotNull(message = "参数异常,未传入项目id") @RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId,
            @NotNull(message = "参数异常,未发现分页信息") @RequestParam(value = "pageSize") @ApiParam(name = "pageSize", value = "当前页数", required = true) Integer pageSize,
            @NotNull(message = "参数异常,未发现分页信息") @RequestParam(value = "pageNum") @ApiParam(name = "pageNum", value = "每页数量", required = true) Integer pageNum,
            @RequestParam(value = "nickName", required = false) @ApiParam(name = "nickName", value = "答题者") String nickName,
            @RequestParam(value = "examResults", required = false) @ApiParam(name = "examResults", value = "考试结果") Long examResults) throws Exception {
        return R.ok(examineScoreService.selectList(pageSize, pageNum, projectId, nickName, examResults));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "刷新拟合区间")
    @PostMapping("/refreshInterval")
    public R<String> refreshInterval(@RequestBody ExamineScoreExportInsertVo examineScoreExportInsertVo) throws Exception {
        // 调用python
        return R.ok("操作成功");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "添加考核信息")
    @PostMapping("/add")
    public R<String> add(@RequestBody ExamineScoreAddVO examineScoreAddVO) throws Exception {
        examineScoreService.add(examineScoreAddVO);
        return R.ok("操作成功");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "完成考核信息")
    @PutMapping("/update")
    public R<String> update(@RequestBody ExamineScoreAddVO examineScoreAddVO) throws Exception {
        examineScoreService.update(examineScoreAddVO);
        return R.ok("操作成功");
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "导出标注数据")
    @PostMapping("/export")
    public void export(@RequestBody ExamineScoreExportInsertVo examineScoreExportInsertVo
    ) throws Exception {
        // 查询考核评分列表
        List<ExamineScoreExportVO> examineScoreList = examineScoreService.selectLists(examineScoreExportInsertVo.getExamineScoreIdList());
        // 查询项目中得信息
        Project projectBy = projectService.getById(examineScoreExportInsertVo.getProjectId());
        String projectName = "";
        if (projectBy != null) {
            projectName = projectBy.getProjectName();
        }
        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(ExamineScoreConstant.COLHEAD_KEY, ExamineScoreConstant.COLHEAD_VALUE);
        ExcelTool excelTool = new ExcelTool<>(ExportConstant.EXCEL_TITLE, 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(projectName, "UTF-8") + ExportConstant.XLSX);
        excelTool.exportExcel(titleData, examineScoreList, response.getOutputStream(), true, false);
    }


    public List<Map<String, String>> getTitleList(String[] colHeadKey, String[] colHeadValue) {
        // 定义表头
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < colHeadKey.length; i++) {
            Map<String, String> map = new HashMap<String, String>(1);
            map.put(colHeadKey[i], colHeadValue[i]);
            list.add(map);
        }
        return list;
    }


}

