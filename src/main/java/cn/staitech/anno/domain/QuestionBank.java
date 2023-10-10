package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 题库表
 * </p>
 *
 * @author author
 * @since 2023-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_question_bank")
@ApiModel(value="QuestionBank对象", description="题库表")
public class QuestionBank implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题库id")
    @TableId(value = "question_id", type = IdType.AUTO)
    private Long questionId;

    @ApiModelProperty(value = "切片ID")
    @TableField("slide_id")
    private Long slideId;

    @ApiModelProperty(value = "图像ID")
    @TableField("image_id")
    private Long imageId;

    @ApiModelProperty(value = "项目ID")
    @TableField("project_id")
    private Long projectId;

    @ApiModelProperty(value = "图片编号")
    @TableField("image_code")
    private String imageCode;

    @ApiModelProperty(value = "图片名称")
    @TableField("image_name")
    private String imageName;

    @ApiModelProperty(value = "json文件名称")
    @TableField("json_name")
    private String jsonName;

    @ApiModelProperty(value = "切片大小")
    @TableField("size")
    private String size;

    @ApiModelProperty(value = "拟合区间")
    @TableField("fitting_interval")
    private String fittingInterval;

    @ApiModelProperty(value = "专题id")
    @TableField("topic_id")
    private Long topicId;

    @ApiModelProperty(value = "机构id")
    @TableField("organization_id")
    private Long organizationId;

    @ApiModelProperty(value = "拟合区间算法执行状态;0-未进行拟合区间计算;1-算法计算成功;2-算法计算失败")
    @TableField("algorithm_status")
    private String algorithmStatus;

    @ApiModelProperty(value = "创建者")
    @TableField("create_by")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    @TableField("update_by")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;


}
