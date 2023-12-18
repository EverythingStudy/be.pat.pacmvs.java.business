package cn.staitech.anno.vo.indicator;

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
    private String speciesId;
    @ApiModelProperty(value = "种属名称")
    private String speciesName;
    @ApiModelProperty(value = "脏器编号")
    private String organId;
    @ApiModelProperty(value = "脏器名称")
    private String organName;
    @ApiModelProperty(hidden = true, value = "标签类型 0:下拉筛选标签；1:自定义标签")
    private Integer indicatorType;
}
