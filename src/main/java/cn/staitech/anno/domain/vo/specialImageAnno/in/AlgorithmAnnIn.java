package cn.staitech.anno.domain.vo.specialImageAnno.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AlgorithmAnnIn {
    @ApiModelProperty(name = "specialId", value = "专题ID")
    private Long specialId;

    @ApiModelProperty(name = "specialImageId", value = "专题切图ID")
    private Long specialImageId;

    @ApiModelProperty(name = "imageId", value = "图像ID")
    private Long imageId;

    @ApiModelProperty(name = "geometryList", value = "标注信息")
    private List<AlgorithmGeometry> geometryList;
}
