package cn.staitech.anno.domain.vo.slideVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-21 19:33:13
 * @Description:
 */
@Data
public class AddSlideVO {
    @ApiModelProperty(value = "图像ID", required = true)
    private List<Long> topicIds;

    @ApiModelProperty(value = "修改人", required = false, hidden = true)
    private Long projectId;
}
