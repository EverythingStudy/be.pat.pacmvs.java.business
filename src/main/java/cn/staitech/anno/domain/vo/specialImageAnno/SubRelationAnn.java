package cn.staitech.anno.domain.vo.specialImageAnno;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubRelationAnn {

    @ApiModelProperty(hidden = true)
    private Long imageId;

    
    @ApiModelProperty(value = "标注对应annId")
    private Long specialAnnotationId;




}
