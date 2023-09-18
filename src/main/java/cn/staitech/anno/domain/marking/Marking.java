package cn.staitech.anno.domain.marking;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author gjt
 * @since 2023-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_marking", autoResultMap = true)
public class Marking implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增id
     */
    @TableId(value = "marking_id", type = IdType.AUTO)
    private Long marking_id;

    /**
     * 标注id
     */
    private String annotation_id;

    /**
     * 面积
     */
    private String area;

    /**
     * 周长
     */
    private String perimeter;

    /**
     * 描述
     */
    private String description;

    /**
     * 标签id
     */
    private Long category_id;

    /**
     * 不同标注标注序号
     */
    private Integer number;

    /**
     * 测量轮廓类型(0:正常,表示有关系,默认为0")
     */
    private Integer measure_type;

    /**
     * 测量关系
     */
    private String measure_relation;

    /**
     * 测量轮廓表示名称:L
     */
    private String measure_name;

    /**
     * 测量轮廓标识：1
     */
    private Integer measure_number;

    /**
     * 周长（圆）
     */
    private String radius;

    /**
     * 平均间距
     */
    private Double mean_distance;

    /**
     * 最大间距
     */
    private Double max_distance;

    /**
     * 最小间距
     */
    private Double min_distance;

    /**
     * 内角
     */
    private String inner_angle;

    /**
     * 外角
     */
    private String exterior_angle;

    /**
     * 创建者
     */
    private Long create_by;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date create_time;

    /**
     * 标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)
     */
    private String annotation_type;

    /**
     * 标注数据类型(LineString,Polygon,point,pc,p,L)
     */
    private String location_type;

    /**
     * 切片id
     */
    private Long slide_id;

    /**
     * 中心
     */
    private String center_point;

    /**
     * 不同标签点的总数
     */
    private Long point_count;

    /**
     * 标注数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONObject geometry;

    /**
     * 更新者
     */
    private Long update_by;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date update_time;


    /**
     * 标注作者
     */
    private String annotation_owner;

    /**
     * 标注更新者
     */
    private String annotation_update_owner;


}
