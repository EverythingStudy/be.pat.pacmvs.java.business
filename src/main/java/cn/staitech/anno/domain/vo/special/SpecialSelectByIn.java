package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SpecialSelectByIn {

    @NotNull(message = "用户不可为空")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @NotNull(message = "专题id")
    @ApiModelProperty(value = "角色id")
    private Long specialId;
}
