package cn.staitech.annotation.vo.anno;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author mugw
 * @version 1.0
 * @description 新增标注参数
 * @date 2025/5/22 09:40:28
 */
@Data
public class AnnotationReq {

    @NotNull(message = "{NO_SLIDE_DATA}")
    @JsonProperty("slide_id")
    private Long slideId;

    @ApiModelProperty(value = "标注类型：默认 0  1 粗轮廓 2 精细轮廓")
    private Long contourType;
}
