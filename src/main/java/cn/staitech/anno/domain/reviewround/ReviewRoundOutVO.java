package cn.staitech.anno.domain.reviewround;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangf
 * @TableName tb_review_round
 */
@Data
public class ReviewRoundOutVO implements Serializable {
    /**
     * 评审轮次自增ID
     */
    @ApiModelProperty(value = "评审轮次自增ID")
    private Long reviewRoundId;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 评审内容
     */
    @ApiModelProperty(value = "评审内容")
    private String reviewContent;

    /**
     * 评审轮次ID、对应1至10轮
     */
    @ApiModelProperty(value = "轮次类型ID")
    private Long roundId;

    /**
     * 专题ID
     */
    @ApiModelProperty(value = "专题ID")
    private Long topicId;

    /**
     * 组别ID、对应group1至8
     */
    @ApiModelProperty(value = "组别ID")
    private Long groupId;

    /**
     * 机构ID
     */
    @ApiModelProperty(value = "组织机构ID")
    private Long organizationId;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建人ID")
    private Long createBy;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "轮软名称")
    private String roundName;
    @ApiModelProperty(value = "专题名称")
    private String topicName;
    @ApiModelProperty(value = "分组名称")
    private String groupName;
    @ApiModelProperty(value = "创建人用户名")
    private String createByName;
    @TableField(exist = false)
    @ApiModelProperty(value = "是否绑定图片:0 未绑定,1已绑定")
    private Integer slideStatus = 0;
    /**
     * 图片绝对路径
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "", hidden = true)
    private String imagePath;
}