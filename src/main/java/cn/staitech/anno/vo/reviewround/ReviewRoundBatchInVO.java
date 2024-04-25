package cn.staitech.anno.vo.reviewround;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.Size;

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
    @Size(min = 0, max = 50, message = "{ReviewRoundInVO.reviewContent.length}")
    private String reviewContent;
    /**
     * 评审内容id
     */
    private String contentId;

    private List<ReviewRoundInsertInVO> insertList;
}