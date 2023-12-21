package cn.staitech.anno.vo.pathologicalIndicatorCategory;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class PathologicalIndicatorCategoryOutVo {

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
    @Size(min = 0, max = 50, message = "{InsertProjectVO.projectName.length}")
    private String categoryName;

    /**
     * 结构ID
     */
    @ApiModelProperty(required = true, value = "结构ID")
    @NotBlank(message = "{PathologicalIndicatorCategoryVO.structureId.isnull}")
    private String structureId;

    /**
     * 颜色的RGB值
     */
    @ApiModelProperty(value = "颜色值RGB")
    @NotBlank(message = "{PathologicalIndicatorCategory.rgb.isnull}")
    private String rgb;

    /**
     * 颜色的HEX值
     */
    @ApiModelProperty(value = "颜色值HEX")
    @NotBlank(message = "{PathologicalIndicatorCategory.hex.isnull}")
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
    @NotNull(message = "{StatisticCategoryListOutVO.orderNumber.isnull}")
    private Integer orderNumber;

    /**
     * 组织机构ID
     */
    @ApiModelProperty(hidden = true, value = "机构ID")
    private Long organizationId;

    /**
     * 指标编码
     */
    @ApiModelProperty(hidden = true, value = "指标编码")
    private String categoryCode;

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

    @TableField(exist = false)
    @ApiModelProperty(value = "指标id(模糊查询)")
    private String structureIds;


    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "组内排序 1:标注区域 2：考核区域 3：结构标签")
    private Integer groupNumber;

    @ApiModelProperty(hidden = true, value = "标签类型 0:下拉筛选标签；1:自定义标签")
    private Integer categoryType;
}
