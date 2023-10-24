package cn.staitech.anno.domain.vo.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class StatisticListInVO {
    @NotNull(message = "{AnnotationStatisticListPageInVO.statisticDimension.isnull}")
    @ApiModelProperty(value = "统计维度：项目、病理指标、标注类别、成员、图像", required = true)
    private Long statisticDimension;

    @NotNull(message = "{StatisticListInVO.statisticCategory.isnull}")
    @ApiModelProperty(value = "显示数量：标注数量、图像数量", required = true)
    private Long statisticCategory;

    @ApiModelProperty(value = "项目列表")
    private List<Integer> projectIdList;

    @ApiModelProperty(value = "病理指标列表")
    private List<Integer> indicatorIdList;

    @ApiModelProperty(value = "标注类别列表")
    private List<Integer> categoryIdList;

    @ApiModelProperty(value = "成员列表")
    private List<Integer> userIdList;

    @ApiModelProperty(value = "图像列表")
    private List<Integer> slideIdList;

    @ApiModelProperty(value = "标注状态")
    private Long examinationFlag;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @JsonIgnore
    @ApiModelProperty(value = "后端使用字段")
    private Long organizationId;
    @JsonIgnore
    @ApiModelProperty(value = "后端使用字段")
    private Long member;
}
