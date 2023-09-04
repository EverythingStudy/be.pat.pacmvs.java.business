package cn.staitech.anno.domain.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticIndicatorListOutVO {
    
    @ApiModelProperty(value = "病例指标id")
    private Integer indicatorId;
    
    @ApiModelProperty(value = "病例指标名称")
    private String indicatorName;
    
}
