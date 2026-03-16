package cn.staitech.annotation.vo.anno;

import cn.staitech.sft.logaudit.annotation.IgnoreLogField;
import cn.staitech.sft.logaudit.annotation.LogFieldDBConvert;
import cn.staitech.sft.logaudit.mapper.LogAuditAddMapper;
import cn.staitech.sft.logaudit.req.LogAuditBaseReq;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mugw
 * @version 1.0
 * @description 新增标注参数
 * @date 2025/5/22 09:40:28
 */
@Data
public class AIAnnotationUpdateVo   {

    /**
     * 主键id
     */
    @NotNull(message = "{NO_ANNOTATION_DATA}")
    @JsonProperty("marking_id")
    @IgnoreLogField
    private Long annotationId;

    /**
     * 面积
     */
    @IgnoreLogField
    private BigDecimal area;

    /**
     * 周长
     */
    @IgnoreLogField
    private BigDecimal perimeter;

    /**
     * 轮廓描述
     */
    private String description;

    /**
     * 标签id
     */
    @JsonProperty("category_id")
    @IgnoreLogField
    private Long tagId;
    @LogFieldDBConvert(mapper = LogAuditAddMapper.class, convertField = "tagIdLog")
    private String tagIdLog;

    /**
     * 轮廓
     */
    @IgnoreLogField
    private Geometry geometry;

    /**
     * 轮廓类型
     */
    @IgnoreLogField
    private String locationType;

    /**
     * 标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注)
     */
    @IgnoreLogField
    @JsonProperty("annotation_type")
    private String annotationType;

    /**
     * 更新者
     */
    @IgnoreLogField
    private Long updateBy;

    /**
     * 更新时间
     */
    @IgnoreLogField
    private Date updateTime;

    /**
     * 切片id
     */
    @IgnoreLogField
    @JsonProperty("slide_id")
    private Long slideId;

    /**
     * geojson中数据id
     */@IgnoreLogField
    private String jsonId;
}
