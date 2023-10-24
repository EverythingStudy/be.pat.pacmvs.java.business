package cn.staitech.anno.domain.vo.specialImageAnno;


import cn.staitech.anno.domain.SubImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpecialAnnoAndSubImageVO {

    /**
     * 标注结果列表
     */
    @ApiModelProperty(value = "标注结果列表")
    private List<SpecialAnnotationsDetailVO> detailList = new ArrayList<SpecialAnnotationsDetailVO>();

    /**
     * 切图列表
     */
    @ApiModelProperty(value = "切图列表")
    private List<SubImage> imageList = new ArrayList<SubImage>();


    @ApiModelProperty(value = "对应关系表")
    private List<SubRelationAnn> relationship = new ArrayList<SubRelationAnn>();


}
