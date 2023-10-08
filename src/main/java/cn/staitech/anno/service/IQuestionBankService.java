package cn.staitech.anno.service;

import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.domain.question.in.ConfirmSelectionIn;
import cn.staitech.anno.domain.question.in.CreateBySlideIn;
import cn.staitech.anno.domain.question.in.CreateQuestionIn;
import cn.staitech.anno.domain.question.in.GetQuestionListIn;
import cn.staitech.anno.domain.question.in.GetQuestionsIn;
import cn.staitech.anno.domain.question.in.SettingCompletedIn;
import cn.staitech.anno.domain.question.out.GetProjectBoxOut;
import cn.staitech.anno.domain.question.out.GetQuestionListOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 题库表 服务类
 * </p>
 *
 * @author author
 * @since 2023-09-26
 */
public interface IQuestionBankService extends IService<QuestionBank> {

    R createQuestion(CreateQuestionIn req);

    R createBySlide(CreateBySlideIn req);

    PageResponse<GetQuestionListOut> getQuestionList(GetQuestionListIn req);

    R<List<GetProjectBoxOut>> getProjectBox();

    List<GetQuestionListOut> getQuestionListExt(GetQuestionsIn req);

    List<GetQuestionListOut> getQuestionByProject(Long projectId);

    R confirmSelection(ConfirmSelectionIn req);

    R settingCompleted(SettingCompletedIn req);
}
