package cn.staitech.anno.domain.vo.specialImageAnno;


import java.util.ArrayList;
import java.util.List;

import cn.staitech.anno.domain.specialAnnotation.SpecialAnnotation;
import cn.staitech.anno.domain.specilaImage.SpecialImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    private List<SpecialAnnotation> annoList  = new ArrayList<SpecialAnnotation>();

}
