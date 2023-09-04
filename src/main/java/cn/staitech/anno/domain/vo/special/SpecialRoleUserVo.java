package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gjt.
 * @data 2023/6/1 10:43
 */
@Data
public class SpecialRoleUserVo {

    @NotNull(message = "角色不可为空")
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @NotNull(message = "用户不可为空")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @NotNull(message = "专题不可为空")
    @ApiModelProperty(value = "专题id")
    private Long specialId;
}
