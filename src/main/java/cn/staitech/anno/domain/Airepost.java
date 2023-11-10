package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 眼科-View图像列表
 *
 * @author wangfeng
 * @date 2023-11-10
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "眼科-View图像列表", description = "眼科-View图像列表")
@TableName("aipre_airepost")
public class Airepost {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID")
    @TableId(value = "report_uuid", type = IdType.AUTO)
    private Long reportUuid;
    @ApiModelProperty(value = "AI处理状态")
    private String aiStatus;
    @ApiModelProperty(value = "原始JSON数据")
    private String aiReportJson;
    @ApiModelProperty(value = "图像ID")
    private String imageUuidId;
    @ApiModelProperty(value = "项目ID")
    private String projectId;
    @ApiModelProperty(value = "")
    private Date startTime;
    @ApiModelProperty(value = "")
    private Date endTime;
    @ApiModelProperty(value = "")
    private Long wasteTime;
    @ApiModelProperty(value = "")
    private String msg;
    @ApiModelProperty(value = "")
    private String jsonAddr;
    @ApiModelProperty(value = "")
    private String taskId;
    @ApiModelProperty(value = "")
    private String graphicsCardModel;
    @ApiModelProperty(value = "")
    private String serverModel;
    @ApiModelProperty(value = "宽")
    private String width;
    @ApiModelProperty(value = "高")
    private String height;
    @ApiModelProperty(value = "中心点")
    private String center;
    @ApiModelProperty(value = "旋转角度（默认0）")
    private String rotation;
    @ApiModelProperty(value = "层级:解析不出的给0）")
    private String level;
    @ApiModelProperty(value = "是否主图：主图1、非主图0")
    private String primary;

}
