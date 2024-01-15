package cn.staitech.anno.vo.predictioninfo.in;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: SlideImageStatusrVO
* @Description:
* @author wanglibei
* @date 2024年1月15日
* @version V1.0
 */
@Data
public class SlideImageStatusrVO{
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "切片ID列表")
    @NotNull(message = "slideIds is null")
    private List<Long> slideIds;
}

