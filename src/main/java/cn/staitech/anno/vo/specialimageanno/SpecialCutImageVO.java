package cn.staitech.anno.vo.specialimageanno;


import cn.staitech.anno.vo.special.SpecialAnnotation;
import cn.staitech.anno.vo.special.SpecialImage;
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
