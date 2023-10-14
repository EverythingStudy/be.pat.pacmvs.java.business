package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.domain.examineScore.ExamineScoreBy;
import cn.staitech.anno.domain.examineScore.ExamineScoreExportVO;
import cn.staitech.anno.domain.examineScore.ExamineSelectVo;
import cn.staitech.anno.domain.examineScore.SelectExaminationListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
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

    List<SelectExaminationListVO> selectQuestionProjectList(Long questionProjectId);

    List<ExamineScoreExportVO> selectLists(@Param("examineScoreIdList") List<Long> examineScoreIdList);

}
