package cn.staitech.annotation.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 筛差
 */
@TableName(value = "fr_annotation_sd")
@Data
public class AnnotationSd implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "annotation_id", type = IdType.AUTO)
    private Long annotationId;
    /**
     * 面积
     */
    private BigDecimal area;
    /**
     * 周长
     */
    private BigDecimal perimeter;
    /**
     * 轮廓描述
     */
    private String description;
    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 轮廓坐标625
     */
    @TableField("contour")
    private Geometry geometry;
    /**
     * 轮廓类型
     */
    private String locationType;
    /**
     * 标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量数据)
     */
    private String annotationType;
    /**
     * 标注创建者
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 切片id
     */
    private Long slideId;
    /**
     * geojson中数据id
     */
    private String jsonId;
    /**
     * 单切片id
     */
    private Long singleSlideId;
}