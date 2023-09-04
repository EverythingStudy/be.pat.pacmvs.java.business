package cn.staitech.anno.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 标注实体类 . 标注表 tb_annotation
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Api(value = "标注", tags = "标注")
@Data
public class Annotation {

    /**
     * 状态 .
     */
    @ApiModelProperty(value = "状态")
    private static final long serialVersionUID = 1L;

    /**
     * 标注id .
     */
    @ApiModelProperty(value = "标注id")
    private Long annotationId;

    /**
     * 面积 .
     */
    @ApiModelProperty(value = "面积")
    private String measure;

    /**
     * 周长 .
     */
    @ApiModelProperty(value = "周长")
    private String perimeter;

    /**
     * 标注类型（1 人工标注  2 算法标注 ） .
     */
    @ApiModelProperty(value = "标注类型（1人工标注  2算法标注 ）")
    private String annotationType;

    /**
     * 描述 .
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 属性 .
     */
    @ApiModelProperty(value = "属性")
    private String attribute;

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
     * 项目id .
     */
    private Long projectId;

    /**
     * 项目图像id .
     */
    @SuppressWarnings("checkstyle:MemberName")
    @ApiModelProperty(value = "项目图像id")
    private Long imageId;

    /**
     * 切片id .
     */
    private Long slideId;

    /**
     * 层数id .
     */
    private Integer tileId;

    /**
     * 审核标注（0未审核标注,1已审核标注  ） .
     */
    @ApiModelProperty(value = "审核标注（0未审核标注,1已审核标注  ）")
    private Long examinationFlag;

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
     * 标注类别id .
     */
    private Long categoryId;

    /**
     * 标注图的质心x
     */
    private String x;

    /**
     * 标注图的质心y
     */
    private String y;
    
    /**
     * 标注类型
     */
    private Long annoType;
    
    /**
     * 标注类别创建人
     */
    private Long createCategoryId;
    
    
    
}
