package cn.staitech.anno.vo.marking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MarkingMerge {

    @ApiModelProperty(value = "id集合")
    private List<String> markingIdList;


}
