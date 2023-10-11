package cn.staitech.anno.domain.examineScore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamineScoreExportInsertVo {

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "审核评分id列表")
    private List<Long> examineScoreIdList;
}
