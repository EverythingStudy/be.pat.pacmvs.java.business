package cn.staitech.anno.vo.indicator;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangf
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IndicatorGetVO {
    @ApiModelProperty(required = true, value = "病理指标ID")
    private Integer indicatorId;
    @ApiModelProperty(required = true, value = "病理指标名称")
    private String indicatorName;
    @ApiModelProperty(value = "种属编号")
    private Long speciesId;
}
