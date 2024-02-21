package cn.staitech.anno.vo.marking;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MarkingExamineInsertVO {
    @ApiModelProperty(value = "front_id")
    private String front_id;

    //@NotNull(message = "{MarkingExamineInsertVO.questionProjectId.isnull}")
    @ApiModelProperty(value = "项目题库id")
    private Long question_project_id;

    //@NotNull(message = "{MarkingExamineInsertVO.questionProjectId.isnull}")
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

    @ApiModelProperty(value = "标注类型")
    private String location_type;

    @ApiModelProperty(value = "要执行的操作(UNION:相交,DIFFERENCE:相差,UPDATE:修改,DELETE:删除,添加:INSERT,null)")
    private String operation;
}
