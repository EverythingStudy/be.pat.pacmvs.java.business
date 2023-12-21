package cn.staitech.anno.vo.predictionInfo.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SlidePredictionDeail
 * @Description:
 * @date 2023年11月7日
 */
@Data
public class SlidePredictionDeail {


    @ApiModelProperty(value = "切片预测ID")
    private Long slidePredictionId;

    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    @ApiModelProperty(value = "图像ID")
    private Long imageId;

    @ApiModelProperty(value = "图像名称")
    private String image_name;

    @ApiModelProperty(value = "缩略图地址")
    private String thumbUrl;

    @ApiModelProperty(value = "图像地址")
    private String imagePath;

    @ApiModelProperty(value = "AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败）")
    private Integer aiAnalyzed;

    @ApiModelProperty(value = "是否是主图默认为2，1是，2否")
    private String mainImage;


    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "机构ID")
    private Long organizationId;

    @ApiModelProperty(name = "项目ID")
    private Long projectId;


}
