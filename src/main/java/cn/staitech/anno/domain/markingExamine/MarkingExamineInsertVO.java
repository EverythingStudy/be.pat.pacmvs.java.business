package cn.staitech.anno.domain.markingExamine;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MarkingExamineInsertVO {

    @NotNull(message = "项目题库id不可为空")
    @ApiModelProperty(value = "项目题库id")
    private Long question_project_id;

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

    @ApiModelProperty(value = "标注类型")
    private String location_type;

}
