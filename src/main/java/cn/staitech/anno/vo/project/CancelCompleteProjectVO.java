package cn.staitech.anno.vo.project;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 
* @ClassName: CancelCompleteProjectVO
* @Description:
* @author wanglibei
* @date 2024年1月5日
* @version V1.0
 */
@Data
public class CancelCompleteProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(required = true, value = "项目ID")
    private Long projectId;
}
