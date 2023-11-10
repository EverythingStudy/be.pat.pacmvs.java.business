package cn.staitech.anno.vo.airepost;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: wangfeng
 * @create: 2023-11-10 17:52:04
 * @Description: 拼接Viewer列表查询条件
 */
@Data
public class AirepostOutVO {
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
    private String msg;
    @ApiModelProperty(value = "图像物理路径")
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

