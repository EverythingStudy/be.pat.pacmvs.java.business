package cn.staitech.anno.domain.vo.specialImage;

import cn.staitech.anno.domain.specilaImage.SpecialImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InsertSpecialImageVO extends SpecialImage {

	@ApiModelProperty(required = true, value = "切片列表")
	private Long[] imageIdList;

}