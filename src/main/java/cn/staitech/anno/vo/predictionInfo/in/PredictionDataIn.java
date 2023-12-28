package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: CreateAssessmentDataIn
 * @Description:
 * @date 2023年11月6日
 */
@Data
public class PredictionDataIn {

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "算法模型名称")
    private String modelName;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "文件夹名称")
    private String folderName;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "组织id")
    private Long organizationId;

    @ApiModelProperty(value = "文件夹路径")
    private String folderUrl;

    @ApiModelProperty(value = "切片数据")
    private List<PreExecData> slideList;

    @ApiModelProperty(value = "机构编码")
    private String organizationNumber;


}
