package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SlideDescriptionVo {
    
    /**
     * 切片id .
     */
    @ApiModelProperty(value = "切片id列表", required = true)
    private List<Long> slideId;
    
    /**
     * 描述 .
     */
    @Size(min = 0, max = 50, message = "{SlideUpdateVO.description.length}")
    @ApiModelProperty(value = "描述")
    private String description;
    
}
