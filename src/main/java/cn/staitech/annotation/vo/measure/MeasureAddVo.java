package cn.staitech.annotation.vo.measure;


import cn.staitech.sft.logaudit.annotation.IgnoreLogField;
import cn.staitech.sft.logaudit.req.LogAuditBaseReq;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import java.util.Date;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2025/5/30 09:22:39
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MeasureAddVo extends LogAuditBaseReq {

    /**
     * 切片id
     */
    @JsonProperty("slide_id")
    @IgnoreLogField
    private Long slideId;

    /**
     * 标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)
     */
    @JsonProperty("annotation_type")
    @IgnoreLogField
    private String annotationType;

    /**
     * 面积
     */
    @IgnoreLogField
    private String area;

    /**
     * 周长
     */
    @IgnoreLogField
    private String perimeter;

    /**
     * 标注名称
     */
    @IgnoreLogField
    private Long number;

    /**
     * 测量轮廓类型(0:正常,表示有关系,默认为0)
     */
    @JsonProperty("measure_type")
    @IgnoreLogField
    private Integer measureType;

    /**
     * 测量关系
     */
    @IgnoreLogField
    private String measureRelation;

    /**
     * 测量轮廓表示名称:L
     */
    @IgnoreLogField
    @JsonProperty("measure_name")
    private String measureName;

    /**
     * 测量轮廓标识：1
     */
    @IgnoreLogField
    private Integer measureNumber;

    /**
     * 平均间距
     */
    @IgnoreLogField
    private Double meanDistance;

    /**
     * 最大间距
     */
    @IgnoreLogField
    private Double maxDistance;

    /**
     * 最小间距
     */
    @IgnoreLogField
    private Double minDistance;

    /**
     * 内角
     */
    @IgnoreLogField
    private String innerAngle;

    /**
     * 外角
     */
    @IgnoreLogField
    private String exteriorAngle;

    /**
     * 中心
     */
    @IgnoreLogField
    private String centerPoint;

    /**
     * 标注数据类型(LineString,Polygon,point,pc,p,L)
     */
    @JsonProperty("location_type")
    @IgnoreLogField
    private String locationType;

    /**
     * 周长（圆）
     */
    @IgnoreLogField
    private String radius;

    /**
     * 标注数据（JSON格式）
     */
    @TableField("contour")
    @IgnoreLogField
    private Geometry geometry;

    /**
     * 创建者
     */
    @IgnoreLogField
    private Long createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @IgnoreLogField
    private Date createTime;

    /**
     * 更新者
     */
    @IgnoreLogField
    private Long updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @IgnoreLogField
    private Date updateTime;

    /**
     * 标注名称
     */
    @IgnoreLogField
    private String measureFullName;
}
