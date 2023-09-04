package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProjectStatusVO {

    /**
     * 项目id .
     */
    @NotNull(message = "项目id不可为空 !")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    /**
     * 项目状态 .
     */
//    @Size(min = 0, max = 50, message = "不可超过50字段")
    @ApiModelProperty(value = "项目状态")
    private String projectStatus;

    @ApiModelProperty(hidden = true,value = "更新者id")
    private Long updateBy;
}
