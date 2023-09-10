package cn.staitech.anno.domain.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticCategoryListOutVO {
    
    @ApiModelProperty(value = "标注类别id")
    private Integer categoryId;
    
    @ApiModelProperty(value = "标注类别名称")
    private String categoryName;
    
    @ApiModelProperty(value = "颜色RGB值")
    private String color;
    
    @ApiModelProperty(value = "病例指标id")
    private Long indicatorId;
    
    @ApiModelProperty(value = "病例指标名称")
    private String indicatorName;


    /**
     * 数量
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer sum;
}
