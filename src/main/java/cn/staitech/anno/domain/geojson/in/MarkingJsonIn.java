package cn.staitech.anno.domain.geojson.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@Data
public class MarkingJsonIn {
    @NotNull(message = "图片id")
    @ApiModelProperty(value = "图片id")
    private Long imageId;

    @NotNull(message = "状态不可为空")
    @ApiModelProperty(value = "(1:导出测量数据,2:导出标注数据)")
    private Long status;

    @ApiModelProperty(value = "图片id",required = true)
    HttpServletResponse response;


}
