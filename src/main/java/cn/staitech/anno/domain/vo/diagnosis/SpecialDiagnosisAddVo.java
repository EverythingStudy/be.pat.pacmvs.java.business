package cn.staitech.anno.domain.vo.diagnosis;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: TbSpecialDiagnosis
 * @Description:
 * @date 2023年6月28日
 */
@Api(value = "人工诊断添加", tags = "人工诊断添加")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialDiagnosisAddVo extends SpecialDiagnosisVo {


    @ApiModelProperty(name = "positionWord", value = "position自定义")
    private String positionWord;


    @ApiModelProperty(name = "lesionWord", value = "lesion自定义")
    private String lesionWord;


    @ApiModelProperty(name = "ddefinitionWord", value = "ddefinition自定义")
    private String ddefinitionWord;


    @ApiModelProperty(name = "gradeWord", value = "grade自定义")
    private String gradeWord;

    @ApiModelProperty(name = "visceraWord", value = "viscera自定义")
    private String visceraWord;


}
