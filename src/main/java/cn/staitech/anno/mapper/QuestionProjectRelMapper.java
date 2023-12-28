package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.QuestionProjectRel;
import cn.staitech.anno.vo.question.out.GetQuestionListOut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 题库项目表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-09-26
 */
public interface QuestionProjectRelMapper extends BaseMapper<QuestionProjectRel> {

    List<GetQuestionListOut> selectListByProject(Long projectId);

    int insertShouldMarks(@Param("projectId") Long projectId, @Param("shouldMarks") Long shouldMarks);

    int updateShouldMarks(@Param("projectId") Long projectId, @Param("shouldMarks") Long shouldMarks);

    int countShouldMarks(Long projectId);

    Long selectShouldMarks(Long projectId);

    /**
     * 批量添加考核选片
     */
    int examineInsert(List<QuestionProjectRel> questionProjectRelList);
}
