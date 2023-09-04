package cn.staitech.anno.domain.vo.measurevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MeasureDelVO {
    
    @NotNull(message = "测量不可为空!")
    @ApiModelProperty(value = "测量id", required = true)
    private Long measureId;
    
    @ApiModelProperty(value = "权限字符")
    private String permission;
}
