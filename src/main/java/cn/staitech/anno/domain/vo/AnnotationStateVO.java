package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AnnotationStateVO {

    /**
     * 切片ID .
     */
    @NotNull(message = "切片ID不可为空！")
    @ApiModelProperty(value = "切片ID", required = true)
    private Long slideId;

    /**
     * 标注状态 (0未开始 1标注中 2标注完成) .
     */
    @NotNull(message = "标注状态不可为空！")
    @Max(value = 2)
    @Min(value = 1)
    @ApiModelProperty(value = "标注状态 (1标注中(取消完成该标注) 2标注完成", required = true, allowableValues = "1, 2")
    private Integer processFlag;
}