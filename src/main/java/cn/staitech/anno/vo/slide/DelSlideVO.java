package cn.staitech.anno.vo.slide;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wangfeng
 * @create: 2023-09-21 19:33:13
 * @Description:
 */
@Data
public class DelSlideVO {
    @ApiModelProperty(value = "项目ID")
    private Long projectId;
    @ApiModelProperty(value = "评审轮次ID")
    private Long reviewRoundId;
}
