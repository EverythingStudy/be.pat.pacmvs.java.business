package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/19 11:11:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownTaskIn {
    @ApiModelProperty("切片id集合")
    private List<Long> slideIds;
    @ApiModelProperty("项目ID")
    private Long projectId;
}
