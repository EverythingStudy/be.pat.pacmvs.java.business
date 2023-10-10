package cn.staitech.anno.domain.examineScore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SelectExaminationListVO {

    @ApiModelProperty(value = "项目题库id")
    private Long questionProjectId;

    @ApiModelProperty(value = "题库id")
    private Long questionId;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "切片编码")
    private String imageName;

    @ApiModelProperty(value = "答题者")
    private String nickName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "考试结果")
    private Long examResults;

    @ApiModelProperty(value = "操作状态")
    private Long operateStatus;
}
