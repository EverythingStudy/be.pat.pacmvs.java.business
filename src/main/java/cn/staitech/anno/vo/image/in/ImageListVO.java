package cn.staitech.anno.vo.image.in;

import cn.staitech.anno.domain.Pager;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author wangf
 */
@Data
public class ImageListVO extends Pager implements Serializable {
    @ApiModelProperty(value = "图像ID", hidden = true)
    private Long imageId;
    @ApiModelProperty(value = "无扩展名文件名称")
    private String fileName;
    @ApiModelProperty(value = "图像名称/文件名称（文件名）")
    private String imageName;
    @ApiModelProperty(value = "图像url地址", hidden = true)
    private String imageUrl;
    @ApiModelProperty(value = "图片绝对路径", hidden = true)
    private String imagePath;
    @ApiModelProperty(value = "缩略图url地址", hidden = true)
    private String thumbUrl;
    @ApiModelProperty(value = "macro图片URL地址", hidden = true)
    private String macroUrl;
    @ApiModelProperty(value = "label图片URL地址", hidden = true)
    private String labelUrl;
    @ApiModelProperty(value = "文件格式", hidden = true)
    private String format;
    @ApiModelProperty(value = "宽度", hidden = true)
    private String width;
    @ApiModelProperty(value = "高度", hidden = true)
    private String height;
    @ApiModelProperty(value = "深度", hidden = true)
    private String depth;
    @ApiModelProperty(value = "大小", hidden = true)
    private String size;
    @ApiModelProperty(value = "大小", hidden = true)
    private String globalSize;
    @ApiModelProperty(value = "分辨率", hidden = true)
    private String resolvingPower;
    @ApiModelProperty(value = "每层的切片个数", hidden = true)
    private String tileCountList;
    @ApiModelProperty(value = "总层数", hidden = true)
    private Integer levelCount;
    @ApiModelProperty(value = "前端总切片个数", hidden = true)
    private Integer chunkTotal;
    @ApiModelProperty(value = "图片的Md5值", hidden = true)
    private String md5;
    @ApiModelProperty(value = "x轴分辨率", hidden = true)
    private String resolutionX;
    @ApiModelProperty(value = "y轴分辨率", hidden = true)
    private String resolutionY;
    @ApiModelProperty(value = "原放大倍数", hidden = true)
    private Integer sourceLens;
    @ApiModelProperty(value = "", hidden = true)
    private String searchValue;
    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createBy;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "修改人", hidden = true)
    private Long updateBy;
    @ApiModelProperty(value = "修改时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "切片编号")
    private Long slideId;
    @ApiModelProperty(value = "人工标注总数")
    private Integer humanAnnotationTotal;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "图片（切片）编号")
    private String imageCode;
    @ApiModelProperty(value = "专题ID")
    private Long topicId;
    @ApiModelProperty(value = "专题名称")
    private String topicName;
    @ApiModelProperty(value = "0上传中、1上传失败、2解析中、3解析失败、4可用")
    private Integer status;
    @ApiModelProperty(value = "创建时间-查询入参")
    private Map<String, Object> createTimeParams;
    @ApiModelProperty(value = "机构编号")
    private Long organizationId;
    @ApiModelProperty(value = "机构名称")
    private String organizationName;
    @ApiModelProperty(value = "轮次ID-1到10")
    private Long roundId;
    @ApiModelProperty(value = "业务类型:1原始切片（默认）、2预测切片", hidden = true)
    private Integer bizType;
    @ApiModelProperty(value = "图像来源(1前端上传，2目录选片，3TCP客户端上传)", hidden = true)
    private Integer source;
    @ApiModelProperty(value = "文件夹ID")
    private Long folderId;
    @ApiModelProperty(value = "创建人")
    @TableField(exist = false)
    private String nickName;

}
