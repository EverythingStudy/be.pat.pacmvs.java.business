package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangf
 * @TableName tb_image_csv
 */
@TableName(value = "tb_image_csv")
@Data
public class ImageCsv implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 图像ID
     */
    private Long imageId;
    /**
     * 切片号
     */
    private String imageName;
    /**
     * 组别
     */
    private String groupName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 种属
     */
    private String species;
    /**
     * 品系
     */
    private String productSeries;
    /**
     * 剂量
     */
    private String dosage;
    /**
     * 实验动物来源
     */
    private String animalSource;
    /**
     * 动物接收周龄
     */
    private String receivingWeek;
    /**
     * 动物给药周期
     */
    private String dosingCycle;
    /**
     * 动物恢复周期
     */
    private String recoveryCycle;
    /**
     * 死亡日期
     */
    private String dateOfDeath;
    /**
     * 移走原因
     */
    private String removeReason;
    /**
     * 备注
     */
    private String remark;
    /**
     * 脏器
     */
    private String organ;
    /**
     * 病变类型1
     */
    private String lesionType1;
    /**
     * 病变程度1
     */
    private String lesionDegree1;
    /**
     * 病变类型2
     */
    private String lesionType2;
    /**
     * 病变程度2
     */
    private String lesionDegree2;
    /**
     * 处理状态，不可用原因共三种，0上传失败（MD5校验不通过），1解析中，2解析失败（不能获得缩略图）（1.0：0上传未合并,1合并且生成缩略图,2传输图像）
     */
    private Integer processFlag;
    /**
     * 创建人id
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人id
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 专题ID
     */
    private Long topicId;
    /**
     * 专题名称
     */
    private String topicName;
    /**
     * 是否可用0不可用1可用
     */
    private Integer status;
    /**
     * 逻辑删除状态（0删除，1未删除）
     */
    private Integer deleteFlag;
    /**
     * 机构ID
     */
    private Long organizationId;
    /**
     * 所在主机ID
     */
    private Integer hostId;

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
        ImageCsv other = (ImageCsv) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getImageId() == null ? other.getImageId() == null : this.getImageId().equals(other.getImageId()))
                && (this.getImageName() == null ? other.getImageName() == null : this.getImageName().equals(other.getImageName()))
                && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
                && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
                && (this.getSpecies() == null ? other.getSpecies() == null : this.getSpecies().equals(other.getSpecies()))
                && (this.getProductSeries() == null ? other.getProductSeries() == null : this.getProductSeries().equals(other.getProductSeries()))
                && (this.getDosage() == null ? other.getDosage() == null : this.getDosage().equals(other.getDosage()))
                && (this.getAnimalSource() == null ? other.getAnimalSource() == null : this.getAnimalSource().equals(other.getAnimalSource()))
                && (this.getReceivingWeek() == null ? other.getReceivingWeek() == null : this.getReceivingWeek().equals(other.getReceivingWeek()))
                && (this.getDosingCycle() == null ? other.getDosingCycle() == null : this.getDosingCycle().equals(other.getDosingCycle()))
                && (this.getRecoveryCycle() == null ? other.getRecoveryCycle() == null : this.getRecoveryCycle().equals(other.getRecoveryCycle()))
                && (this.getDateOfDeath() == null ? other.getDateOfDeath() == null : this.getDateOfDeath().equals(other.getDateOfDeath()))
                && (this.getRemoveReason() == null ? other.getRemoveReason() == null : this.getRemoveReason().equals(other.getRemoveReason()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getOrgan() == null ? other.getOrgan() == null : this.getOrgan().equals(other.getOrgan()))
                && (this.getLesionType1() == null ? other.getLesionType1() == null : this.getLesionType1().equals(other.getLesionType1()))
                && (this.getLesionDegree1() == null ? other.getLesionDegree1() == null : this.getLesionDegree1().equals(other.getLesionDegree1()))
                && (this.getLesionType2() == null ? other.getLesionType2() == null : this.getLesionType2().equals(other.getLesionType2()))
                && (this.getLesionDegree2() == null ? other.getLesionDegree2() == null : this.getLesionDegree2().equals(other.getLesionDegree2()))
                && (this.getProcessFlag() == null ? other.getProcessFlag() == null : this.getProcessFlag().equals(other.getProcessFlag()))
                && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getTopicId() == null ? other.getTopicId() == null : this.getTopicId().equals(other.getTopicId()))
                && (this.getTopicName() == null ? other.getTopicName() == null : this.getTopicName().equals(other.getTopicName()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getDeleteFlag() == null ? other.getDeleteFlag() == null : this.getDeleteFlag().equals(other.getDeleteFlag()))
                && (this.getOrganizationId() == null ? other.getOrganizationId() == null : this.getOrganizationId().equals(other.getOrganizationId()))
                && (this.getHostId() == null ? other.getHostId() == null : this.getHostId().equals(other.getHostId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getImageId() == null) ? 0 : getImageId().hashCode());
        result = prime * result + ((getImageName() == null) ? 0 : getImageName().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getSpecies() == null) ? 0 : getSpecies().hashCode());
        result = prime * result + ((getProductSeries() == null) ? 0 : getProductSeries().hashCode());
        result = prime * result + ((getDosage() == null) ? 0 : getDosage().hashCode());
        result = prime * result + ((getAnimalSource() == null) ? 0 : getAnimalSource().hashCode());
        result = prime * result + ((getReceivingWeek() == null) ? 0 : getReceivingWeek().hashCode());
        result = prime * result + ((getDosingCycle() == null) ? 0 : getDosingCycle().hashCode());
        result = prime * result + ((getRecoveryCycle() == null) ? 0 : getRecoveryCycle().hashCode());
        result = prime * result + ((getDateOfDeath() == null) ? 0 : getDateOfDeath().hashCode());
        result = prime * result + ((getRemoveReason() == null) ? 0 : getRemoveReason().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getOrgan() == null) ? 0 : getOrgan().hashCode());
        result = prime * result + ((getLesionType1() == null) ? 0 : getLesionType1().hashCode());
        result = prime * result + ((getLesionDegree1() == null) ? 0 : getLesionDegree1().hashCode());
        result = prime * result + ((getLesionType2() == null) ? 0 : getLesionType2().hashCode());
        result = prime * result + ((getLesionDegree2() == null) ? 0 : getLesionDegree2().hashCode());
        result = prime * result + ((getProcessFlag() == null) ? 0 : getProcessFlag().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getTopicId() == null) ? 0 : getTopicId().hashCode());
        result = prime * result + ((getTopicName() == null) ? 0 : getTopicName().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDeleteFlag() == null) ? 0 : getDeleteFlag().hashCode());
        result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
        result = prime * result + ((getHostId() == null) ? 0 : getHostId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", imageId=" + imageId +
                ", imageName=" + imageName +
                ", groupName=" + groupName +
                ", gender=" + gender +
                ", species=" + species +
                ", productSeries=" + productSeries +
                ", dosage=" + dosage +
                ", animalSource=" + animalSource +
                ", receivingWeek=" + receivingWeek +
                ", dosingCycle=" + dosingCycle +
                ", recoveryCycle=" + recoveryCycle +
                ", dateOfDeath=" + dateOfDeath +
                ", removeReason=" + removeReason +
                ", remark=" + remark +
                ", organ=" + organ +
                ", lesionType1=" + lesionType1 +
                ", lesionDegree1=" + lesionDegree1 +
                ", lesionType2=" + lesionType2 +
                ", lesionDegree2=" + lesionDegree2 +
                ", processFlag=" + processFlag +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", topicId=" + topicId +
                ", topicName=" + topicName +
                ", status=" + status +
                ", deleteFlag=" + deleteFlag +
                ", organizationId=" + organizationId +
                ", hostId=" + hostId +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}