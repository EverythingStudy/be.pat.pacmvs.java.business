package cn.staitech.anno.domain.marking;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MarkingExamineUpdateVO {
    @NotNull(message = "{MarkingExamineInsertVO.questionProjectId.isnull}")
    @ApiModelProperty(value = "项目题库id")
    private Long marking_id;

    @ApiModelProperty(value = "面积")
    private String area;

    @ApiModelProperty(value = "周长")
    private String perimeter;

    @ApiModelProperty(value = "标注坐标")
    private JSONObject geometry;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "标注颜色id")
    private Long category_id;
}
