package cn.staitech.anno.domain.vo.specialImage;


import cn.staitech.anno.domain.specilaImage.SpecialImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: SpecialImageListVO
 * @Description:专题-片列表
 * @author wanglibei
 * @date 2023年6月2日
 * @version V1.0
 */
@Data
public class SpecialImageVO extends SpecialImage {


	@ApiModelProperty(value = "切片编号")
	private String imageCode;
	
	@ApiModelProperty(value = "切片名称")
	private String imageName;
	
	@ApiModelProperty(value = "缩略图")
	private String thumbUrl;

	@ApiModelProperty(value = "所属专题-专题名称")
	private String specialName;
	
	@ApiModelProperty(value = "原图url")
	private String imageUrl;
	
	@ApiModelProperty(value = "原图地址")
	private String imagePath;
	
	@ApiModelProperty(value = "所在主机ID")
	private Integer hostId;

}