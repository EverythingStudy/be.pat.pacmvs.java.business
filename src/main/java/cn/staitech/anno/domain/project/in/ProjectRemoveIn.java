package cn.staitech.anno.domain.project.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author wudi
 * @Date 2023/5/30 15:58
 * @desc 删除项目
 */
@Data
public class ProjectRemoveIn {
    @ApiModelProperty(value = "项目ID")
    @NotNull(message = "项目id不能为空")
    private Long projectId;
}
