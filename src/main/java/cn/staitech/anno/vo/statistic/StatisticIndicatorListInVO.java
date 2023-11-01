package cn.staitech.anno.vo.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StatisticIndicatorListInVO {

    @ApiModelProperty(value = "项目ID列表", required = true)
    private List<Long> projectIdList;

    private Long organizationId;


}
