package cn.staitech.anno.domain.vo.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticSysDictDataOutVO {
    
    @ApiModelProperty(value = "字典编码")
    private Long dictCode;
    
    @ApiModelProperty(value = "字典标签")
    private String dictLabel;
    
    @ApiModelProperty(value = "字典排序")
    private Long dictSort;
}
