package cn.staitech.anno.vo.annotation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryStatisticsIn {
    @ApiModelProperty(value = "标注id")
    private Long categoryId;

}
