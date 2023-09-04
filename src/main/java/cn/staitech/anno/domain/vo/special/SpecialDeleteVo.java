package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gjt.
 * @data 2023/5/31 17:15
 */
@Data
public class SpecialDeleteVo {

    /**
     * 主键id .
     */
    @NotNull(message = "专题信息不可为空")
    @ApiModelProperty(value = "专题id", required = true)
    private Long specialId;

    /**
     * 主键id .
     */
    @NotNull(message = "状态不可为空")
    @ApiModelProperty(value = "状态", required = true)
    private Long delFlag;

    /**
     * 状态 .
     */
    @ApiModelProperty(value = "权限字符", required = true)
    private String perms;
}
