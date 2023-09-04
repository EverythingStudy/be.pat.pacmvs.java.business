package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author admin
 */
@Data
public class DescriptionUpdateVO {
    
    /**
     * 标注id .
     */
    @NotNull(message = "标注id不可为空 !")
    @ApiModelProperty(value = "标注id", required = true)
    private Long annotationId;
    
    /**
     * 描述 .
     */
    @Size(min = 0, max = 50, message = "标注描述不可超过50字段")
    @ApiModelProperty(value = "描述")
    private String description;
    
}
