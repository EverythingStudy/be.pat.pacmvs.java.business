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

//    @ApiModelProperty(value = "是否是主图（默认为2，1是，2否）")
//    private String mainImage;

    @ApiModelProperty(value = "碎片状态默认为0校验通过，1校验不通过")
    private String eyeMent;
    
    @ApiModelProperty(value = "文件夹url")
    private String folderUrl;
    
    
    @Override
    public String toString() {
        return "ImageCsvListVO{} " + super.toString();
    }
}
