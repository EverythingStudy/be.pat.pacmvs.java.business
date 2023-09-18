package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_review_round
 */
@TableName(value ="tb_review_round")
@Data
public class ReviewRound implements Serializable {
    /**
     * 评审轮次自增ID
     */
    @TableId(type = IdType.AUTO)
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ReviewRound other = (ReviewRound) that;
        return (this.getReviewRoundId() == null ? other.getReviewRoundId() == null : this.getReviewRoundId().equals(other.getReviewRoundId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getReviewContent() == null ? other.getReviewContent() == null : this.getReviewContent().equals(other.getReviewContent()))
            && (this.getRoundId() == null ? other.getRoundId() == null : this.getRoundId().equals(other.getRoundId()))
            && (this.getTopicId() == null ? other.getTopicId() == null : this.getTopicId().equals(other.getTopicId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getOrganizationId() == null ? other.getOrganizationId() == null : this.getOrganizationId().equals(other.getOrganizationId()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getReviewRoundId() == null) ? 0 : getReviewRoundId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getReviewContent() == null) ? 0 : getReviewContent().hashCode());
        result = prime * result + ((getRoundId() == null) ? 0 : getRoundId().hashCode());
        result = prime * result + ((getTopicId() == null) ? 0 : getTopicId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", reviewRoundId=").append(reviewRoundId);
        sb.append(", projectId=").append(projectId);
        sb.append(", reviewContent=").append(reviewContent);
        sb.append(", roundId=").append(roundId);
        sb.append(", topicId=").append(topicId);
        sb.append(", groupId=").append(groupId);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}