package cn.staitech.anno.vo.projectmember;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 项目成员修改VO
 *
 * @author 王峰
 * @date 2023/03/25 15:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProjectMemberUpdateVO {

    @NotNull(message = "{PorjectVO.projectId.isnull}")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;
    @NotNull(message = "{ProjectMemberUpdateVO.userId.isnull}")
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;
    @NotBlank(message = "{ProjectMemberUpdateVO.roleId.isnull}")
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;
}
