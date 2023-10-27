package cn.staitech.anno.domain.diagnosis;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: TbSpecialDiagnosis
 * @Description:
 * @date 2023年6月28日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialDiagnosis {
    @ApiModelProperty(name = "specialDiagnosisId", value = "诊断ID")
    private Long specialDiagnosisId;

    @ApiModelProperty(name = "specialId", value = "专题id")
    private Long specialId;

    @ApiModelProperty(name = "projectId", value = "项目ID")
    private Long projectId;

    @ApiModelProperty(name = "subImageId", value = "新切片ID（切好的单脏器）")
    private Long subImageId;

    @ApiModelProperty(name = "groupId", value = "分组id")
    private Long groupId;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "diagnosisStatus", value = "诊断状态 1：未诊断 1：已诊断")
    private Integer diagnosisStatus;

    @ApiModelProperty(name = "status", value = "是否可用 0:不可用1:可用")
    private Integer status;

    @ApiModelProperty(name = "deleteFlag", value = "逻辑删除状态（0:删除 1:未删除）")
    private Integer deleteFlag;

    @ApiModelProperty(name = "createBy", value = "创建人id")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "更新人id")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
