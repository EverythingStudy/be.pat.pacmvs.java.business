package cn.staitech.anno.vo.labelprojectstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectCreateByOut {
    @ApiModelProperty(value = "创建者id")
    private Long userId;

    @ApiModelProperty(value = "创建者")
    private String userName;

}
