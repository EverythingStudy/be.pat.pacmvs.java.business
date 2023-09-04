package cn.staitech.anno.domain.vo.measurevo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangf
 */
@Data
public class MeasureSelectVO {


    /**
     * 测量自增id .
     */
    @ApiModelProperty(value = "测量自增id")
    private Long measureId;

    /**
     * 项目id .
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 图像id .
     */
    @ApiModelProperty(value = "图像id")
    private Long imageId;

    /**
     * 切片id .
     */
    @ApiModelProperty(value = "切片id")
    private Long slideId;

    /**
     * 被测量图像A .
     */
    @ApiModelProperty(value = "被测量图像A")
    private Long maIdA;

    /**
     * 被测量图像B .
     */
    @ApiModelProperty(value = "被测量图像B")
    private Long maIdB;

    /**
     * 测量编号 .
     */
    @ApiModelProperty(value = "测量编号-字母")
    private String measureName;
    /**
     * 测量编号 .
     */
    @ApiModelProperty(value = "测量编号-数字")
    private Long measureNum;

    /**
     * 标注类型 .
     */
    @ApiModelProperty(value = "标注类型")
    private String locationType;

    /**
     * 标注地方位置图形数据 .
     */
    @ApiModelProperty(value = "标注地方位置图形数据")
    private String location;

    /**
     * 周长 .
     */
    @ApiModelProperty(value = "周长")
    private String perimeter;

    /**
     * 面积 .
     */
    @ApiModelProperty(value = "面积")
    private String area;

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
     * 圆半径
     */
    private String radius;

    /**
     * 圆心
     */
    private String centerPoint;

    /**
     * 创建人id .
     */
    private Long createBy;

    /**
     * 创建时间 .
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**
     * 更新者 .
     */
    private Long updateBy;

    /**
     * 更新时间 .
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    /**
     * 创建者名称 .
     */
    private String userName;

    /**
     * 点统计 .
     */
    private Long pointCount;

    /**
     * 测量类型 .
     */
    private Integer measureType;
}
