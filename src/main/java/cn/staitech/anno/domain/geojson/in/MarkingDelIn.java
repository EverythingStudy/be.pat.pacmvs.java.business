package cn.staitech.anno.domain.geojson.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MarkingDelIn {

    @NotNull(message = "切片不可为空")
    @ApiModelProperty(value = "切片id")
    private Long slide_id;

    @NotBlank(message = "标注id不可为空！")
    @ApiModelProperty(value = "标注id")
    private String annotation_id;
}
