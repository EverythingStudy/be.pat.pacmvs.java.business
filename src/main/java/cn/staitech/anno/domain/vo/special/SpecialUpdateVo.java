package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gjt.
 * @data 2023/5/29 11:19
 */
@Data
public class SpecialUpdateVo extends SpecialInsertVo {

    /**
     * 主键id .
     */
    @NotNull(message = "专题信息不可为空")
    @ApiModelProperty(value = "专题id", required = true)
    private Long specialId;
}
