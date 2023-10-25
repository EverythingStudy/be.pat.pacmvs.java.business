package cn.staitech.anno.domain.vo.specialimageanno.in;

import cn.staitech.anno.domain.special.SpecialImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CallBackAnnAddIn extends SpecialImage {

    @ApiModelProperty(value = "标注坐标")
    private List<CallBackAnnGeometry> geometryList;

}
