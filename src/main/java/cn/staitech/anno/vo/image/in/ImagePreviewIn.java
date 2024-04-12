package cn.staitech.anno.vo.image.in;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: ImagePreviewIn
* @Description:
* @author wanglibei
* @date 2024年4月10日
* @version V1.0
 */
@Data
public class ImagePreviewIn{

	@ApiModelProperty(value = "切片id")
    @NotNull(message = "[切片id]不能为空")
    private Long imageId;

}
