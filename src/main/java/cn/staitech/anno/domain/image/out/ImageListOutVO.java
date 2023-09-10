package cn.staitech.anno.domain.image.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * ImageListOutVO
 *
 * @author wangfeng
 * @date 2023/06/01
 */
@Data
public class ImageListOutVO {
    @ApiModelProperty(value = "切片ID（原图像ID）")
    private Long imageId;
    @ApiModelProperty(value = "文件名称（文件名）")
    private String imageName;
    @ApiModelProperty(value = "图像url地址")
    private String imageUrl;
    @ApiModelProperty(value = "图片绝对路径")
    private String imagePath;
    @ApiModelProperty(value = "图像预览")
    private String thumbUrl;
    @ApiModelProperty(value = "宽度")
    private String width;
    @ApiModelProperty(value = "高度")
    private String height;
    @ApiModelProperty(value = "文件大小")
    private String size;
    @ApiModelProperty(value = "全局大小")
    private String globalSize;
    @ApiModelProperty(value = "原放大倍数")
    private Integer sourceLens;
    @ApiModelProperty(value = "图片状态:0分片合并及生成缩略图处理中，,1合并且生成缩略图（可显示）,2文件以经传输（不可见）")
    private Integer processFlag;
    @ApiModelProperty(value = "文件状态:0处理中,1不可用,2可用")
    private String processFlagName;
    @ApiModelProperty(value = "文件状态:0处理中,1不可用,2可用")
    private String fileStatus;
    @ApiModelProperty(value = "创建人id")
    private Long createBy;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新人id")
    private Long updateBy;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "图片（切片）编号")
    private String imageCode;
    @ApiModelProperty(value = "专题ID")
    private Long topicId;
    @ApiModelProperty(value = "专题名称")
    private String topicName;
    @ApiModelProperty(value = "可用状态:0不可用1可用")
    private Integer status;
    @ApiModelProperty(value = "逻辑删除状态:（0删除，1未删除）")
    private Integer deleteFlag;
    @ApiModelProperty(value = "机构编号")
    private Long organizationId;
    @ApiModelProperty(value = "机构名称")
    private String organizationName;
    @ApiModelProperty(value = "轮次ID-1到10")
    private Long roundId;
    @ApiModelProperty(value = "轮次名称")
    private String roundName;
    @ApiModelProperty(value = "业务类型:1原始切片（默认）、2预测切片")
    private Integer businessType;
    @ApiModelProperty(value = "业务类型名称:1原始切片（默认）、2预测切片")
    private String businessTypeName;
    @ApiModelProperty(value = "图像来源(1前端上传，2目录选片，3TCP客户端上传)")
    private Integer source;
}
