package cn.staitech.anno.domain.vo.slideVo;

import cn.staitech.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class SlideUpdateVO extends BaseEntity {

    /**
     * 切片id .
     */
    @NotNull(message = "切片id不可为空 !")
    @ApiModelProperty(value = "切片id", required = true)
    private List<Long> slideId;

    /**
     * 描述 .
     */
    @Size(min = 0, max = 50, message = "描述不可超过50字段")
    @ApiModelProperty(value = "描述")
    private String description;
}
