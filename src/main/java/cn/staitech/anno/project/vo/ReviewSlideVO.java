package cn.staitech.anno.project.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mugw
 * @version 1.0
 * @description 评审切片对象
 * @date 2023/9/22 14:18:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSlideVO {

    /**
     * 切片ID
     */
    private Long slideId;

    @ApiModelProperty("缩略图URL地址")
    private String thumbUrl;
    @ApiModelProperty("分值")
    private String score;
    @ApiModelProperty("评审状态")
    private String reviewStatus;


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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    /**
     * 评审状态 默认1：未审 2：已审
     */
    @ApiModelProperty("单审状态 1：未审 2：已审")
    private String selfReviewStatus;
}
