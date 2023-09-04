package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 查询项目成员VO
 * @author wangfeng
 * @date 2023/03/25 14:20
 */
@Data
public class ProjectMemberSelectVO {
    @NotNull(message = "项目id不可为空 !")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    @ApiModelProperty(value = "用户名", required = false)
    private String userName;

    @ApiModelProperty(value = "角色id", required = false)
    private Long roleId;

}
