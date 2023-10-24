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
     * 分数
     */
    @NotNull(message = "{ReviewUP.reviewId.isnull}")
    @ApiModelProperty("评审id")
    private Long reviewId;
}
