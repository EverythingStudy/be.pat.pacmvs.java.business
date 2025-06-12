package cn.staitech.annotation.vo.anno;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @author mugw
 * @version 1.0
 * @description 轮廓间距
 * @date 2025/5/28 09:13:09
 */
@Data
public class AnnotationDistanceVo {

    @ApiModelProperty(value = "轮廓点一")
    @JsonProperty("contourTypeOne")
    private Geometry pointOne;

    @ApiModelProperty(value = "轮廓点二")
    @JsonProperty("contourTypeTwo")
    private Geometry pointTwo;

    @ApiModelProperty(value = "平均间距")
    private Double meanDistance;

    @ApiModelProperty(value = "最小间距")
    private Double minDistance;
}
