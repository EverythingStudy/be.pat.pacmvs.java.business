package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
    private Long markingId;

    /**
     * 标注id
     */
    private String annotationId;

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
    private Integer categoryId;

    /**
     * 不同标注标注序号
     */
    private Long number;

    /**
     * 测量轮廓类型(0:正常,表示有关系,默认为0")
     */
    private Integer measureType;

    /**
     * 测量关系
     */
    private String measureRelation;

    /**
     * 测量轮廓表示名称:L
     */
    private String measureName;

    /**
     * 测量轮廓标识：1
     */
    private Integer measureNumber;

    /**
     * 周长（圆）
     */
    private String radius;

    /**
     * 平均间距
     */
    private Double meanDistance;

    /**
     * 最大间距
     */
    private Double maxDistance;

    /**
     * 最小间距
     */
    private Double minDistance;

    /**
     * 内角
     */
    private String innerAngle;

    /**
     * 外角
     */
    private String exteriorAngle;

    /**
     * 创建者
     */
    private Integer createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)
     */
    private String annotationType;

    /**
     * 标注数据类型(LineString,Polygon,point,pc,p,L)
     */
    private String locationType;

    /**
     * 切片id
     */
    private Integer slideId;

    /**
     * 中心
     */
    private String centerPoint;

    /**
     * 不同标签点的总数
     */
    private Integer pointCount;

    /**
     * 标注数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Object geometry;


}
