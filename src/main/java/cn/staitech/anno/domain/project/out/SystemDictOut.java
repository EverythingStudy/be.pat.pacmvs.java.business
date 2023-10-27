package cn.staitech.anno.domain.project.out;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api(value = "系统数据字典", tags = "系统数据字典")
public class SystemDictOut {

    @ApiModelProperty(value = "字典主键")
    private Long dictId;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

}