package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: EyeThumImageQuery
* @Description:
* @author wanglibei
* @date 2023年11月14日
* @version V1.0
 */
@Data
public class EyeThumImageQuery{


    @ApiModelProperty(value = "切片ID")
    private Long slideId;
    
    @ApiModelProperty(value = "删除标志（0：存在，1：删除）")
    private String delFlag;
    
    @ApiModelProperty(value = "是否是主图默认为2，1是，2否")
    private String mainImage;


}
