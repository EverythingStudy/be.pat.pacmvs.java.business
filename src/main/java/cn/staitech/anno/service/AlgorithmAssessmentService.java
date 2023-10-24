package cn.staitech.anno.service;

import cn.staitech.anno.domain.AlgorithmAssessment;
import cn.staitech.anno.domain.assessment.in.*;
import cn.staitech.anno.domain.assessment.out.GetAssessmentListOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

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

    void export(AssessmentExportIN assessmentExportIn) throws Exception;

    boolean zipExport(String zipUrl, Long projectId, String fileUrl) throws Exception;

    R getJsonInfo(GetJsonInfoIn req);
}
