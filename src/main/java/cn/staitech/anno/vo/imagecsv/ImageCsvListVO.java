package cn.staitech.anno.vo.imagecsv;

import cn.staitech.anno.domain.ImageCsv;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: wangfeng
 * @create: 2023-09-20 17:46:52
 * @Description:
 */
@Data
public class ImageCsvListVO extends ImageCsv implements Serializable {
	
	@ApiModelProperty(value = "文件夹名称")
    private String folderName;

    @ApiModelProperty(value = "预测缩略图url")
    private String predictionThumbUrl;

    @ApiModelProperty(value = "AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败")
    private Integer aiAnalyzed;

    @ApiModelProperty(value = "碎片状态默认为0校验通过，1校验不通过")
    private String eyeMent;
    
    @ApiModelProperty(value = "文件夹url")
    private String folderUrl;
}
