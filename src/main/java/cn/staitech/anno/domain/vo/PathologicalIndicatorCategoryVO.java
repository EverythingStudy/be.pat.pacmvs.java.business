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
    @NotBlank(message = "{PathologicalIndicatorCategoryVO.structureId.isnull}")
    private String structureId;

    @NotBlank(message = "{StatisticCategoryListOutVO.rgb.isnull}")
    @ApiModelProperty(required = true, value = "颜色RBG值")
    private String rgb;

    @NotBlank(message = "{StatisticCategoryListOutVO.rgb.isnull}")
    @ApiModelProperty(required = true, value = "颜色值HEX")
    private String hex;

    @NotNull(message = "{PathologicalIndicatorCategoryVO.indicatorId.isnull}")
    @ApiModelProperty(required = true, value = "结构指标ID")
    private Long indicatorId;

    @ApiModelProperty(value = "图层顺序")
    @NotNull(message = "{StatisticCategoryListOutVO.orderNumber.isnull}")
    private Integer orderNumber;
}
