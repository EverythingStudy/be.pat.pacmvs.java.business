package cn.staitech.anno.domain.vo.specialImageAnno.in;

import cn.staitech.anno.domain.geojson.in.viewAddIn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SpecialAnnAddIn extends viewAddIn {
    //slide_id 相当于我的special_image_id
    //marking_id相当于我的slice_annotation_id
    @ApiModelProperty(name = "special_id", value = "专题ID")
    private Long special_id;

    @ApiModelProperty(name = "image_id", value = "图像ID")
    private Long image_id;
}
