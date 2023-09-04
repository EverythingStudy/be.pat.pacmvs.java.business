package cn.staitech.anno.domain.vo;

import cn.staitech.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectSlideExcelVO {
    
    /**
     * 图像id
     */
    @ApiModelProperty(value = "图像id")
    private Long imageId;

    /**
     * 切片id
     */
    @ApiModelProperty(value = "切片id")
    @Excel(name = "切片id", cellType = Excel.ColumnType.NUMERIC)
    private Long slideId;
    
    /**
     * 切片名称
     */
    @ApiModelProperty(value = "切片名称")
    @Excel(name = "切片名称")
    private String imageName;

    /**
     * 平台名称
     */
    @ApiModelProperty(value = "平台名称")
    @Excel(name = "平台名称")
    private String platformName;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    @Excel(name = "项目名称")
    private String projectName;

    /**
     * 该切片标注总数
     */
    @ApiModelProperty(value = " 该切片标注总数")
    @Excel(name = "各切片的标注总数量")
    private Integer markTotalNum;

    /**
     * 标注类别名称
     */
    @ApiModelProperty(value = "各切片中每个标注类别的数量")
    @Excel(name = "各切片中每个标注类别的数量")
    private String categoryName;

    /**
     * 人工标注数量
     */
    @ApiModelProperty(value = "各切片中每个成员的标注数量")
    @Excel(name = "各切片中每个成员的标注数量")
    private String manMarkNum;
    
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
    @ApiModelProperty(value = "图像预览")
    private String thumbUrl;
    
    /**
     * macro图片URL地址
     */
    @ApiModelProperty(value = " macro图片URL地址")
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
    @ApiModelProperty(value = "文件大小")
    private String size;
    
    /**
     * 大小
     */
    @ApiModelProperty(value = "大小")
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

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 标注状态
     */
    @ApiModelProperty(value = "标注状态ID")
    private Long processFlag;
    
    /**
     * 标注状态名称
     */
    @ApiModelProperty(value = "标注状态名称")
    @Excel(name = "标注状态名称")
    private String processFlagName;
    
    /**
     * 标注类别id
     */
    @ApiModelProperty(value = "标注类别id")
    private Long categoryId;
}
