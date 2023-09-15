package cn.staitech.anno.project.vo;

import cn.staitech.anno.project.domain.Project;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/13 17:44:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVO extends Project {

    private String indicatorName;
    @ApiModelProperty("种属")
    private String speciesVal;
    @ApiModelProperty("机构")
    private String organizationName;
    @ApiModelProperty("创建者")
    private String userName;
    @ApiModelProperty("品系")
    private String psName;
}
