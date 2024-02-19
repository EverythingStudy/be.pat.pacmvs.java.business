package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 标注统计-图像标注统计
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_project_anno_statistics")
@ApiModel(value="ProjectAnnoStatistics对象", description="标注统计-图像标注统计")
public class ProjectAnnoStatistics implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "project_anno_statistics_id", type = IdType.AUTO)
    private Long projectAnnoStatisticsId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "状态:1待启动，2进行中，3暂停，4已完成")
    private Integer projectStatus;

    @ApiModelProperty(value = "标注用户id")
    private Long annoUserId;

    @ApiModelProperty(value = "标注用户昵称")
    private String annoNickName;

    @ApiModelProperty(value = "图像数量")
    private Long imageCount;

    @ApiModelProperty(value = "标注总数")
    private Long markingNum;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "任务创建时间")
    private Date taskCreateTime;
    
    @ApiModelProperty(value = "机构id")
    private Long organizationId;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "项目id列表")
    private List<Long> projectIdList;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "项目标注人员列表")
    private List<Long> projectAnnoUseridList;


}
