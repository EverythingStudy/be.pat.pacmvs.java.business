package cn.staitech.anno.domain.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 项目Id列表
 *
 * @author wangfeng
 */
@Data
public class StatisticUserListInVO {

    @ApiModelProperty(value = "项目ID列表", required = true)
    private List<Long> projectIdList;
}
