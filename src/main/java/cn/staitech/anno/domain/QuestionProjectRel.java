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
 * 题库项目表
 * </p>
 *
 * @author author
 * @since 2023-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_question_project_rel")
@ApiModel(value="QuestionProjectRel对象", description="题库项目表")
public class QuestionProjectRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题库项目表")
    @TableId(value = "question_project_id", type = IdType.AUTO)
    private Long questionProjectId;

    @ApiModelProperty(value = "题库id")
    @TableField("question_id")
    private Long questionId;

    @ApiModelProperty(value = "项目id")
    @TableField("project_id")
    private Long projectId;

    @ApiModelProperty(value = "应标个数")
    @TableField("should_marks")
    private Long shouldMarks;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    @TableField("del_flag")
    private String delFlag;

    @ApiModelProperty(value = "图片编号")
    @TableField("image_code")
    private String imageCode;

    @ApiModelProperty(value = "json文件名称")
    @TableField("json_name")
    private String jsonName;

    @ApiModelProperty(value = "创建者")
    @TableField("create_by")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人名称")
    @TableField("create_name")
    private String createName;

    @ApiModelProperty(value = "更新者")
    @TableField("update_by")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;


}
