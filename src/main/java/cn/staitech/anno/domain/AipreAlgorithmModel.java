package cn.staitech.anno.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * aipre_algorithm_model
 * @author 
 */
@Data
public class AipreAlgorithmModel implements Serializable {
    /**
     * 算法模型id
     */
    private Long modelId;

    /**
     * 算法模型名称
     */
    private String modelName;

    /**
     * 算法模型名称en
     */
    private String modelNameEn;

    /**
     * 病例组织id
     */
    private Long tissueId;

    private static final long serialVersionUID = 1L;
}