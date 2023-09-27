package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * 添加标签VO
 * @author wangf
 */
@Data
public class PathologicalIndicatorCategoryVO {
    @ApiModelProperty(required = true, value = "结构ID")
    @NotBlank(message = "结构ID不能为空!")
    private String structureId;

    @NotBlank(message = "颜色值不可为空")
    @ApiModelProperty(required = true, value = "颜色RBG值")
    private String rgb;

    @NotBlank(message = "颜色值不可为空")
    @ApiModelProperty(required = true, value = "颜色值HEX")
    private String hex;

    @NotNull(message = "病理指标不能为空")
    @ApiModelProperty(required = true, value = "结构指标ID")
    private Long indicatorId;

    @ApiModelProperty(value = "图层顺序")
    @NotBlank(message = "图层顺序不可为空!")
    private String orderNumber;
}
