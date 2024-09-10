package cn.staitech.anno.vo.organization;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysConfigOut {
    @ApiModelProperty(value = "code",required = true)
    private String configKey;

    @ApiModelProperty(value = "value值")
    private String configValue;

    @ApiModelProperty(value = "更新者id",hidden = true)
    private Long updateBy;
}
