package cn.staitech.anno.vo.reviewround;

import lombok.Data;

/**
 * @author wangf
 * @TableName tb_review_round
 */
@Data
public class ReviewRoundInVO {
    /**
     * 评审轮次自增ID
     */
    private Long reviewRoundId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 评审内容
     */
    private String reviewContent;

    /**
     * 评审轮次ID、对应1至10轮
     */
    private Long roundId;

    /**
     * 专题ID
     */
    private Long topicId;

    /**
     * 组别ID、对应group1至8
     */
    private Long groupId;

    /**
     * 机构ID
     */
    private Long organizationId;
}