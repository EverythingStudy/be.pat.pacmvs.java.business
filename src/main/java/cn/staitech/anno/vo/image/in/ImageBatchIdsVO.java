package cn.staitech.anno.vo.image.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangf
 */
@Data
public class ImageBatchIdsVO {
    /**
     * 图像id
     */
    @ApiModelProperty(value = "图像ID", required = true)
    private List<Long> imageIdList;
}
