package cn.staitech.anno.domain;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 标注测量表
 * </p>
 *
 * @author wanglibei
 * @since 2023-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_mark_measure")
@ApiModel(value="MarkMeasure对象", description="标注测量表")
public class MarkMeasure implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键自增id")
    @TableId(value = "mark_measure_id", type = IdType.AUTO)
    private String markMeasureId;

    @ApiModelProperty(value = "标注id")
    private String annotationId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "面积")
    private String area;

    @ApiModelProperty(value = "周长")
    private String perimeter;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "标签id")
    private Long categoryId;

    @ApiModelProperty(value = "标注名称")
    private Long number;

    @ApiModelProperty(value = "测量轮廓类型(0:正常,表示有关系,默认为0")
    private Integer measureType;

    @ApiModelProperty(value = "测量关系")
    private String measureRelation;

    @ApiModelProperty(value = "测量轮廓表示名称:L")
    private String measureName;

    @ApiModelProperty(value = "测量轮廓标识：1")
    private Integer measureNumber;

    @ApiModelProperty(value = "平均间距")
    private Double meanDistance;

    @ApiModelProperty(value = "最大间距")
    private Double maxDistance;

    @ApiModelProperty(value = "最小间距")
    private Double minDistance;

    @ApiModelProperty(value = "内角")
    private String innerAngle;

    @ApiModelProperty(value = "外角")
    private String exteriorAngle;

    @ApiModelProperty(value = "中心")
    private String centerPoint;

    @ApiModelProperty(value = "标注数据类型(LineString,Polygon,point,pc,p,L)")
    private String locationType;

    @ApiModelProperty(value = "不同标签点的总数")
    private Integer pointCount;

    @ApiModelProperty(value = "周长（圆）")
    private String radius;

    @ApiModelProperty(value = "标注数据")
    private JSONObject geometry;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "标注绘制者")
    private String annotationOwner;

    @ApiModelProperty(value = "标注更新者")
    private String annotationUpdateOwner;

    @ApiModelProperty(value = "机构ID")
    private Long organizationId;
    
    @ApiModelProperty(value = "标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)")
    private String annotationType;


}
