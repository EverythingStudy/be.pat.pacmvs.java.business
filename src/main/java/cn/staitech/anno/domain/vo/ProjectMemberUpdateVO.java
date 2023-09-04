package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 项目成员修改VO
 * @author 王峰
 * @date 2023/03/25 15:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProjectMemberUpdateVO {

    @NotNull(message = "项目id不可为空")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;
    @NotNull(message = "用户id不可为空")
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;
    @NotBlank(message = "角色ID不可为空")
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;
}
