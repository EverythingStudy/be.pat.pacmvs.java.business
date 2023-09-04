package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnnotationVO {
    
    /**
     * 标注id
     */
    @NotNull(message = "标注不可为空!")
    @ApiModelProperty(required = true, value = "标注id")
    private Long annotationId;

    /**
     * ROI正方形标注边长512或1024;默认512.
     */
    @NotNull(message = "distance不可为空!")
    @ApiModelProperty(required = true, value = "ROI正方形标注边长512或1024，默认512")
    private int distance = 512 ;
}
