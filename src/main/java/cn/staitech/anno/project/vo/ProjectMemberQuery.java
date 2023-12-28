package cn.staitech.anno.project.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
* @ClassName: ImageAnnoUserQueryIn
* @Description:
* @author wanglibei
* @date 2023年12月25日
* @version V1.0
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
