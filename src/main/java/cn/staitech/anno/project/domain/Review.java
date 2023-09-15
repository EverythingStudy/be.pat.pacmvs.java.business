package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @TableName tb_review
 */
@TableName(value ="tb_review")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long reviewId;

    /**
     * 分值
     */
    @NotNull(message="[分值]不能为空")
    @ApiModelProperty("分值")
    private Long score;
    /**
     * 详情
     */
    @NotBlank(message="[详情]不能为空")
    @Size(max= 200,message="编码长度不能超过200")
    @ApiModelProperty("详情")
    @Length(max= 200,message="编码长度不能超过200")
    private String details;
    /**
     * 评审人
     */
    @NotBlank(message="[评审人]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("评审人")
    @Length(max= 255,message="编码长度不能超过255")
    private String reviewPeople;
    /**
     * 评审时间
     */
    @NotNull(message="[评审时间]不能为空")
    @ApiModelProperty("评审时间")
    private Date reviewTime;
    /**
     * 切片id
     */
    @NotNull(message="[切片id]不能为空")
    @ApiModelProperty("切片id")
    private Long slideId;
    /**
     * 评审内容
     */
    @NotBlank(message="[评审内容]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("评审内容")
    @Length(max= 255,message="编码长度不能超过255")
    private String reviewContent;
    /**
     * 评审伦次
     */
    @NotNull(message="[评审伦次]不能为空")
    @ApiModelProperty("评审伦次")
    private Long reviewRound;
    /**
     * 专题编号
     */
    @NotBlank(message="[专题编号]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("专题编号")
    @Length(max= 255,message="编码长度不能超过255")
    private String specialNumber;
    /**
     * 分组id
     */
    @NotNull(message="[分组id]不能为空")
    @ApiModelProperty("分组id")
    private Long groupId;
    /**
     * 创建者名称
     */
    @NotBlank(message="[创建者名称]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("创建者名称")
    @Length(max= 255,message="编码长度不能超过255")
    private String createName;
    /**
     * 创建时间
     */
    @NotNull(message="[创建时间]不能为空")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 创建者
     */
    @NotNull(message="[创建者]不能为空")
    @ApiModelProperty("创建者")
    private Long createBy;
    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private Long updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Long projectId;

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
        Review other = (Review) that;
        return (this.getReviewId() == null ? other.getReviewId() == null : this.getReviewId().equals(other.getReviewId()))
            && (this.getScore() == null ? other.getScore() == null : this.getScore().equals(other.getScore()))
            && (this.getDetails() == null ? other.getDetails() == null : this.getDetails().equals(other.getDetails()))
            && (this.getReviewPeople() == null ? other.getReviewPeople() == null : this.getReviewPeople().equals(other.getReviewPeople()))
            && (this.getReviewTime() == null ? other.getReviewTime() == null : this.getReviewTime().equals(other.getReviewTime()))
            && (this.getSlideId() == null ? other.getSlideId() == null : this.getSlideId().equals(other.getSlideId()))
            && (this.getReviewContent() == null ? other.getReviewContent() == null : this.getReviewContent().equals(other.getReviewContent()))
            && (this.getReviewRound() == null ? other.getReviewRound() == null : this.getReviewRound().equals(other.getReviewRound()))
            && (this.getSpecialNumber() == null ? other.getSpecialNumber() == null : this.getSpecialNumber().equals(other.getSpecialNumber()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getCreateName() == null ? other.getCreateName() == null : this.getCreateName().equals(other.getCreateName()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getReviewId() == null) ? 0 : getReviewId().hashCode());
        result = prime * result + ((getScore() == null) ? 0 : getScore().hashCode());
        result = prime * result + ((getDetails() == null) ? 0 : getDetails().hashCode());
        result = prime * result + ((getReviewPeople() == null) ? 0 : getReviewPeople().hashCode());
        result = prime * result + ((getReviewTime() == null) ? 0 : getReviewTime().hashCode());
        result = prime * result + ((getSlideId() == null) ? 0 : getSlideId().hashCode());
        result = prime * result + ((getReviewContent() == null) ? 0 : getReviewContent().hashCode());
        result = prime * result + ((getReviewRound() == null) ? 0 : getReviewRound().hashCode());
        result = prime * result + ((getSpecialNumber() == null) ? 0 : getSpecialNumber().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getCreateName() == null) ? 0 : getCreateName().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", reviewId=").append(reviewId);
        sb.append(", score=").append(score);
        sb.append(", details=").append(details);
        sb.append(", reviewPeople=").append(reviewPeople);
        sb.append(", reviewTime=").append(reviewTime);
        sb.append(", slideId=").append(slideId);
        sb.append(", reviewContent=").append(reviewContent);
        sb.append(", reviewRound=").append(reviewRound);
        sb.append(", specialNumber=").append(specialNumber);
        sb.append(", groupId=").append(groupId);
        sb.append(", createName=").append(createName);
        sb.append(", createTime=").append(createTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", projectId=").append(projectId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}