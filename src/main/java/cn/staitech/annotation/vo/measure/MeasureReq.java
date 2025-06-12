package cn.staitech.annotation.vo.measure;

import cn.staitech.common.core.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2025/5/30 09:25:09
 */
@Data
public class MeasureReq extends PageRequest {

    @NotNull(message = "{ARGUMENT_INVALID}")
    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    @ApiModelProperty(value = "标注名称")
    private String measureFullName;
}
