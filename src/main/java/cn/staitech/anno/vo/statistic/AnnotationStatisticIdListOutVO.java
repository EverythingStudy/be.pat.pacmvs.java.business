package cn.staitech.anno.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnnotationStatisticIdListOutVO {

    @ApiModelProperty(value = "统计维度ID")
    private Long statisticId;

    @ApiModelProperty(value = "统计维度名称")
    private String statisticName;
}
