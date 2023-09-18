package cn.staitech.anno.domain.vo.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SlideFileName {

    @ApiModelProperty(value = "专题名称")
    private String topicName;

    @ApiModelProperty(value = "切片名称")
    private String imageName;

    @ApiModelProperty(value = "切片类型")
    private String slideType;

    @ApiModelProperty(value = "结构编码")
    private String categoryNumber;
}
