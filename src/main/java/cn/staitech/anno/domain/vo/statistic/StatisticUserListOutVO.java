package cn.staitech.anno.domain.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticUserListOutVO {
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;
}
