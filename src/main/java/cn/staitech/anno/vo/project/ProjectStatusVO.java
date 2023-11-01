package cn.staitech.anno.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 项目状态
 *
 * @author wangf
 */
@Data
public class ProjectStatusVO {
    @NotNull(message = "{PorjectVO.projectId.isnull}")
    @ApiModelProperty(value = "项目ID", required = true)
    private Long projectId;
    @ApiModelProperty(value = "项目状态")
    private String status;
    @ApiModelProperty(hidden = true, value = "更新者id")
    private Long updateBy;
}
