package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AssessmentResults;
import cn.staitech.anno.domain.assessment.in.AssessmentExportIN;
import cn.staitech.anno.domain.assessment.out.AssessmentExportOut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
public interface AssessmentResultsMapper extends BaseMapper<AssessmentResults> {

    List<AssessmentExportOut> selectExportList(AssessmentExportIN assessmentExportIN);

}
