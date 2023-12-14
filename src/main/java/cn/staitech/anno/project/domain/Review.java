package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tb_review
 */
@TableName(value = "tb_review")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long reviewId;
    /**
     * 分值
     */
    @NotNull(message = "{Review.score.isnull}")
    @ApiModelProperty("分值")
    private Long score;
    /**
     * 详情
     */
    @NotBlank(message = "{Review.details.isnull}")
    @Size(max = 200, message = "{PathologicalIndicatorCategory.categoryName.length}")
    @ApiModelProperty("详情")
    @Length(max = 200, message = "{PathologicalIndicatorCategory.categoryName.length}")
    private String details;
    /**
     * 评审人
     */
    @NotBlank(message = "{Review.reviewPeople.isnull}")
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("评审人")
    @Length(max = 255, message = "{projectType.length}")
    private String reviewPeople;
    /**
     * 评审时间
     */
    @NotNull(message = "{Review.reviewTime.isnull}")
    @ApiModelProperty("评审时间")
    private Date reviewTime;
    /**
     * 切片id
     */
    @NotNull(message = "{Review.slideId.isnull}")
    @ApiModelProperty("切片id")
    private Long slideId;
    /**
     * 切片编号
     */
    @ApiModelProperty("切片名称")
    private String slideName;
    /**
     * 评审内容
     */
    @NotBlank(message = "{Review.reviewContent.isnull}")
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("评审内容")
    @Length(max = 255, message = "{projectType.length}")
    private String reviewContent;
    /**
     * 评审伦次
     */
    @NotNull(message = "{Review.reviewRound.isnull}")
    @ApiModelProperty("评审伦次")
    private Long reviewRound;
    /**
     * 专题编号
     */
    @NotBlank(message = "{Review.topicName.isnull}")
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("专题编号")
    @Length(max = 255, message = "{projectType.length}")
    private String topicName;
    /**
     * 专题id
     */
    @NotNull(message = "{Review.topicId.isnull}")
    @ApiModelProperty("专题id")
    private String topicId;
    /**
     * 分组id
     */
    @NotNull(message = "{Review.groupId.isnull}")
    @ApiModelProperty("分组id")
    private Long groupId;
    /**
     * 分组名称
     */
    @NotNull(message = "{Review.groupName.isnull}")
    @ApiModelProperty("分组名称")
    private String groupName;
    /**
     * 创建者名称
     */
    @NotBlank(message = "{Review.createName.isnull}")
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("创建者名称")
    @Length(max = 255, message = "{projectType.length}")
    private String createName;
    /**
     * 创建时间
     */
    @NotNull(message = "{Opt.createTime.isnull}")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-mm-ss HH:mm:ss")
    private String createTime;
    /**
     * 创建者
     */
    @NotNull(message = "{Opt.createBy.isnull}")
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
    @JsonFormat(pattern = "yyyy-mm-ss HH:mm:ss")
    private String updateTime;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Long projectId;
    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;
    /**
     * 轮次id
     */
    @ApiModelProperty("轮次id")
    private Long roundId;
    /**
     * 轮次名称
     */
    @ApiModelProperty("轮次名称")
    private String roundName;
    
    @ApiModelProperty(value = "评审状态 默认1：未审 2：已审")
    private String selfReviewStatus;

}