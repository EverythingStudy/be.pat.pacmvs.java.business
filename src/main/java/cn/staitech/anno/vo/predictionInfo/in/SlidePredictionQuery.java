package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: SlidePredictionQuery
* @Description:
* @author wanglibei
* @date 2023年11月7日
* @version V1.0
 */
@Data
public class SlidePredictionQuery{


    @ApiModelProperty(value = "切片ID")
    private Long slideId;
    
    @ApiModelProperty(value = "碎片状态默认为0校验通过，1校验不通过")
    private String eyeMent;
    
    @ApiModelProperty(value = "排序 1：原始切片使用 （失败的放前面，imageName asc）2：算法使用(主图放前面、imageName asc)")
    private int orderNumber;



}
