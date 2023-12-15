package cn.staitech.anno.vo.slide;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: wangfeng
 * @create: 2023-11-10 17:52:04
 * @Description: 拼接Viewer列表查询条件
 */
@Data
public class SlideIdVO {

    @NotNull(message = "{SlideIdVO.slideId.NotNull}")
    @ApiModelProperty(value = "切片ID")
    private Long slideId;
}

