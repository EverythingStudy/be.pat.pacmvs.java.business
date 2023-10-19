package cn.staitech.anno.domain.vo.slideVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-21 19:33:13
 * @Description:
 */
@Data
public class DelSlideIdsVO {
    /**
     * 切片id .
     */
    @NotNull(message = "{SlideUpdateVO.slideId.isnull}")
    @ApiModelProperty(value = "切片id", required = true)
    private List<Long> slideIds;
}
