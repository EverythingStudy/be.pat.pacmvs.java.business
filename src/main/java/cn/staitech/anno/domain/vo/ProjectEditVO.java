package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProjectEditVO {

    /**
     * 项目ID
     */
    @NotNull(message = "{PorjectVO.projectId.isnull}")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    /**
     * 病理id
     */
    @ApiModelProperty(value = "病理id", required = true)
    private Long indicatorId;
}
