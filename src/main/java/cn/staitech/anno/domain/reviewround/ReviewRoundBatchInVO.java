package cn.staitech.anno.domain.reviewround;

import lombok.Data;

import java.util.List;

/**
 * @author wangf
 * @TableName tb_review_round
 */
@Data
public class ReviewRoundBatchInVO {
    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 评审内容
     */
    private String reviewContent;

    private List<ReviewRoundInsertInVO> insertList;
}