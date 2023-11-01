package cn.staitech.anno.vo.project.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-21 18:56:52
 * @Description:
 */
@Data
public class ProjectIdsVO {
    /**
     * 图像id
     */
    @ApiModelProperty(value = "项目ID", required = true)
    private List<Long> projectIds;
}
