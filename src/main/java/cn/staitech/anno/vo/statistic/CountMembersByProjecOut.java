package cn.staitech.anno.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/11/2 17:11
 * @desc
 */
@Data
public class CountMembersByProjecOut {

    private Long markingTotal;
    @ApiModelProperty("未复核标注数")
    private Long notReviewed;
    @ApiModelProperty("已复核标注数")
    private Long reviewed;


}
