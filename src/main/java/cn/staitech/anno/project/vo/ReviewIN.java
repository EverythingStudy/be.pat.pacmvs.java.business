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
    /**
     * 详情
     */
    @NotBlank(message = "{Review.details.isnull}")
    @Size(max = 100, message = "{PathologicalIndicatorCategory.structureId.length}")
    @ApiModelProperty("详情")
    @Length(max = 100, message = "{PathologicalIndicatorCategory.structureId.length}")
    private String details;
    /**
     * 分数
     */
    @NotNull(message = "{ReviewIN.score.isnull}")
    @ApiModelProperty("分数")
    private Long score;
    /**
     * 切片id
     */
    @NotNull(message = "{Review.slideId.isnull}")
    @ApiModelProperty("切片id")
    private Long slideId;
}
