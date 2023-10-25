package cn.staitech.anno.domain.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialAnnotation
 * @Description:
 * @date 2023年6月12日
 */
@Api(value = "专题选片-标注结果", tags = "专题选片-标注结果")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialAnnotation {

    @ApiModelProperty(name = "sliceAnnotationId", value = "标注ID")
    private Long sliceAnnotationId;

    @ApiModelProperty(name = "annotationId", value = "标注id")
    private String annotationId;

    @ApiModelProperty(name = "specialImageId", value = "专题选片ID")
    private Long specialImageId;

    @ApiModelProperty(name = "specialId", value = "专题ID")
    private Long specialId;

    @ApiModelProperty(name = "imageId", value = "图像ID")
    private Long imageId;

    @ApiModelProperty(name = "area", value = "面积字符串")
    private String area;

    @ApiModelProperty(name = "measure", value = "面积")
    private BigDecimal measure;

    @ApiModelProperty(name = "perimeter", value = "周长")
    private BigDecimal perimeter;

    @ApiModelProperty(name = "categoryId", value = "标注类别id")
    private Integer categoryId;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    @ApiModelProperty(name = "annotationType", value = "标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)")
    private String annotationType;

    @ApiModelProperty(name = "measureFullName", value = "标注名称")
    private String measureFullName;

    @ApiModelProperty(name = "measureType", value = "测量轮廓类型(0:正常,表示有关系,默认为0")
    private Integer measureType;

    @ApiModelProperty(name = "measureRelation", value = "测量关系")
    private String measureRelation;

    @ApiModelProperty(name = "measureName", value = "测量轮廓表示名称:L'")
    private String measureName;

    @ApiModelProperty(name = "measureNumber", value = "测量轮廓标识：1")
    private Integer measureNumber;

    @ApiModelProperty(name = "radius", value = "周长（圆）")
    private String radius;

    @ApiModelProperty(name = "meanDistance", value = "平均间距")
    private Double meanDistance;

    @ApiModelProperty(name = "maxDistance", value = "最大间距")
    private Double maxDistance;

    @ApiModelProperty(name = "minDistance", value = "最小间距")
    private Double minDistance;

    @ApiModelProperty(name = "innerAngle", value = "内角")
    private String innerAngle;

    @ApiModelProperty(name = "exteriorAngle", value = "外角")
    private String exteriorAngle;

    @ApiModelProperty(name = "centerPoint", value = "中心")
    private String centerPoint;

    @ApiModelProperty(name = "pointCount", value = "不同标签点的总数")
    private Integer pointCount;

    @ApiModelProperty(name = "locationType", value = "标注类型")
    private String locationType;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "更新人")
    private Long updateBy;


    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(name = "innerAngle", value = "内角")
    private String location;

    @ApiModelProperty(name = "geometry", value = "新标注数据")
    private String geometry;

    @ApiModelProperty(name = "OldAnnotationId", value = "OldAnnotationId")
    private Long OldAnnotationId;
}
