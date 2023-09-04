package cn.staitech.anno.domain.vo;

import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 复核图像
 */
@Data
public class ExaminationListVO extends BaseEntity {
    /**
     * 切片ID
     */
    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    /**
     * 项目ID
     */
    @ApiModelProperty(hidden = true)
    private Long projectId;

    /**
     * 图像ID
     */
    @ApiModelProperty(hidden = true)
    private Long imageId;

    /**
     * 人工标注数
     */
    @ApiModelProperty(value = "人工标注数")
    private Integer humanAnnotationTotal;

    /**
     * 标注状态 (0未开始 1标注中 2标注完成 3已提交复核)
     */
    @ApiModelProperty(hidden = true)
    private Integer processFlag;

    /**
     * 复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核未通过 4交付)
     */
    @ApiModelProperty(value = "复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核未通过 4交付)")
    private Integer examinationFlag;

    @ApiModelProperty(hidden = true)
    private String examinationFlagName;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "用户提交复核时间时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 图像名称
     */
    @ApiModelProperty(value = "切片名称")
    private String imageName;

    /**
     * 图像地址
     */
    @ApiModelProperty(hidden = true)
    private String imageUrl;

    /**
     * 图片绝对路径
     */
    @ApiModelProperty(hidden = true)
    private String imagePath;

    /**
     * 缩略图
     */
    @ApiModelProperty(value = "缩略图")
    private String thumbUrl;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "所属项目")
    private String projectName;

    /**
     * 病理指标ID
     */
    @ApiModelProperty(hidden = true)
    private Long indicatorId;

    /**
     * 病理指标名称
     */
    @ApiModelProperty(value = "关联病理指标")
    private String indicatorName;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户")
    private String userName;
}