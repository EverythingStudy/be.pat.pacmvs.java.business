package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目ID列表
 * 2023-04-04 13:35
 *
 * @author wangfeng
 */
@Data
public class ProjectIdListVO {

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID列表", required = true)
    private Long projectId;

}
