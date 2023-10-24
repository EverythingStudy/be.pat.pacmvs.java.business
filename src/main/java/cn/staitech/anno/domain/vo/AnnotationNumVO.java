package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnnotationNumVO {

    @ApiModelProperty(value = "创建者id")
    private Long createBy;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "人工标注数量")
    private int count;
}
