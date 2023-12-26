package cn.staitech.anno.project.vo;

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
public class ImageAnnoUserQueryIn {

    
	@ApiModelProperty(required = false, value = "projectIds 数组")
    private Long[] projectIds;
    

}
