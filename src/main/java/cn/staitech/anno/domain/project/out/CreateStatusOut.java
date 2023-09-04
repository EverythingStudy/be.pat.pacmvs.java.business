package cn.staitech.anno.domain.project.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/7/4 11:24
 * @desc
 */
@Data
public class CreateStatusOut {
    @ApiModelProperty(value = "是否自动创建过，0-未创建；1-已创建过")
    private Long createStatus;
    @ApiModelProperty(value = "一键创建结果")
    private Long resultDesc;
}
