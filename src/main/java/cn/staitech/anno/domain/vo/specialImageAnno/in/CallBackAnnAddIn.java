package cn.staitech.anno.domain.vo.specialImageAnno.in;

import java.util.List;

import cn.staitech.anno.domain.specilaImage.SpecialImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CallBackAnnAddIn extends SpecialImage{
	
	@ApiModelProperty(value = "标注坐标")
	private List<CallBackAnnGeometry> geometryList;
	
}
