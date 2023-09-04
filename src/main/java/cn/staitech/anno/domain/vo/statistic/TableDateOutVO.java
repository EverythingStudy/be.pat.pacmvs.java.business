package cn.staitech.anno.domain.vo.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TableDateOutVO {
    
    @ApiModelProperty(value = "表记录最早时间")
    private String earliestDate;
    
}
