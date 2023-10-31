package cn.staitech.anno.domain.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticProjectListOutVO {

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "病理指标ID")
    private Long indicatorId;

}
