package cn.staitech.anno.controller;


import cn.staitech.anno.domain.assessment.in.*;
import cn.staitech.anno.domain.assessment.out.GetAssessmentListOut;
import cn.staitech.anno.service.AlgorithmAssessmentService;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
@Api(value = "算法考核", tags = "算法考核-切片列表")
@RestController
@RequestMapping("/algorithmAssessment")
public class AlgorithmAssessmentController {
    @Resource
    private AlgorithmAssessmentService algorithmAssessmentService;

    @ApiOperation(value = "生成算法考核数据-根据项目")
    @PostMapping("/createQuestion")
    public R createAssessment(@Validated @RequestBody CreateAssessmentIn req) {
        return algorithmAssessmentService.createAssessment(req);
    }

    @ApiOperation(value = "算法考核列表分页查询")
    @PostMapping("/getQuestionList")
    public R<PageResponse<GetAssessmentListOut>> getAssessmentList(@Validated @RequestBody GetAssessmentListIn req) {
        PageResponse<GetAssessmentListOut> resp = algorithmAssessmentService.getAssessmentList(req);
        return R.ok(resp);
    }

    @ApiOperation(value = "算法考核-删除")
    @PostMapping("/removeAssessment")
    public R removeAssessment(@Validated @RequestBody RemoveAssessmentIn req) {
        return algorithmAssessmentService.removeAssessment(req);
    }

    @ApiOperation(value = "算法考核-获取json;路径：/home/pat_saas/Upload")
    @PostMapping("/getJsonInfo")
    public R getJsonInfo(@Validated @RequestBody GetJsonInfoIn req) {
        return algorithmAssessmentService.getJsonInfo(req);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "导出考核结果")
    @PostMapping("/export")
    public void export(@RequestBody AssessmentExportIn assessmentExportIn
    ) throws Exception {
        algorithmAssessmentService.export(assessmentExportIn);
    }
}
