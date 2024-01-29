package cn.staitech.anno.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: CancelCompleteProjectVO
 * @Description:
 * @date 2024年1月5日
 */
@Data
public class CancelCompleteProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(required = true, value = "项目ID")
    private Long projectId;
}
