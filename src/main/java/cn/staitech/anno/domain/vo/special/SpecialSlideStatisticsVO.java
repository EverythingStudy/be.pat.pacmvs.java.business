package cn.staitech.anno.domain.vo.special;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
* @ClassName: SpecialSlideStatisticsVO
* @Description:
* @author wanglibei
* @date 2023年8月1日
* @version V1.0
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
