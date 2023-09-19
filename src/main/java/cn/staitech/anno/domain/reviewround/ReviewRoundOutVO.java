package cn.staitech.anno.domain.reviewround;

import cn.staitech.anno.domain.Pager;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangf
 * @TableName tb_review_round
 */
@Data
public class ReviewRoundOutVO extends Pager implements Serializable {
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

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}