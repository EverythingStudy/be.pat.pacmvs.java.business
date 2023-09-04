package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class IndicatorAddVO {
    
    @NotBlank(message = "病例指标名称不能为空字符串")
    @Size(min = 0, max = 60, message = "名称不能超过60个字符")
    @ApiModelProperty(value = "病例指标名称", required = true)
    private String indicatorName;
    
    @ApiModelProperty(hidden = true, value = "创建者id")
    private Long userId;
    
    @ApiModelProperty(hidden = true, value = "创建者")
    private String createBy;
    
}
