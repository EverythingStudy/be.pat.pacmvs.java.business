package cn.staitech.anno.domain;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * aipre_algorithm_model
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlgorithmModel implements Serializable {
    /**
     * 算法模型id
     */
    @ApiModelProperty(value = "id")
    private Long modelId;

    /**
     * 算法模型名称
     */
    @ApiModelProperty(value = "名称")
    private String modelName;

    /**
     * 算法模型名称en
     */
    @ApiModelProperty(value = "名称en")
    private String modelNameEn;

    /**
     * 病例组织id
     */
    private Long tissueId;

    private static final long serialVersionUID = 1L;
}