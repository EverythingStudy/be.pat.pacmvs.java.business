package cn.staitech.anno.service;

import cn.staitech.anno.domain.ExamineScore;
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

    List<ExamineScore> selectLists(Long projectId,List<Long> examineScoreIdList);

}
