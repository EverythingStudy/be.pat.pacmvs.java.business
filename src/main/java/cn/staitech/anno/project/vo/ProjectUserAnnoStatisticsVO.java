package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: ProjectUserAnnoStatisticsVO
* @Description:
* @author wanglibei
* @date 2023年12月26日
* @version V1.0
 */
@Data
public class ProjectUserAnnoStatisticsVO {

	@ApiModelProperty("项目ID")
	private Long projectId;

	@ApiModelProperty("用户")
	private Long createBy;

	@ApiModelProperty("图像数量")
	private Integer imageCount;

	@ApiModelProperty("标注总数")
	private Integer annoCount;



}
