package cn.staitech.anno.vo.predictionInfo.out;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 眼科切片预测表
 * </p>
 *
 * @author wanglibei
 * @since 2023-11-02
 */
@Data
public class SlidePredictionOut{


    @ApiModelProperty(value = "是否已经有主图 0：没有主图 1：已经有主图")
    private Integer alreadyMainImage;

    @ApiModelProperty(name = "原始切片信息")
    private List<SlidePredictionInfo>  list;



}
