package cn.staitech.anno.domain.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StatisticSlideListInVO {

    @ApiModelProperty(value = "项目ID列表", required = true)
    private List<Long> projectIdList;
    @JsonIgnore
    private Long organizationId;
}
