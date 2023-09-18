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
     * 切片编号
     */
    @ApiModelProperty("切片名称")
    private String slideName;
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
    private String topicName;
    /**
     * 专题id
     */
    @NotNull(message="[专题id]不能为空")
    @ApiModelProperty("专题id")
    private String topicId;
    /**
     * 分组id
     */
    @NotNull(message="[分组id]不能为空")
    @ApiModelProperty("分组id")
    private Long groupId;
    /**
     * 分组名称
     */
    @NotNull(message="[分组名称]不能为空")
    @ApiModelProperty("分组名称")
    private String groupName;
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}