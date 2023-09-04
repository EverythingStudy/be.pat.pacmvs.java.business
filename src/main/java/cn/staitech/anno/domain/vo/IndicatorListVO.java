package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class IndicatorListVO {

    @ApiModelProperty(value = "病例指标名称")
    private String indicatorName;

    @ApiModelProperty("请求参数（开始和结束时间）")
    private Map<String, Object> params;

    @ApiModelProperty(value = "页码")
    public int pageNum = 1;

    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;

}
