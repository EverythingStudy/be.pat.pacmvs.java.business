package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.vo.examination.ExaminationInVO;
import cn.staitech.anno.vo.examination.SelectExaminationListVO;
import cn.staitech.anno.vo.examine.ExamineScoreBy;
import cn.staitech.anno.vo.examine.ExamineScoreExportVO;
import cn.staitech.anno.vo.examine.ExamineSelectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
public interface ExamineScoreMapper extends BaseMapper<ExamineScore> {

    List<SelectExaminationListVO> selectExaminationList(QuestionBank questionBank);

    List<ExamineScore> selectExamineList(ExamineSelectVo examineSelectVo);

    SelectExaminationListVO selectExaminationBy(ExamineScore examineScore);

    ExamineScoreBy selectByIds(Long examineScoreId);

    SelectExaminationListVO selectQuestionProject(Long questionProjectId);

    List<SelectExaminationListVO> selectQuestionProjectList(ExaminationInVO examinationInVO);

    List<ExamineScoreExportVO> selectLists(@Param("examineScoreIdList") List<Long> examineScoreIdList);

    /**
     * 人工评分
     */
    int manualScoring(ExamineScore examineScore);

}
