package cn.staitech.anno.vo.slide;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SlideRes {

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "图片名称")
    private String imageName;
}
