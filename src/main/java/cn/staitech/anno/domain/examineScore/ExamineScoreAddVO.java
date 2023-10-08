package cn.staitech.anno.domain.examineScore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamineScoreAddVO {

    @ApiModelProperty(value = "项目题库id")
    private Long questionProjectId;


}
