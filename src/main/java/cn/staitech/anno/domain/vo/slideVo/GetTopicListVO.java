package cn.staitech.anno.domain.vo.slideVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wangfeng
 * @create: 2023-09-21 19:33:13
 * @Description:
 */
@Data
public class GetTopicListVO {
    @ApiModelProperty(value = "项目ID", required = true, hidden = true)
    private Long projectId;

    @ApiModelProperty(value = "评审轮次ID", required = false, hidden = true)
    private Long reviewRoundId;

}
