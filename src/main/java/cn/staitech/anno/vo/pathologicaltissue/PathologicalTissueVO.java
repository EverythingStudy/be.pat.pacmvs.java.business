package cn.staitech.anno.vo.pathologicaltissue;

import cn.staitech.anno.domain.AlgorithmModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PathologicalTissueVO {
    /**
     * 病理组织id
     */
    @ApiModelProperty(value = "病理组织id")
    private Long tissueId;

    /**
     * 病理组织名称
     */
    @ApiModelProperty(value = "病理组织名称")
    private String tissueName;

    /**
     * 病理组织英文名称
     */
    @ApiModelProperty(value = "病理组织英文名称")
    private String tissueNameEn;

    /**
     * 项目类型id
     */
    @ApiModelProperty(value = "项目类型id")
    private Long projectTypeId;

    @ApiModelProperty(value = "算法模型")
    private List<AlgorithmModel> models;

}
