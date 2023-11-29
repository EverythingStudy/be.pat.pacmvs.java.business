package cn.staitech.anno.service;

import cn.staitech.anno.vo.algorithm.AlgorithmAssessment;
import cn.staitech.anno.vo.assessment.in.*;
import cn.staitech.anno.vo.assessment.out.GetAssessmentListOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
public interface AlgorithmAssessmentService extends IService<AlgorithmAssessment> {

    R createAssessment(CreateAssessmentIn req);

    PageResponse<GetAssessmentListOut> getAssessmentList(GetAssessmentListIn req);

    R removeAssessment(RemoveAssessmentIn req);

    void export(AssessmentExportIn assessmentExportIn) throws Exception;

    List<String> zipExport(String zipUrl, Long projectId, String fileUrl, String roundId) throws Exception;

    R getJsonInfo(GetJsonInfoIn req);
}
