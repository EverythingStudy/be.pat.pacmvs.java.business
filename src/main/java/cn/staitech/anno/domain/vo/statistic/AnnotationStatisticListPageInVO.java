package cn.staitech.anno.domain.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AnnotationStatisticListPageInVO {

    @NotNull(message ="{AnnotationStatisticListPageInVO.statisticDimension.isnull}" )
    @ApiModelProperty(value = "统计维度：项目、病理指标、标注类别、成员、图像", required = true)
    private Long statisticDimension;

    @NotNull(message = "{AnnotationStatisticListPageInVO.statisticCategory.isnull}")
    @ApiModelProperty(value = "统计类别：标注数量、图像数量", required = true)
    private Long statisticCategory;

    @ApiModelProperty(value = "专题id")
    private Long organizationId;

    @ApiModelProperty(value = "项目ID列表")
    private List<Integer> projectIdList;

    @ApiModelProperty(value = "病理指标ID列表")
    private List<Integer> indicatorIdList;

    @ApiModelProperty(value = "标注类别ID列表")
    private List<Integer> categoryIdList;

    @ApiModelProperty(value = "成员ID列表")
    private List<Integer> userIdList;

    @ApiModelProperty(value = "图像ID列表")
    private List<Integer> slideIdList;

    @ApiModelProperty(value = "标注状态")
    private Long examinationFlag;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @NotNull(message = "{AnnotationStatisticListPageInVO.statisticId.isnull}")
    @ApiModelProperty(value = "分页请求的统计维度ID", required = true)
    private Long statisticId;
}
