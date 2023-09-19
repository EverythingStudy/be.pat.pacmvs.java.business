package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author mugw
 * @version 1.0
 * @description 评审
 * @date 2023/9/15 13:11:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewVO {
    @NotBlank(message="[详情]不能为空")
    @ApiModelProperty("详情")
    private String details;
    @NotNull(message="[分值]不能为空")
    @ApiModelProperty("分值")
    private Long score;
    @ApiModelProperty("创建者名称")
    private String createName;
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 评审人
     */
    @NotBlank(message="[评审人]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("评审人")
    @Length(max= 255,message="编码长度不能超过255")
    private String reviewPeople;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("切片编号")
    private String imageCode;

    @ApiModelProperty("评审内容")
    private String content;
    @ApiModelProperty("专题编号")
    private String topicName;
    @ApiModelProperty("组别编号")
    private String groupName;
    @ApiModelProperty("轮次")
    private String roundName;
    @ApiModelProperty("评审内容")
    private String reviewContent;
    /**
     * 切片id
     */
    @NotNull(message="[切片id]不能为空")
    @ApiModelProperty("切片id")
    private Long slideId;
}
