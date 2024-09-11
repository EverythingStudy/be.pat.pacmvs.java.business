package cn.staitech.anno.vo.annotation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryVO {

    /**
     * 标注类别id
     */
    @ApiModelProperty(required = true, value = "标注类别id")
    private Long categoryId;
}
