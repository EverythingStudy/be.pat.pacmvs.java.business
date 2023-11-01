package cn.staitech.anno.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialSlideStatisticsVO
 * @Description:
 * @date 2023年8月1日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialSlideStatisticsVO implements Serializable {
    /**
     * @Fields serialVersionUID :
     */
    private static final long serialVersionUID = -8949072598396999171L;

    @ApiModelProperty(value = "人工诊断状态：0未诊断，1已诊断")
    private Integer diagnosis;

    @ApiModelProperty(value = "统计数量")
    private Integer totalCount;

}
