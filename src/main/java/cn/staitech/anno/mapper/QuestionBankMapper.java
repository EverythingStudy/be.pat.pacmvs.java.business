package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.domain.question.in.GetQuestionListIn;
import cn.staitech.anno.domain.question.in.GetQuestionsIn;
import cn.staitech.anno.domain.question.out.GetProjectBoxOut;
import cn.staitech.anno.domain.question.out.GetQuestionListOut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 题库表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-09-26
 */
public interface QuestionBankMapper extends BaseMapper<QuestionBank> {
    List<GetQuestionListOut> selectQuestionList(GetQuestionListIn getQuestionListIn);

    List<GetProjectBoxOut>  selectProjectList(Long organizationId);

    List<GetQuestionListOut> selectQuestionListExt(GetQuestionsIn getQuestionListIn);
}
