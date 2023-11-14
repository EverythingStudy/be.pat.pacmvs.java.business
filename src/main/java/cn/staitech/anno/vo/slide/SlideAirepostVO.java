package cn.staitech.anno.vo.slide;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 切片详情
 */
@Data
public class SlideAirepostVO {
    @ApiModelProperty(value = "切片id")
    private Long slideId;
    @TableId(value = "image_id", type = IdType.AUTO)
    @ApiModelProperty(value = "图像id", hidden = true)
    private Long imageId;
    @TableField(value = "file_name")
    @ApiModelProperty(value = "无扩展名文件名称")
    private String fileName;
    @TableField(value = "image_name")
    @ApiModelProperty(value = "图像名称-文件名称（文件名）", hidden = true)
    private String imageName;
    @TableField(value = "image_url")
    @ApiModelProperty(value = "图像url地址", hidden = true)
    private String imageUrl;
    @TableField(value = "image_path")
    @ApiModelProperty(value = "图片绝对路径", hidden = true)
    private String imagePath;
    @TableField(value = "thumb_url")
    @ApiModelProperty(value = "缩略图url地址", hidden = true)
    private String thumbUrl;
    @TableField(value = "macro_url")
    @ApiModelProperty(value = "macro图片URL地址", hidden = true)
    private String macroUrl;
    @TableField(value = "label_url")
    @ApiModelProperty(value = "label图片URL地址", hidden = true)
    private String labelUrl;
    @TableField(value = "format")
    @ApiModelProperty(value = "文件格式", hidden = true)
    private String format;
    @TableField(value = "width")
    @ApiModelProperty(value = "宽度", hidden = true)
    private Double width;
    @TableField(value = "height")
    @ApiModelProperty(value = "高度", hidden = true)
    private Double height;
    @ApiModelProperty(value = "预测缩略图ID")
    private Long predictionImageId;
}
