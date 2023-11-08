package cn.staitech.anno.vo.predictionInfo.in;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: CreateAssessmentDataIn
* @Description:
* @author wanglibei
* @date 2023年11月6日
* @version V1.0
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
    
    @ApiModelProperty(value = "切片数据")
    private List<PreExecData> slideList;



}
