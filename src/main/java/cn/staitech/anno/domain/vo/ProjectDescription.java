package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProjectDescription {
    
    /**
     * 切片id .
     */
    @NotNull(message = "项目id不可为空 !")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;
    
    /**
     * 描述 .
     */
    @Size(min = 0, max = 50, message = "描述不可超过50字段")
    @ApiModelProperty(value = "描述")
    private String description;
    
}
