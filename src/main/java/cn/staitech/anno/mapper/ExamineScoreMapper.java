package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.domain.examineScore.SelectExaminationListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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

}
