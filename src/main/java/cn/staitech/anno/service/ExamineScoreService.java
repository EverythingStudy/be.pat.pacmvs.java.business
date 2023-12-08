package cn.staitech.anno.service;

import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.vo.examination.SelectExaminationListVO;
import cn.staitech.anno.vo.examine.*;
import cn.staitech.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
public interface ExamineScoreService extends IService<ExamineScore> {

    PageResponse<ExamineScore> selectList(Integer pageSize, Integer pageNum, Long projectId, String nickName, Long examResults);

    void refreshInterval(ExamineScoreExportInsertVo examineScoreExportInsertVo);

    List<ExamineScoreExportVO> selectLists(List<Long> examineScoreIdList);

    List<SelectExaminationListVO> selectExaminationList(Long projectId, String slideNumber);

    SelectExaminationListVO selectExaminationBy(Long questionProjectId);

    int add(ExamineScoreAddVO examineScoreAddVO) throws Exception;

    ExamineScoreBy selectByIds(Long examineScoreId);

    int update(ExamineScoreAddVO examineScoreAddVO) throws Exception;

    void updatePersonalFit(Long examineScoreId);

    /**
     * 人工评分
     * */
    int manualScoring(ExamineScoreUpdateVO examineScoreUpdateVO);

    /**
     * 批量算法评分
     * */
    int BatchAlgorithm(ExamineScoreBathVO examineScoreBathVO);

}
