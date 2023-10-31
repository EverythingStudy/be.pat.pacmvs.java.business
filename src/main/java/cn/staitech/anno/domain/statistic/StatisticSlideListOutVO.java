package cn.staitech.anno.domain.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticSlideListOutVO {

    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    @ApiModelProperty(value = "切片名称")
    private String slideName;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;
}
