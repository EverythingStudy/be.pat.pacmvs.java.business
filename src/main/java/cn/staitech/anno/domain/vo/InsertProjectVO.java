package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
public class InsertProjectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "项目名称不能为空字符串")
    @ApiModelProperty(required = true, value = "项目名称")
    @Size(min = 0, max = 50, message = "名称不能超过50个字符")
    private String projectName;


    @NotNull(message = "状态不能为空")
    @ApiModelProperty(required = true, value = "状态(1:使用项目名称创建病理指标 2:使用现有病理指标 3:无属性)")
    private Integer status;

    @ApiModelProperty(value = "指标id")
    private Long indicatorsId;

    @ApiModelProperty(required = true,value = "切片列表(可以不传参数)")
    private int[] imageIdList;

    @ApiModelProperty(value = "项目描述")
    @Size(min = 0, max = 50, message = "项目描述不能超过50个字符")
    private String description;

    @ApiModelProperty(value = "脏器组织id")
    private Long dictCode;

}
