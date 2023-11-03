package cn.staitech.anno.vo.reviewround;

import lombok.Data;

/**
 * @author wangf
 * @TableName tb_review_round
 */
@Data
public class ReviewRoundInsertInVO {
    /**
     * 评审轮次ID
     */
    private Long roundId;

    /**
     * 专题ID
     */
    private Long topicId;

    /**
     * 组别ID
     */
    private Long groupId;
}