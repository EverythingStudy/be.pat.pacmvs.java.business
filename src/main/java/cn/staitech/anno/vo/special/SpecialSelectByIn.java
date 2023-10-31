package cn.staitech.anno.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SpecialSelectByIn {

    @NotNull(message = "{SpecialRoleUserVo.userId.isnull}")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @NotNull(message = "{SpecialSelectByIn.specialId.isnull}")
    @ApiModelProperty(value = "角色id")
    private Long specialId;
}
