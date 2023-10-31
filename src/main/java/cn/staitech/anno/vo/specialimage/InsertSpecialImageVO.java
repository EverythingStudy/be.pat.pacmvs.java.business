package cn.staitech.anno.vo.specialimage;

import cn.staitech.anno.vo.special.SpecialImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InsertSpecialImageVO extends SpecialImage {

    @ApiModelProperty(required = true, value = "切片列表")
    private Long[] imageIdList;

}