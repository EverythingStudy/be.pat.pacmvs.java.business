package cn.staitech.anno.domain;

import cn.staitech.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标注图像
 */
@Data
public class SlideViewer extends BaseEntity {
    /**
     * 切片ID
     */
    @ApiModelProperty(value = "切片ID")
    private Long slideId;
    
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Long projectId;
    
    /**
     * 图像ID
     */
    @ApiModelProperty(value = "图像ID")
    private Long imageId;
    
    /**
     * 人工标注数
     */
    @ApiModelProperty(value = "人工标注数")
    private Integer humanAnnotationTotal;
    
    /**
     * 标注状态 (0未开始 1标注中 2标注完成 3已提交复核)
     */
    @ApiModelProperty(value = "标注状态 (0未开始 1标注中 2标注完成 3已提交复核)")
    private Integer processFlag;
    
    /**
     * 复核状态 (0未提交 1已提交复核(未复核) 2复核通过 3驳回 4交付)
     */
    @ApiModelProperty(value = "复核状态 (1未复核 2复核通过 3驳回 4交付)")
    private Integer examinationFlag;

    /**
     * 图像名称
     */
    @ApiModelProperty(value = "图像名称")
    private String imageName;
    
    /**
     * 图像url地址
     */
    @ApiModelProperty(value = "图像url地址")
    private String imageUrl;
    
    /**
     * 图片绝对路径
     */
    @ApiModelProperty(value = "图片绝对路径")
    private String imagePath;
    
    /**
     * 缩略图url地址
     */
    @ApiModelProperty(value = "缩略图url地址")
    private String thumbUrl;
    
    /**
     * macro图片URL地址
     */
    @ApiModelProperty(value = "macro图片URL地址")
    private String macroUrl;
    
    /**
     * label图片URL地址
     */
    @ApiModelProperty(value = "label图片URL地址")
    private String labelUrl;
    
    /**
     * 文件格式
     */
    @ApiModelProperty(value = "文件格式")
    private String format;
    
    /**
     * 宽度
     */
    @ApiModelProperty(value = "宽度")
    private String width;
    
    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private String height;
    
    /**
     * 深度
     */
    @ApiModelProperty(value = "深度")
    private String depth;
    
    /**
     * 大小
     */
    @ApiModelProperty(value = "大小")
    private String size;
    
    /**
     * 全局大小
     */
    @ApiModelProperty(value = "全局大小")
    private String globalSize;
    
    /**
     * 分辨率
     */
    @ApiModelProperty(value = "分辨率")
    private String resolvingPower;
    
    /**
     * 每层的切片个数
     */
    @ApiModelProperty(value = "每层的切片个数")
    private String tileCountList;
    
    /**
     * 总层数
     */
    @ApiModelProperty(value = "总层数")
    private Integer levelCount;
    
    /**
     * 前端总切片个数
     */
    @ApiModelProperty(value = "前端总切片个数")
    private Integer chunkTotal;
    
    /**
     * 图片的Md5值
     */
    @ApiModelProperty(value = "图片的Md5值")
    private String md5;
    
    /**
     * x轴分辨率
     */
    @ApiModelProperty(value = "x轴分辨率")
    private String resolutionX;
    
    /**
     * y轴分辨率
     */
    @ApiModelProperty(value = "y轴分辨率")
    private String resolutionY;
    
    /**
     * 原放大倍数
     */
    @ApiModelProperty(value = "原放大倍数")
    private Integer sourceLens;
}