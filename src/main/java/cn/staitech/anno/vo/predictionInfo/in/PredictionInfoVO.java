package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @ClassName: PredictionInfoVO
 * @Description:
 * @author wanglibei
 * @date 2023年11月3日
 * @version V1.0
 */
@Data
public class PredictionInfoVO {
	@ApiModelProperty(value = "图片Id")
	private Long imageId;

	@ApiModelProperty(value = "图片名称", required = true)
	private String imageName;

	@ApiModelProperty(value = "算法生成的图片地址", required = true)
	private String algorithmImageUrl;
	
	@ApiModelProperty(value = "预测实际存放地址")
	private String predictionImageUrl;

	@ApiModelProperty(value = "图片大小", required = true)
	private String size;

	@ApiModelProperty(value = "md5", required = true)
	private String md5;

	@ApiModelProperty(value = "分片个数")
	private Integer chunkTotal;



	@ApiModelProperty(value = "机构ID", required = true)
	private Long organizationId;

	@ApiModelProperty(value = "专题名称")
	private String topicName;


	@ApiModelProperty(value = "登录人")
	private  Long userId ;
}
