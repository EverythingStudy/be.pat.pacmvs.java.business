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
public class ReviewIN {
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
     * 切片id
     */
    @NotNull(message="[切片id]不能为空")
    @ApiModelProperty("切片id")
    private Long slideId;
}
