package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/10/17 16:55
 * @desc
 */
@Data
public class PreExecData {
    
    @ApiModelProperty(value = "切片id")
    private Long slideId;
    
    @ApiModelProperty(value = "文件夹路径")
    private String folderName;
    

}
