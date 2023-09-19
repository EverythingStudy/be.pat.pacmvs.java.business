package cn.staitech.anno.domain.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StatisticCategoryListInVO {
    
    @ApiModelProperty(value = "病理指标ID列表", required = true)
    private List<Long> indicatorIdList;
    
    @ApiModelProperty(value = "项目ID列表", required = true)
    private List<Long> projectIdList;

    private Long organizationId;
    
}
