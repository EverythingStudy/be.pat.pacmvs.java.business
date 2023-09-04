package cn.staitech.anno.domain.vo.specialImageAnno;

import cn.staitech.anno.domain.specialAnnotation.SpecialAnnotation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SpecialAnnotationsDetailVO extends SpecialAnnotation {
    
    

    /**
     * 数量 .
     */
    @ApiModelProperty(hidden = true,value = "数量")
    private int sum;

    /**
     * 标注颜色RGB值 .
     */
    @ApiModelProperty(value = "标注（类别）颜色RGB值")
    private String color;

    @ApiModelProperty(value = "标注类别名称")
    private String categoryName;


    /**
     * 创建者用户名 .
     */
    @ApiModelProperty(value = "标注创建者")
    private String createName;

    /**
     * 标注更新者用户名 .
     */
    @ApiModelProperty(value = "标注更新者用户名")
    private String updateName;

    /**
     * ROI正方形标注边长512或1024;默认512.
     */
    @ApiModelProperty(hidden = true,value = "ROI正方形标注边长512或1024，默认512")
    private int distance = 512 ;
}
