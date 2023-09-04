package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class PathologicalIndicatorCategoryVO {
    
    @ApiModelProperty(hidden = true, value = "标注类别记录id")
    private Long logId;
    
    @ApiModelProperty(hidden = true, value = "标注类别id")
    private Long categoryId;
    
    @NotNull(message = "病理指标不能为空")
    @ApiModelProperty(required = true, value = "病理指标id")
    private Long indicatorId;
    
    @NotBlank(message = "颜色值不可为空")
    @ApiModelProperty(required = true, value = "颜色值")
    @NotBlank(message = "病理指标名称不可为空 ！")
    private String color;
    
    @ApiModelProperty(hidden = true, value = "创建者id")
    private Long createBy;
    
    @ApiModelProperty(hidden = true, value = "创建时间")
    private Date createTime;
    
    @ApiModelProperty(hidden = true, value = "更新者id")
    private Long updateBy;
    
    @ApiModelProperty(hidden = true, value = "更新时间")
    private Date updateTime;
    
    @NotBlank(message = "类别名称不可为空")
    @Size(min = 0, max = 50, message = "名称不能超过50个字符")
    @ApiModelProperty(required = true, value = "标注类别名称")
    private String categoryName;

    @ApiModelProperty(hidden = true, value = "标注类型")
    private Long annoType;

    @ApiModelProperty(hidden = true, value = "标注数量")
    private Integer sum;

    @ApiModelProperty(value = "图层顺序")
    @NotBlank(message = "图层顺序不可为空 ！")
    private String orderNumber;

    @ApiModelProperty(hidden = true,value = "标签编号")
    private String number;

}
