package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gjt.
 * @data 2023/6/5 9:44
 */
@Data
public class SpecialStatusVo {
    /**
     * 主键id .
     */
    @NotNull(message = "{SpecialDeleteVo.specialId.isnull}")
    @ApiModelProperty(value = "专题id", required = true)
    private Long specialId;

    /**
     * 状态 .
     */
    @NotNull(message = "{MarkingJsonIn.status.notNull}")
    @ApiModelProperty(value = "状态", required = true)
    private Long status;

    /**
     * 状态 .
     */
    @ApiModelProperty(value = "权限字符", required = true)
    private String perms;
}
