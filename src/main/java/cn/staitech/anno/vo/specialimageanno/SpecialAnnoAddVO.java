package cn.staitech.anno.vo.specialimageanno;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SpecialAnnoAddVO {

    @NotNull(message = "{SlideUpdateVO.slideId.isnull}")
    @ApiModelProperty(value = "切片id", required = true)
    private Long specialImageId;

    @ApiModelProperty(value = "用户id", required = true)
    private Long markingCreateBy;

    @ApiModelProperty(value = "标注id")
    private Long annotationId;

    /**
     * 位置 .
     */
    @ApiModelProperty(value = "标注地方位置图形数据", required = true)
    private String location;

    /**
     * 标注类型 .
     */
    @ApiModelProperty(value = "标注类型")
    private String locationType;

    /**
     * 标注类别id .
     */
    @ApiModelProperty(value = "标注类别id")
    private Integer categoryId;

    /**
     * 项目id .
     */
    @ApiModelProperty(hidden = true, value = "专题id")
    private Long specialId;

    /**
     * 数量 .
     */
    @ApiModelProperty(hidden = true, value = "数量")
    private Integer sum;

    /**
     * 创建者id .
     */
    @ApiModelProperty(hidden = true, value = "创建者id")
    private Long createBy;

    /**
     * 描述 .
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 权限标识 .
     */
    @ApiModelProperty(value = "权限标识", required = true)
    private String permission;

    @ApiModelProperty(name = "dictId", value = "脏器标签ID")
    private Long dictId;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型 1:save  2:update 3:delte")
    private Long operateType;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作后状态 1:失败  2:成功")
    private Integer operateStatus;

    @ApiModelProperty(value = "old标注id")
    private Object oldAnnotationId;

    @ApiModelProperty(name = "sliceAnnotationId", value = "标注ID")
    private Long sliceAnnotationId;

    @ApiModelProperty(name = "imageId", value = "图像ID")
    private Long imageId;

    @ApiModelProperty(name = "measure", value = "面积")
    private BigDecimal measure;

    @ApiModelProperty(name = "perimeter", value = "周长")
    private BigDecimal perimeter;

    @ApiModelProperty(name = "annotationType", value = "标注类型（1人工标注  2算法标注 ）")
    private Integer annotationType;

    @ApiModelProperty(name = "attribute", value = "属性")
    private String attribute;

    @ApiModelProperty(name = "tileId", value = "层数ID")
    private Integer tileId;

    @ApiModelProperty(name = "examinationFlag", value = "标注审核状态（0未审核标注,1已审核标注  ）")
    private Integer examinationFlag;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "更新人")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(name = "x", value = "标注图的质心x")
    private String x;

    @ApiModelProperty(name = "y", value = "标注图的质心y")
    private String y;

    @ApiModelProperty(name = "annoType", value = "1:ROI")
    private Integer annoType;

    @ApiModelProperty(name = "createCategoryId", value = "标注类别创建者")
    private Long createCategoryId;

    @ApiModelProperty(name = "createName", value = "创建人昵称")
    private String createName;

    @ApiModelProperty(name = "updateName", value = "修改人昵称")
    private String updateName;

    @ApiModelProperty(value = "标注（类别）颜色RGB值")
    private String color;

}
