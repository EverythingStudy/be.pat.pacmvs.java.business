package cn.staitech.anno.vo.markMeasure;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@Accessors(chain = true)
@TableName(value = "tb_mark_measure", autoResultMap = true)
@ApiModel(value = "MarkMeasure对象", description = "标注测量表")
public class MarkMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键自增id")
    @TableId(value = "mark_measure_id", type = IdType.INPUT)
    private String mark_measure_id;

    @ApiModelProperty(value = "标注id")
    private String annotation_id;

    @ApiModelProperty(value = "标注id-多的赋值")
    @TableField(exist = false)
    private String marking_id;

    @ApiModelProperty(value = "项目id")
    private Long project_id;

    @ApiModelProperty(value = "切片id")
    private Long slide_id;

    @ApiModelProperty(value = "面积")
    private String area;

    @ApiModelProperty(value = "周长")
    private String perimeter;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "标签id")
    private Long category_id;

    @ApiModelProperty(value = "标注名称")
    private Long number;

    @ApiModelProperty(value = "测量轮廓类型0:正常,表示有关系,默认为0")
    private Integer measure_type;

    @ApiModelProperty(value = "测量关系")
    private String measure_relation;

    @ApiModelProperty(value = "测量轮廓表示名称:L")
    private String measure_name;

    @ApiModelProperty(value = "测量轮廓标识：1")
    private Integer measure_number;

    @ApiModelProperty(value = "平均间距")
    private Double mean_distance;

    @ApiModelProperty(value = "最大间距")
    private Double max_distance;

    @ApiModelProperty(value = "最小间距")
    private Double min_distance;

    @ApiModelProperty(value = "内角")
    private String inner_angle;

    @ApiModelProperty(value = "外角")
    private String exterior_angle;

    @ApiModelProperty(value = "中心")
    private String center_point;

    @ApiModelProperty(value = "标注数据类型(LineString,Polygon,point,pc,p,L)")
    private String location_type;

    @ApiModelProperty(value = "不同标签点的总数")
    private Integer point_count;

    @ApiModelProperty(value = "周长（圆）")
    private String radius;

    @ApiModelProperty(value = "标注数据")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONObject geometry;

    @ApiModelProperty(value = "创建者")
    private Long create_by;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date create_time;

    @ApiModelProperty(value = "更新者")
    private Long update_by;

    @ApiModelProperty(value = "更新时间")
    private Date update_time;

    @ApiModelProperty(value = "标注绘制者")
    private String annotation_owner;

    @ApiModelProperty(value = "标注更新者")
    private String annotation_update_owner;

    @ApiModelProperty(value = "机构ID")
    private Long organization_id;

    @ApiModelProperty(value = "注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)")
    private String annotation_type;


}
