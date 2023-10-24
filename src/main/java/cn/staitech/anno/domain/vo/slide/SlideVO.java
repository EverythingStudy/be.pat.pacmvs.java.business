package cn.staitech.anno.domain.vo.slide;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SlideVO {

    /**
     * 切片id
     */
    @ApiModelProperty(required = true, value = "切片id")
    private Long[] slideId;
}
