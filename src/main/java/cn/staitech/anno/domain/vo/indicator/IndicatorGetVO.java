package cn.staitech.anno.domain.vo.indicator;

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

    private String indicatorName;

    private Integer indicatorId;

    @ApiModelProperty(value = "种属编号")
    private Long speciesId;
}
