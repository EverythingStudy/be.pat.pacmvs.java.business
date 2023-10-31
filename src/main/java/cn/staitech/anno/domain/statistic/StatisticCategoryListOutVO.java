package cn.staitech.anno.domain.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangf
 */
@Data
public class StatisticCategoryListOutVO {

    @ApiModelProperty(value = "标注类别id")
    private Integer categoryId;

    @ApiModelProperty(value = "标注类别名称")
    private String categoryName;

    @ApiModelProperty(required = true, value = "结构ID")
    private String structureId;


    private String color;
    @NotBlank(message = "颜色值不可为空")
    private String rgb;

    @ApiModelProperty(required = true, value = "颜色值HEX")
    private String hex;

    @ApiModelProperty(hidden = true, value = "机构ID")
    private Long organizationId;
    @ApiModelProperty(value = "图层顺序")
    @NotBlank(message = "{StatisticCategoryListOutVO.orderNumber.isnull}")
    private Integer orderNumber;

    @ApiModelProperty(value = "病例指标id")
    private Long indicatorId;

    @ApiModelProperty(value = "病例指标名称")
    private String indicatorName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer sum;
}
