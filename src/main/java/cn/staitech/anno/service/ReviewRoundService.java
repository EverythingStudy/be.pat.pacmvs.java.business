package cn.staitech.anno.service;

import cn.staitech.anno.domain.ReviewRound;
import cn.staitech.anno.domain.reviewround.ReviewRoundBatchInVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wangf
 * @description 针对表【tb_review_round】的数据库操作Service
 * @createDate 2023-09-18 16:15:33
 */
public interface ReviewRoundService extends IService<ReviewRound> {
    /**
     * 批量添评审轮次
     *
     * @param reviewRoundBatchInVO
     * @return
     */
    boolean saveBatchByList(ReviewRoundBatchInVO reviewRoundBatchInVO);
}
