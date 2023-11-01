package cn.staitech.anno.vo.examination;

import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 复核图像
 */
@Data
public class ExaminationSelectVO extends BaseEntity {

    /**
     * 图像名称
     */
    @ApiModelProperty(value = "切片名称")
    private String imageName;

    /**
     * 复核状态 (0未提交 1已提交复核(未复核) 2复核通过 3驳回 4交付)
     */
    @ApiModelProperty(value = "复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核未通过 4交付)")
    private Integer examinationFlag;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "所属项目")
    private String projectName;

    @ApiModelProperty(value = "", hidden = true)
    private String searchValue;

    @ApiModelProperty(value = "", hidden = true)
    private Long createBy;

    @ApiModelProperty(value = "", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "", hidden = true)
    private Long updateBy;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户")
    private String userName;

    @ApiModelProperty(value = "", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "", hidden = true)
    private String remark;
}