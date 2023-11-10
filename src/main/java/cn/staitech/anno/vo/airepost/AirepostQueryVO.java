package cn.staitech.anno.vo.airepost;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wangfeng
 * @create: 2023-11-10 17:52:04
 * @Description: 拼接Viewer列表查询条件
 */
@Data
public class AirepostQueryVO {
    @ApiModelProperty(value = "切片ID")
    private Long slideId;
    @ApiModelProperty(value = "项目ID")
    private Long projectId;
}

