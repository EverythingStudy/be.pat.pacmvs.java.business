package cn.staitech.anno.domain.vo.measurevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MeasureAddVO {

    @NotNull(message = "切片信息不可为空")
    @ApiModelProperty(value = "切片id", required = true)
    private Long slideId;

    /**
     * 位置 .
     */
    @NotNull(message = "测量信息不可为空")
    @ApiModelProperty(value = "标注地方位置图形数据", required = true)
    private String location;

    /**
     * 标注类型 .
     */
    @ApiModelProperty(value = "标注地方位置图形数据类型")
    private String locationType;


    /**
     * 位置 .
     */
    @ApiModelProperty(value = "测量编号-字母")
    private String measureName;

    /**
     * 测量类型 .
     */
    @ApiModelProperty(value = "测量类型", required = true)
    private Integer measureType;

    /**
     * 圆心 .
     */
    @ApiModelProperty(value = "圆心")
    private String centerPoint;

    /**
     * 半径 .
     */
    @ApiModelProperty(value = "半径")
    private String radius;

    /**
     * 被测量图形A .
     */
    @ApiModelProperty(value = "被测量图形A")
    private Long maIdA;

    /**
     * 被测量图形B .
     */
    @ApiModelProperty(value = "被测量图形B")
    private Long maIdB;

    /**
     * 内角 .
     */
    @ApiModelProperty(value = "内角")
    private String innerAngle;

    /**
     * 外角 .
     */
    @ApiModelProperty(value = "外角")
    private String exteriorAngle;

    /**
     * 平均间距 .
     */
    @ApiModelProperty(value = "平均间距")
    private String meanDistance;

    /**
     * 最小间距 .
     */
    @ApiModelProperty(value = "最小间距")
    private String minDistance;

    /**
     * 最大间距 .
     */
    @ApiModelProperty(value = "最大间距")
    private String maxDistance;

    /**
     * 权限标识 .
     */
    @ApiModelProperty(value = "权限标识", required = true)
    private String permission;
}
