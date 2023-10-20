package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * tb_slide
 * @TableName tb_slide
 */
@TableName(value ="tb_slide")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Slide implements Serializable {
    /**
     * 切片ID
     */
    @TableId(type = IdType.AUTO)
    private Long slideId;

    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;
    /**
     * 图像ID
     */
    @ApiModelProperty("图像ID")
    private Integer imageId;
    /**
     * 人工标注数
     */
    @ApiModelProperty("人工标注数")
    private Integer humanAnnotationTotal;
    /**
     * 算法标注数
     */
    @ApiModelProperty("算法标注数")
    private Integer algorithmAnnotationTotal;
    /**
     * 已审核切片数
     */
    @ApiModelProperty("已审核切片数")
    private Integer examinationSlideTotal;
    /**
     * 处理状态（0未处理,1处理中,2处理完成）
     */
    @ApiModelProperty("处理状态（0未处理,1处理中,2处理完成）")
    private Integer processFlag;
    /**
     * 复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核不通过 4交付)
     */
    @ApiModelProperty("复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核不通过 4交付)")
    private Integer examinationFlag;
    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private Long createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private Long updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 切片描述
     */
    @Size(max= 50,message="{PathologicalIndicatorCategory.number.length}")
    @ApiModelProperty("切片描述")
    @Length(max= 50,message="{PathologicalIndicatorCategory.number.length}")
    private String description;
    /**
     * 分组id
     */
    @ApiModelProperty("分组id")
    private Integer groupId;
    /**
     * 是否删除(0未删除 1已删除)
     */
    @ApiModelProperty("是否删除(0未删除 1已删除)")
    private Integer isDelete;
    /**
     * AI筛阴：1:阴性、2:阳性、0:未筛、3:未知
     */
    @ApiModelProperty("AI筛阴：1:阴性、2:阳性、0:未筛、3:未知")
    private Integer aiCheck;
    /**
     * AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败
     */
    @ApiModelProperty("AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败")
    private Integer aiAnalyzed;
    /**
     * 人工诊断状态：0未诊断，1已诊断
     */
    @ApiModelProperty("人工诊断状态：0未诊断，1已诊断")
    private Integer diagnosis;
    /**
     * geojson文件地址
     */
    @Size(max= 255,message="{projectType.length}")
    @ApiModelProperty("geojson文件地址")
    @Length(max= 255,message="{projectType.length}")
    private String geojsonUrl;
    /**
     * 备注
     */
    @Size(max= 4096,message="{Slide.remark.isnull}")
    @ApiModelProperty("备注")
    @Length(max= 4096,message="{Slide.remark.isnull}")
    private String remark;
    /**
     * 状态
     */
    @NotBlank(message="{Slide.status.isnull}")
    @Size(max= 1,message="{Slide.status.length}")
    @ApiModelProperty("状态(0未开始 1标注中 2标注完成 3提交复核(未复核) 4开始复核(复核中) 5复核通过(已复核) 6交付)")
    @Length(max= 1,message="{Slide.status.length}")
    private String status;

    @ApiModelProperty("json文件切片id")
    private String geoImageId;

    /**
     * 评审轮次id
     */
    @ApiModelProperty(value = "评审轮次id")
    private Long reviewRoundId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Slide other = (Slide) that;
        return (this.getSlideId() == null ? other.getSlideId() == null : this.getSlideId().equals(other.getSlideId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getImageId() == null ? other.getImageId() == null : this.getImageId().equals(other.getImageId()))
            && (this.getHumanAnnotationTotal() == null ? other.getHumanAnnotationTotal() == null : this.getHumanAnnotationTotal().equals(other.getHumanAnnotationTotal()))
            && (this.getAlgorithmAnnotationTotal() == null ? other.getAlgorithmAnnotationTotal() == null : this.getAlgorithmAnnotationTotal().equals(other.getAlgorithmAnnotationTotal()))
            && (this.getExaminationSlideTotal() == null ? other.getExaminationSlideTotal() == null : this.getExaminationSlideTotal().equals(other.getExaminationSlideTotal()))
            && (this.getProcessFlag() == null ? other.getProcessFlag() == null : this.getProcessFlag().equals(other.getProcessFlag()))
            && (this.getExaminationFlag() == null ? other.getExaminationFlag() == null : this.getExaminationFlag().equals(other.getExaminationFlag()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getAiCheck() == null ? other.getAiCheck() == null : this.getAiCheck().equals(other.getAiCheck()))
            && (this.getAiAnalyzed() == null ? other.getAiAnalyzed() == null : this.getAiAnalyzed().equals(other.getAiAnalyzed()))
            && (this.getDiagnosis() == null ? other.getDiagnosis() == null : this.getDiagnosis().equals(other.getDiagnosis()))
            && (this.getGeojsonUrl() == null ? other.getGeojsonUrl() == null : this.getGeojsonUrl().equals(other.getGeojsonUrl()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSlideId() == null) ? 0 : getSlideId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getImageId() == null) ? 0 : getImageId().hashCode());
        result = prime * result + ((getHumanAnnotationTotal() == null) ? 0 : getHumanAnnotationTotal().hashCode());
        result = prime * result + ((getAlgorithmAnnotationTotal() == null) ? 0 : getAlgorithmAnnotationTotal().hashCode());
        result = prime * result + ((getExaminationSlideTotal() == null) ? 0 : getExaminationSlideTotal().hashCode());
        result = prime * result + ((getProcessFlag() == null) ? 0 : getProcessFlag().hashCode());
        result = prime * result + ((getExaminationFlag() == null) ? 0 : getExaminationFlag().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getAiCheck() == null) ? 0 : getAiCheck().hashCode());
        result = prime * result + ((getAiAnalyzed() == null) ? 0 : getAiAnalyzed().hashCode());
        result = prime * result + ((getDiagnosis() == null) ? 0 : getDiagnosis().hashCode());
        result = prime * result + ((getGeojsonUrl() == null) ? 0 : getGeojsonUrl().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", slideId=").append(slideId);
        sb.append(", projectId=").append(projectId);
        sb.append(", imageId=").append(imageId);
        sb.append(", humanAnnotationTotal=").append(humanAnnotationTotal);
        sb.append(", algorithmAnnotationTotal=").append(algorithmAnnotationTotal);
        sb.append(", examinationSlideTotal=").append(examinationSlideTotal);
        sb.append(", processFlag=").append(processFlag);
        sb.append(", examinationFlag=").append(examinationFlag);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", description=").append(description);
        sb.append(", groupId=").append(groupId);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", aiCheck=").append(aiCheck);
        sb.append(", aiAnalyzed=").append(aiAnalyzed);
        sb.append(", diagnosis=").append(diagnosis);
        sb.append(", geojsonUrl=").append(geojsonUrl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}