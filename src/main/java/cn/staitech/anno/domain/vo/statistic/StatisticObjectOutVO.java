package cn.staitech.anno.domain.vo.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticObjectOutVO {

    @ApiModelProperty(value = "统计维度ID")
    private Long statisticId;

    @ApiModelProperty(value = "统计维度名称")
    private String statisticName;

    @ApiModelProperty(value = "统计维度:日期")
    private String statisticDate;

    @ApiModelProperty(value = "统计数量")
    private Long statisticCount;
}
