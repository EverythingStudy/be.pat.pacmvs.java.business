package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ReviewUP {

    /**
     * 详情
     */
    @NotBlank(message="[详情]不能为空")
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("详情")
    @Length(max= 100,message="编码长度不能超过100")
    private String details;
    /**
     * 分数
     */
    @NotNull(message="[分数]不能为空")
    @ApiModelProperty("分数")
    private Long score;

    /**
     * 分数
     */
    @NotNull(message="[评审id]不能为空")
    @ApiModelProperty("评审id")
    private Long reviewId;
}
