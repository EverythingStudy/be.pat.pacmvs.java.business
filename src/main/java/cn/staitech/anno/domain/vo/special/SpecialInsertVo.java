package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author gjt.
 * @data 2023/5/29 10:05
 */
@Data
public class SpecialInsertVo {

    /**
     * 专题编号 .
     */
    @NotBlank(message = "专题编号不能为空")
    @Size(min = 0, max = 100, message = "专题编号长度不能超过100个字符")
    @ApiModelProperty(value = "专题编号")
    private String specialNumber;

    /**
     * 专题名称 .
     */
    @NotBlank(message = "专题名称不能为空")
    @Size(min = 0, max = 100, message = "专题名称长度不能超过100个字符")
    @ApiModelProperty(value = "专题名称")
    private String specialName;

    /**
     * 种属
     */
    @NotBlank(message = "种属不可为空")
    @ApiModelProperty(value = "种属")
    private String species;

    /**
     * 试验类型
     */
    @NotBlank(message = "试验类型不可为空")
    @ApiModelProperty(value = "试验类型")
    private String trialType;

    /**
     * 染色类型 .
     */
    @NotBlank(message = "染色类型不可为空")
    @ApiModelProperty(value = "染色类型")
    private String stainType;

    /**
     * 病理指标id
     */
    @NotNull(message = "病理指标不可为空")
    @ApiModelProperty(value = "病理指标id")
    private Long indicatorId;

}
