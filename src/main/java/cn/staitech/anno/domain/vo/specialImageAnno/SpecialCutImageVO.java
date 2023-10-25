package cn.staitech.anno.domain.vo.specialImageAnno;


import cn.staitech.anno.domain.special.SpecialAnnotation;
import cn.staitech.anno.domain.special.SpecialImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpecialCutImageVO {

    /**
     * SpecialImage
     */
    @ApiModelProperty(value = "SpecialImage信息")
    private SpecialImage image = new SpecialImage();

    /**
     * 标注结果列表
     */
    @ApiModelProperty(value = "标注结果列表")
    private List<SpecialAnnotation> annoList = new ArrayList<SpecialAnnotation>();

}
