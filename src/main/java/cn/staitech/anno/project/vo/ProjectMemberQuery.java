package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: ImageAnnoUserQueryIn
 * @Description:
 * @date 2023年12月25日
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberQuery {


    @ApiModelProperty(required = false, value = "projectId列表")
    private List<Long> projectIds;
    @ApiModelProperty(required = false, value = "partUsers列表")
    private List<Long> partUsers;


}
