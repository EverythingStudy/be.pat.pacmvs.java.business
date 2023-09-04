package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VisceraVO {
    @ApiModelProperty(value = "脏器组织id")
    private Long dictCode;

    @ApiModelProperty(value = "脏器组织名称")
    private String dictLabel;
}
