package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tb_image_csv
 */
@TableName(value = "tb_image_csv")
@Data
public class ImageCsv implements Serializable {
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
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
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

    // =====================================================
    /**
     * 切片ID
     */
    @ApiModelProperty(value = "切片ID")
    @TableField(exist = false, value = "slide_id")
    private Long slideId;

    /**
     * 缩略图url地址
     */
    @TableField(exist = false, value = "thumb_url")
    @ApiModelProperty(value = "缩略图url地址", hidden = true)
    private String thumbUrl;

    /**
     * reviewRoundId
     */
    @TableField(exist = false, value = "review_round_id")
    @ApiModelProperty(value = "reviewRoundId", hidden = true)
    private Long reviewRoundId;
    @TableField(exist = false, value = "format")
    @ApiModelProperty(value = "文件格式", hidden = true)
    private String format;
    @TableField(exist = false, value = "width")
    @ApiModelProperty(value = "宽度", hidden = true)
    private String width;
    @TableField(exist = false, value = "height")
    @ApiModelProperty(value = "高度", hidden = true)
    private String height;

    @TableField(exist = false, value = "resolution_x")
    @ApiModelProperty(value = "x轴分辨率", hidden = true)
    private String resolutionX;
    @TableField(exist = false, value = "resolution_y")
    @ApiModelProperty(value = "y轴分辨率", hidden = true)
    private String resolutionY;
    @TableField(exist = false, value = "source_lens")
    @ApiModelProperty(value = "原放大倍数", hidden = true)
    private Integer sourceLens;
}