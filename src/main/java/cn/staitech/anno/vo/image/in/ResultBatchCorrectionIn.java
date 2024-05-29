package cn.staitech.anno.vo.image.in;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: ResultBatchCorrectionIn
* @Description:
* @author wanglibei
* @date 2024年4月3日
* @version V1.0
 */
@Data
public class ResultBatchCorrectionIn{
    

	@ApiModelProperty(value = "切片id")
    @NotNull(message = "[切片id]不能为空")
    private List<Long> imageId;
    
    @ApiModelProperty(value = "修正状态  1：修正")
    private Integer definitionStatus;
    

   

}
