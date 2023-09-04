package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gjt.
 * @data 2023/6/1 14:16
 */
@Data
public class SpecialRoleUserStatusVo extends SpecialRoleUserVo {

    @NotNull(message = "=状态不可为空")
    @ApiModelProperty(value = "状态")
    private Long status;

}
