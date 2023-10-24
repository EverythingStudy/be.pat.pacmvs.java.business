package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tb_review_round
 */
@TableName(value = "tb_review_round")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRound implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 评审轮次自增ID
     */
    @TableId(value = "review_round_id", type = IdType.AUTO)
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
     * 内容唯一键
     */
    private String contentId;
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
}