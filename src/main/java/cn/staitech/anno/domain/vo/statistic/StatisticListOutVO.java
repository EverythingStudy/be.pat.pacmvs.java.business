package cn.staitech.anno.domain.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StatisticListOutVO {
    
    @ApiModelProperty(value = "统计维度类别")
    private String statisticDimension;
    
    @ApiModelProperty(value = "统计数量类别")
    private String statisticCategory;
    
    @ApiModelProperty(value = "综合统计列表X轴刻度值")
    private Long statisticListXScaleValue;
    
    @ApiModelProperty(value = "统计数据")
    private List<List> statisticList;
    
}
