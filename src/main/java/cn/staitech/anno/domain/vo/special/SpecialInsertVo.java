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
    @NotBlank(message = "{SpecialInsertVo.specialNumber.isnull}")
    @Size(min = 0, max = 100, message = "{SpecialInsertVo.specialNumber.length}")
    @ApiModelProperty(value = "专题编号")
    private String specialNumber;

    /**
     * 专题名称 .
     */
    @NotBlank(message = "{SpecialInsertVo.specialName.isnull}")
    @Size(min = 0, max = 100, message = "{ImageTopicBatchIdsVO.topicName.length}")
    @ApiModelProperty(value = "专题名称")
    private String specialName;

    /**
     * 种属
     */
    @NotBlank(message = "{SpecialInsertVo.species.isnull}")
    @ApiModelProperty(value = "种属")
    private String species;

    /**
     * 试验类型
     */
    @NotBlank(message = "{SpecialInsertVo.trialType.isnull}")
    @ApiModelProperty(value = "试验类型")
    private String trialType;

    /**
     * 染色类型 .
     */
    @NotBlank(message = "{SpecialInsertVo.stainType.isnull}")
    @ApiModelProperty(value = "染色类型")
    private String stainType;

    /**
     * 病理指标id
     */
    @NotNull(message = "{SpecialInsertVo.indicatorId.isnull}")
    @ApiModelProperty(value = "病理指标id")
    private Long indicatorId;

}
