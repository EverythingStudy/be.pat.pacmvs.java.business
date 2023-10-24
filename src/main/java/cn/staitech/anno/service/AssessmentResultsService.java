package cn.staitech.anno.service;

import cn.staitech.anno.domain.AssessmentResults;
import cn.staitech.anno.domain.assessmentResults.AssessmentResultsQueryIn;
import cn.staitech.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
public interface AssessmentResultsService extends IService<AssessmentResults> {

    PageResponse<AssessmentResults> selectPageList(AssessmentResultsQueryIn in);

}
