package cn.staitech.anno.vo.examine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExamineScoreExportVO {

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "切片编码")
    private String imageName;

    @ApiModelProperty(value = "答题者")
    private String nickName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "应标个数")
    private Long shouldNumber;

    @ApiModelProperty(value = "实标个数")
    private Long realityNumber;

    @ApiModelProperty(value = "算法拟合区间")
    private String miou;

    @ApiModelProperty(value = "算法拟合区间")
    private String fiou;

    @ApiModelProperty(value = "算法拟合区间")
    private String biou;

    @ApiModelProperty(value = "算法拟合区间")
    private String tiou;

    @ApiModelProperty(value = "个人拟合度")
    private String personalFit;

    @ApiModelProperty(value = "考试结果")
    private String examResults;

}
