package cn.staitech.anno.domain.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gjt.
 * @data 2023/6/1 10:43
 */
@Data
public class SpecialRoleUserVo {

    @NotNull(message = "{SpecialRoleUserVo.roleId.isnull}")
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @NotNull(message = "{SpecialRoleUserVo.userId,isnull}")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @NotNull(message = "{SpecialRoleUserSelectVo.specialId.isnull}")
    @ApiModelProperty(value = "专题id")
    private Long specialId;
}
