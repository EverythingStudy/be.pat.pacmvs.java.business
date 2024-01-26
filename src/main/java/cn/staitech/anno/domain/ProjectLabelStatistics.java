package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 标注统计-项目/标签 统计
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_project_label_statistics")
@ApiModel(value="ProjectLabelStatistics对象", description="标注统计-项目/标签 统计")
public class ProjectLabelStatistics implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "project_label_statistics_id", type = IdType.AUTO)
    private Long projectLabelStatisticsId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "状态:1待启动，2进行中，3暂停，4已完成")
    private Integer projectStatus;

    @ApiModelProperty(value = "项目描述")
    private String description;

    @ApiModelProperty(value = "项目创建者id")
    private Long projectCreateBy;

    @ApiModelProperty(value = "项目创建者昵称")
    private String createNickName;

    @ApiModelProperty(value = "项目创建时间")
    private Date projectCreateTime;

    @ApiModelProperty(value = "标签集id")
    private Long indicatorId;

    @ApiModelProperty(value = "标签集名称")
    private String indicatorName;

    @ApiModelProperty(value = "标签id")
    private Long categoryId;

    @ApiModelProperty(value = "标签名称")
    private String categoryName;

    @ApiModelProperty(value = "图像数量")
    private Long imageNum;

    @ApiModelProperty(value = "标注总数")
    private Long markingNum;
    
    @ApiModelProperty(value = "机构id")
    private Long organizationId;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "任务创建时间")
    private Date taskCreateTime;


}
