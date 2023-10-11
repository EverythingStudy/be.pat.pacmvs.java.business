package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 标注类别
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_pathological_indicator_category")
public class PathologicalIndicatorCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标注类别ID
     */
    @ApiModelProperty(required = true, value = "标签id")
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 结构指标ID
     */
    @ApiModelProperty(required = true, value = "病理指标id")
    private Long indicatorId;

    /**
     * 标注类别名称
     */
    @ApiModelProperty(value = "标签名称")
    @Size(min = 0, max = 50, message = "名称不能超过50个字符")
    private String categoryName;

    /**
     * 结构ID
     */
    @ApiModelProperty(required = true, value = "结构ID")
    @NotBlank(message = "结构ID不能为空!")
    private String structureId;

    /**
     * 颜色的RGB值
     */
    @ApiModelProperty(value = "颜色值RGB")
    @NotBlank(message = "RGB颜色值不可为空")
    private String rgb;

    /**
     * 颜色的HEX值
     */
    @ApiModelProperty(value = "颜色值HEX")
    @NotBlank(message = "HEX颜色值不可为空")
    private String hex;

    /**
     * 颜色名称(备用)
     */
    private String color;

    /**
     * 完整编码
     */
    @ApiModelProperty(hidden = true, value = "标签编号")
    private String number;

    /**
     * 图层顺序
     */
    @ApiModelProperty(required = true, value = "图层顺序")
    @NotNull(message = "图层顺序不可为空!")
    private Integer orderNumber;

    /**
     * 组织机构ID
     */
    @ApiModelProperty(hidden = true, value = "机构ID")
    private Long organizationId;

    /**
     * 0:默认标注类型；1:unlable
     */
    @ApiModelProperty(required = true, value = "标注类型")
    private Integer annoType;

    /**
     * 默认为0，1为删除
     */
    @ApiModelProperty(hidden = true, value = "删除状态（默认0，1删除)")
    private Integer delFlag;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true, value = "创建者id")
    private Long createBy;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true, value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

