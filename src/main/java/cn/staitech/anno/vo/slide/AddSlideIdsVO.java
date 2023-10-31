package cn.staitech.anno.vo.slide;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-21 19:33:13
 * @Description:
 */
@Data
public class AddSlideIdsVO {
    @ApiModelProperty(value = "图像ID", required = true)
    private List<Long> imageIds;

    @ApiModelProperty(value = "项目ID", required = true, hidden = true)
    private Long projectId;

    @ApiModelProperty(value = "评审轮次ID", required = false, hidden = true)
    private Long reviewRoundId;
}
