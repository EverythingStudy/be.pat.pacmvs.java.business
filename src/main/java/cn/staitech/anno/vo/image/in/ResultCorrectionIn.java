package cn.staitech.anno.vo.image.in;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: ResultCorrectionIn
* @Description:
* @author wanglibei
* @date 2024年4月3日
* @version V1.0
 */
@Data
public class ResultCorrectionIn{
    

	@ApiModelProperty(value = "切片id")
    @NotNull(message = "[切片id]不能为空")
    private Long imageId;
    
    @ApiModelProperty(value = "修正状态  1：修正  2：还原")
    private Integer definitionStatus;
    

   

}
