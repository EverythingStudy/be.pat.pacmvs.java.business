package cn.staitech.anno.vo.annotation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author wangf
 */
@Data
public class LabelListVO {
    @ApiModelProperty(required = true, value = "标签id")
    private Long categoryId;
    @ApiModelProperty(required = true, value = "病理指标id")
    private Long indicatorId;
    @ApiModelProperty(required = true, value = "结构ID")
    private String structureId;
    @ApiModelProperty(value = "结构名称")
    private String structureName;

    private String color;
    @NotBlank(message = "颜色值不可为空")
    private String rgb;
    @ApiModelProperty(required = true, value = "颜色值HEX")
    private String hex;
    @ApiModelProperty(hidden = true, value = "机构ID")
    private Long organizationId;
    @ApiModelProperty(value = "创建者id")
    private Long createBy;
    @ApiModelProperty(value = "创建者名字")
    private String userName;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(hidden = true, value = "更新者id")
    private Long updateBy;
    @ApiModelProperty(hidden = true, value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "标签名称")
    private String categoryName;
    @ApiModelProperty(hidden = true, value = "标注类型")
    private Long annoType;
    @ApiModelProperty(value = "图层顺序")
    private Integer orderNumber;
    @ApiModelProperty(value = "标签编号")
    private String number;
    @ApiModelProperty(value = "种属编号")
    private String speciesId;
    @ApiModelProperty(value = "脏器编号")
    private String organId;
}
