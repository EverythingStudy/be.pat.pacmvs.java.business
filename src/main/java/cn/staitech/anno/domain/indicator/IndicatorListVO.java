package cn.staitech.anno.domain.indicator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author wangf
 */
@Data
public class IndicatorListVO {

    @ApiModelProperty(value = "页码")
    public int pageNum = 1;
    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;
    @ApiModelProperty(value = "病例指标名称")
    private String indicatorName;
    @ApiModelProperty(value = "种属编码")
    private Integer speciesId;
    @ApiModelProperty(value = "脏器编码")
    private String organId;
    @ApiModelProperty("请求参数（开始和结束时间）")
    private Map<String, Object> createTimeParams;
}
