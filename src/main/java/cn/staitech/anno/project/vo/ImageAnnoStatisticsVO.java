package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @ClassName: ImageAnnoStatisticsVO
 * @Description:
 * @author wanglibei
 * @date 2023年12月26日
 * @version V1.0
 */
@Data
public class ImageAnnoStatisticsVO {

	@ApiModelProperty("项目ID")
	private Long projectId;
	
	
	@ApiModelProperty("userId")
	private Long userId;

	@ApiModelProperty("项目名称")
	private String projectName;

	@ApiModelProperty("用户账号")
	private String userName;

	@ApiModelProperty("用户姓名")
	private String nickName;

	@ApiModelProperty("图像数量")
	private Integer imageCount;

	@ApiModelProperty("标注总数")
	private Integer markingNum;

	@ApiModelProperty("状态(0未开始 1标注中 2标注完成 3提交复核(未复核) 4开始复核(复核中) 5复核通过(已复核) 6交付)")
	private String status;
	
	@ApiModelProperty("状态描述(0未开始 1标注中 2标注完成 3提交复核(未复核) 4开始复核(复核中) 5复核通过(已复核) 6交付)")
	private String statusDesc;


}
