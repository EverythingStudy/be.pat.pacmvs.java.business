package cn.staitech.anno.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class LabelListVO {

    @ApiModelProperty(required = true, value = "标签id")
    private Long categoryId;

    @ApiModelProperty(required = true, value = "病理指标id")
    private Long indicatorId;

    @ApiModelProperty(value = "颜色值")
    private String color;

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
    private String orderNumber;

    @ApiModelProperty(value = "标签编号")
    private String number;

}
