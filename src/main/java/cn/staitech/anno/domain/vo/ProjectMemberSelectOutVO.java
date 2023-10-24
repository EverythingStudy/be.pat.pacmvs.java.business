package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目成员列表VO
 *
 * @author 王峰
 * @date 2023/03/25 14:24
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProjectMemberSelectOutVO {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "用户名称")
    private String userName;
}
