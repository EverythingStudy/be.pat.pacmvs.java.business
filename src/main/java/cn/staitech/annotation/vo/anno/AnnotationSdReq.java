package cn.staitech.annotation.vo.anno;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 获取筛差数据
 */
@Data
public class AnnotationSdReq {
    /**
     * 单切片id
     */
    @NotNull(message = "单切片id不能为空")
    @ApiModelProperty(value = "单切片id", required = true)
    private Long singleId;
}
