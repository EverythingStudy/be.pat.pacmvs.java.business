package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class singleRemoveVO {
    @NotNull(message = "{SpecialRemoveVO.annotationId.isnull}")
    @ApiModelProperty(value = "标注id", required = true)
    private Long annotationId;

    @ApiModelProperty(value = "权限字符")
    private String permission;

}
