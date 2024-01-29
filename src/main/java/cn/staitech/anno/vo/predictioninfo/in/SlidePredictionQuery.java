package cn.staitech.anno.vo.predictioninfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SlidePredictionQuery
 * @Description:
 * @date 2023年11月7日
 */
@Data
public class SlidePredictionQuery {


    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    @ApiModelProperty(value = "碎片状态默认为0校验通过，1校验不通过")
    private String eyeMent;

    @ApiModelProperty(value = "排序 1：原始切片使用 （失败的放前面，imageName asc）2：算法使用(主图放前面、imageName asc)")
    private int orderNumber;


}
