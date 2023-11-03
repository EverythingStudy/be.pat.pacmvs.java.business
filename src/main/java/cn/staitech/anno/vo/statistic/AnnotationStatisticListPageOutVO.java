package cn.staitech.anno.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author admin
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnnotationStatisticListPageOutVO {

    @ApiModelProperty(value = "标注ID")
    private Long markingId;

}
