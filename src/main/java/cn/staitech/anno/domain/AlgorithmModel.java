package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * aipre_algorithm_model
 *
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("aipre_algorithm_model")
@Builder
@ApiModel(value = "眼科-算法模型表", description = "眼科-算法模型表")
public class AlgorithmModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 算法模型id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "model_id", type = IdType.AUTO)
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
}