package cn.staitech.anno.domain.reviewround;

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
public class DelReviewRoundIdsVO {
    /**
     * reviewRoundIds
     */
    @NotNull(message = "reviewRoundIdsid不可为空 !")
    @ApiModelProperty(value = "reviewRoundIds", required = true)
    private List<Long> reviewRoundIds;
}
