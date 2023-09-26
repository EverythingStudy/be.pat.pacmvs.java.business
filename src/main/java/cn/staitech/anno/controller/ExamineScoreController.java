package cn.staitech.anno.controller;


import cn.staitech.anno.constant.ExamineScoreConstant;
import cn.staitech.anno.constant.ExportConstant;
import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.service.ExamineScoreService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/examineScore")
public class ExamineScoreController {

    @Resource
    private ExamineScoreService examineScoreService;

    // 添加考核评分表中

    // 根据项目查询评分列表接口(搜索条件（答题者，考试结果）翻页查询)



    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注列表")
    @GetMapping("/selectListBy")
    public R<PageResponse<ExamineScore>> selectListBy(@NotNull(message = "参数异常,未传入项目id") @RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId, @NotNull(message = "参数异常,未发现分页信息") @RequestParam(value = "pageSize") @ApiParam(name = "pageSize", value = "当前页数", required = true) Integer pageSize, @NotNull(message = "参数异常,未发现分页信息") @RequestParam(value = "pageNum") @ApiParam(name = "pageNum", value = "每页数量", required = true) Integer pageNum, @RequestParam(value = "nickName") @ApiParam(name = "nickName", value = "答题者") String nickName, @RequestParam(value = "examResults") @ApiParam(name = "examResults", value = "考试结果") Long examResults) throws Exception {
        return R.ok(examineScoreService.selectList(pageSize, pageNum, projectId, nickName, examResults));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注列表")
    @GetMapping("/export")
    public void export(@NotNull(message = "参数异常,未传入项目id") @RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId, @RequestParam(value = "examineScoreIdList") @ApiParam(name = "examineScoreIdList", value = "审核评分id列表") List<Long> examineScoreIdList) throws Exception {
        // 查询考核评分列表
        List<ExamineScore> examineScoreList = examineScoreService.selectLists(projectId, examineScoreIdList);

        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(ExamineScoreConstant.COLHEAD_KEY, ExamineScoreConstant.COLHEAD_VALUE);
        ExcelTool excelTool = new ExcelTool<>(ExportConstant.EXCEL_TITLE, 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("项目名称", "UTF-8") + ExportConstant.XLSX);
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

