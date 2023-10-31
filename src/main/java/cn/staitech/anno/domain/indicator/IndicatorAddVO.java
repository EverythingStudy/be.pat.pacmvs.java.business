package cn.staitech.anno.domain.indicator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加结构指标
 *
 * @author wangf
 */
@Data
public class IndicatorAddVO {
    @ApiModelProperty(value = "种属编号")
    private Long speciesId;
    @ApiModelProperty(value = "种属名称")
    private String speciesName;
    @ApiModelProperty(value = "脏器编号")
    private String organId;
    @ApiModelProperty(value = "脏器名称")
    private String organName;
}
