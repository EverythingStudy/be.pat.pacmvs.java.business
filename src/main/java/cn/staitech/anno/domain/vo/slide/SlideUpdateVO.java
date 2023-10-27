package cn.staitech.anno.domain.vo.slide;

import cn.staitech.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SlideUpdateVO extends BaseEntity {

    /**
     * 切片id .
     */
    @NotNull(message = "{SlideUpdateVO.slideId.isnull}")
    @ApiModelProperty(value = "切片id", required = true)
    private List<Long> slideId;

    /**
     * 描述 .
     */
    @Size(min = 0, max = 50, message = "{SlideUpdateVO.description.length}")
    @ApiModelProperty(value = "描述")
    private String description;
}
