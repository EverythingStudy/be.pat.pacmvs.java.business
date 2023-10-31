package cn.staitech.anno.vo.specialimageanno.in;

import cn.staitech.anno.vo.geojson.Properties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SpecialAnnoProperties extends Properties {
    @ApiModelProperty(name = "special_id", value = "专题ID")
    private Long special_id;

    @ApiModelProperty(name = "image_id", value = "图像ID")
    private Long image_id;

    @ApiModelProperty(name = "slice_annotation_id", value = "slice_annotation_id")
    private Long slice_annotation_id;

    @ApiModelProperty(name = "annotation_id", value = "annotation_id")
    private String annotation_id;

    @ApiModelProperty(name = "special_image_id", value = "special_image_id")
    private Long special_image_id;

    @ApiModelProperty(name = "location", value = "location")
    private String location;

//	@ApiModelProperty(name = "create_by", value = "create_by")
//	private Long create_by;

    @ApiModelProperty(name = "geometry", value = "geometry")
    private String geometry;


}
