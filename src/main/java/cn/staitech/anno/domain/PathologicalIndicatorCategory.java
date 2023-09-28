package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 标注类别
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableName(value ="tb_pathological_indicator_category")
public class PathologicalIndicatorCategory {
    @ApiModelProperty(required = true, value = "标签id")
    private Long categoryId;
    @ApiModelProperty(required = true, value = "病理指标id")
    private Long indicatorId;
    @ApiModelProperty(required = true, value = "结构ID")
    @NotBlank(message = "结构ID不能为空!")
    private String structureId;
    private String color;
    @ApiModelProperty(value = "颜色值RGB")
    @NotBlank(message = "RGB颜色值不可为空")
    private String rgb;

    @ApiModelProperty(value = "颜色值HEX")
    @NotBlank(message = "HEX颜色值不可为空")
    private String hex;

    @ApiModelProperty(hidden = true, value = "机构ID")
    private Long organizationId;

    @ApiModelProperty(hidden = true, value = "创建者id")
    private Long createBy;

    @ApiModelProperty(hidden = true, value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(hidden = true, value = "更新者id")
    private Long updateBy;

    @ApiModelProperty(hidden = true, value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "标签名称")
    @Size(min = 0, max = 50, message = "名称不能超过50个字符")
    private String categoryName;

    @ApiModelProperty(required = true, value = "标注类型")
    private Long annoType;

    @ApiModelProperty(required = true, value = "图层顺序")
    @NotBlank(message = "图层不可为空")
    private String orderNumber;

    @ApiModelProperty(hidden = true, value = "标签编号")
    private String number;

    @ApiModelProperty(hidden = true, value = "删除状态（默认0，1删除)")
    private Integer delFlag;
}