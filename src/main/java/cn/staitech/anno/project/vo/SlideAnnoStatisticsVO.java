package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/14 16:30:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideAnnoStatisticsVO {

    @ApiModelProperty("统计类型")
    private String statisticsType;

    @ApiModelProperty("统计结果")
    private Integer result;
}
