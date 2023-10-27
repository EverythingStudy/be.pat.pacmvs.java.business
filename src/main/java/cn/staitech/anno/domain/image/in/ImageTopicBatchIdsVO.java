package cn.staitech.anno.domain.image.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author wangfeng
 */
@Data
public class ImageTopicBatchIdsVO {
    /**
     * 图像id
     */
    @ApiModelProperty(value = "图像ID", required = true)
    private List<Long> imageIdList;

    @ApiModelProperty(value = "所属专题-ID")
    private Long topicId;

    @Size(min = 1, max = 100, message = "{icBatchIdsVO.topicName.length}")
    @ApiModelProperty(value = "所属专题-专题名称")
    private String topicName;

    @ApiModelProperty(value = "修改人", required = false, hidden = true)
    private Long updateBy;
}
