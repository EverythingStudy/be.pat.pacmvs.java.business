package cn.staitech.anno.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProjectDescription {

    /**
     * 切片id .
     */
    @NotNull(message = "{PorjectVO.projectId.isnull}")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    /**
     * 描述 .
     */
    @Size(min = 0, max = 50, message = "{SlideUpdateVO.description.length}")
    @ApiModelProperty(value = "描述")
    private String description;

}
