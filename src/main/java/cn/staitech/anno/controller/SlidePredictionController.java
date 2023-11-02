package cn.staitech.anno.controller;


import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import cn.staitech.anno.service.AlgorithmPredictionService;
import cn.staitech.anno.vo.assessment.in.AssessmentExportIn;
import cn.staitech.anno.vo.assessment.in.CreateAssessmentIn;
import cn.staitech.anno.vo.assessment.in.GetAssessmentListIn;
import cn.staitech.anno.vo.assessment.in.GetJsonInfoIn;
import cn.staitech.anno.vo.assessment.in.RemoveAssessmentIn;
import cn.staitech.anno.vo.assessment.out.GetAssessmentListOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 眼科切片预测表 前端控制器
 * </p>
 *
 * @author wanglibei
 * @since 2023-11-02
 */
@RestController
@RequestMapping("/slidePrediction")
public class SlidePredictionController {
	@Resource
	private AlgorithmPredictionService algorithmPredictionService;

	@ApiOperation(value = "生成算法考核数据-根据项目")
	@PostMapping("/createQuestion")
	public R createAssessment(@Validated @RequestBody CreateAssessmentIn req) {
		return algorithmPredictionService.createAssessment(req);
	}

	@ApiOperation(value = "算法考核列表分页查询")
	@PostMapping("/getQuestionList")
	public R<PageResponse<GetAssessmentListOut>> getAssessmentList(@Validated @RequestBody GetAssessmentListIn req) {
		PageResponse<GetAssessmentListOut> resp = algorithmPredictionService.getAssessmentList(req);
		return R.ok(resp);
	}

	@ApiOperation(value = "算法考核-删除")
	@PostMapping("/removeAssessment")
	public R removeAssessment(@Validated @RequestBody RemoveAssessmentIn req) {
		return algorithmPredictionService.removeAssessment(req);
	}

	@ApiOperation(value = "算法考核-获取json;路径：/home/pat_saas/Upload")
	@PostMapping("/getJsonInfo")
	public R getJsonInfo(@Validated @RequestBody GetJsonInfoIn req) {
		return algorithmPredictionService.getJsonInfo(req);
	}

	@ApiOperationSupport(author = "gjt")
	@ApiOperation(value = "导出考核结果")
	@PostMapping("/export")
	public void export(@RequestBody AssessmentExportIn assessmentExportIn
			) throws Exception {
		algorithmPredictionService.export(assessmentExportIn);
	}
}

