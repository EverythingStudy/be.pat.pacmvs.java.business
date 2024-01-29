package cn.staitech.anno.vo.predictioninfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: EyeSlideResult
 * @Description:
 * @date 2023年11月15日
 */
@Data
public class EyeSlideResult {
    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "图像路径")
    private String imagePath;

    @ApiModelProperty(value = "文件夹名称")
    private String folderName;

    @ApiModelProperty(value = "预测缩略图url")
    private String predictionThumbUrl;

    @ApiModelProperty(value = "AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败")
    private Integer aiAnalyzed;
}
