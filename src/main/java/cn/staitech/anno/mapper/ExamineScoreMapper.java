package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.domain.examineScore.ExamineScoreExportVO;
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

    SelectExaminationListVO selectExaminationBy(@Param("questionProjectId") Long questionProjectId);

    List<ExamineScoreExportVO> selectLists(@Param("examineScoreIdList") List<Long> examineScoreIdList);

}
