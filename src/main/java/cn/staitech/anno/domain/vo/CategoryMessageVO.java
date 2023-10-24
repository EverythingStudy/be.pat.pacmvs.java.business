package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryMessageVO {

    /**
     * 标注类别id
     */
    @ApiModelProperty(required = true, value = "标注类别id")
    private Long categoryId;

    /**
     * 标注类别名称
     */
    @ApiModelProperty(hidden = true, value = "标注类别名称")
    private String categoryName;

    /**
     * 标注类别编号
     */
    @ApiModelProperty(required = true, value = "标注类别编号")
    private Integer orderNumber;
}
