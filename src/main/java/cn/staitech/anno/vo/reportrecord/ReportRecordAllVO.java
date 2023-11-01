package cn.staitech.anno.vo.reportrecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportRecordAllVO {

    /**
     * 报告id
     */
    @ApiModelProperty(value = " 报告id")
    private Long reportId;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 数据格式
     */
    @ApiModelProperty(value = "数据格式")
    private String format;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = " 文件大小")
    private String fileSize;

    /**
     * 下载人员ID
     */
    @ApiModelProperty(value = "下载人员ID")
    private Long userId;

    /**
     * 下载人员
     */
    @ApiModelProperty(value = "下载人员")
    private String userName;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /**
     * 默认为0，删除为1
     */
    @ApiModelProperty(value = "删除状态")
    private Integer delFlag;

    /**
     * 报告类型（1单切片报告，2组件报告）
     */
    @ApiModelProperty(value = "报告类型号（1单切片报告，2组件报告）")
    private Integer reportType;

    @ApiModelProperty(value = "报告类型")
    private String reportTypeName;


    @ApiModelProperty(value = "专题id")
    private Long specialId;

    /**
     * 要导出的json数据
     */
    @ApiModelProperty(hidden = true, value = "要导出的json数据")
    private String reportFile;

    @ApiModelProperty(value = "任务进度")
    private Integer pace;

    @ApiModelProperty(value = "进度状态(0进行中，1成功，2失败)")
    private Integer status;

    @ApiModelProperty(value = "1给药结束安乐死、2恢复期结束安乐死")
    private Integer reasons;


    @ApiModelProperty(value = "报告地址")
    private String reportUrl;

}
