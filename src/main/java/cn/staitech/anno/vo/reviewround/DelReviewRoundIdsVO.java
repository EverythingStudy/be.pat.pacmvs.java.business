package cn.staitech.anno.vo.reviewround;

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
    @NotNull(message = "{DelReviewRoundIdsVO.reviewRoundIds.isnull}")
    @ApiModelProperty(value = "reviewRoundIds", required = true)
    private List<Long> reviewRoundIds;
}
