package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnnoCategoryVO {
    
    @NotNull(message = "{SpecialRemoveVO.annotationId.isnull}")
    @ApiModelProperty(value = "标注不可为空", required = true)
    private Long annotationId;
    
    @ApiModelProperty(value = "标注类别不可为空", required = true)
    private Long categoryId;
    
    @ApiModelProperty(value = "权限字符")
    private String permission;
    
    
}
