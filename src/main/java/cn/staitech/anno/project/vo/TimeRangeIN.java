package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/19 11:21:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeRangeIN {
    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date  endTime;
}
