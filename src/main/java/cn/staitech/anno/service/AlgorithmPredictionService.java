package cn.staitech.anno.service;

import java.util.List;

import cn.staitech.anno.vo.assessment.in.AssessmentExportIn;
import cn.staitech.anno.vo.assessment.in.CreateAssessmentIn;
import cn.staitech.anno.vo.assessment.in.GetAssessmentListIn;
import cn.staitech.anno.vo.assessment.in.GetJsonInfoIn;
import cn.staitech.anno.vo.assessment.in.RemoveAssessmentIn;
import cn.staitech.anno.vo.assessment.out.GetAssessmentListOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;

/**
 * 
* @ClassName: AlgorithmPredictionService
* @Description:算法预测
* @author wanglibei
* @date 2023年11月2日
* @version V1.0
 */
public interface AlgorithmPredictionService{

    R createAssessment(CreateAssessmentIn req);

    PageResponse<GetAssessmentListOut> getAssessmentList(GetAssessmentListIn req);

    R removeAssessment(RemoveAssessmentIn req);

    void export(AssessmentExportIn assessmentExportIn) throws Exception;

    List<String> zipExport(String zipUrl, Long projectId, String fileUrl) throws Exception;

    R getJsonInfo(GetJsonInfoIn req);
}
