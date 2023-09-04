package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * 图像子表，存储AI切片 tb_sub_image
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("tb_sub_image")
public class SubImage implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 图像id
     */
    @TableId(value = "image_id", type = IdType.AUTO)
    @ApiModelProperty(hidden = true)
    private Long imageId;

    /**
     * 性别（0雌，1雄）
     */
    private Integer gender;

    /**
     * 专题id
     */
    @ApiModelProperty(hidden = true)
    private Long specialId;

    /**
     * 切片批次号 默认为第一批次
     */
    @ApiModelProperty(hidden = true)
    private Long sliceBatchNumber;

    /**
     * 审核状态 0：待审核 1：审核通过 2：审核不通过
     */
    @ApiModelProperty(value = "审核状态 0：待审核 1：审核通过 2：审核不通过")
    private Integer auditStatus;

    /**
     * 图像名称
     */
    @ApiModelProperty(value = "文件名称（文件名）")
    private String imageName;

    /**
     * 图像编码
     */
    @ApiModelProperty(value = "图像编码")
    private String imageCode;

    @ApiModelProperty(value = "原图像编码")
    private String parentImageCode;

    @ApiModelProperty(value = "原图像名称")
    private String parentImageName;
    /**
     * 原始图像id
     */
    @ApiModelProperty(hidden = true)
    private Long parentImageId;

    /**
     * 图像url地址
     */
    @ApiModelProperty(hidden = true)
    private String imageUrl;

    /**
     * 图片绝对路径
     */
    @ApiModelProperty(hidden = true)
    private String imagePath;

    /**
     * 缩略图url地址
     */
    @ApiModelProperty(hidden = true)
    private String thumbUrl;

    /**
     * macro图片URL地址
     */
    @ApiModelProperty(hidden = true)
    private String macroUrl;

    /**
     * label图片URL地址
     */
    @ApiModelProperty(hidden = true)
    private String labelUrl;

    /**
     * 文件格式
     */
    @ApiModelProperty(hidden = true)
    private String format;

    /**
     * 宽度
     */
    @ApiModelProperty(hidden = true)
    private String width;

    /**
     * 高度
     */
    @ApiModelProperty(hidden = true)
    private String height;

    /**
     * 深度
     */
    @ApiModelProperty(hidden = true)
    private String depth;

    /**
     * 大小
     */
    @ApiModelProperty(hidden = true)
    private String size;

    /**
     * 大小
     */
    @ApiModelProperty(hidden = true)
    private String globalSize;

    /**
     * 分辨率
     */
    @ApiModelProperty(hidden = true)
    private String resolvingPower;

    /**
     * 每层的切片个数
     */
    @ApiModelProperty(hidden = true)
    private String tileCountList;

    /**
     * 总层数
     */
    @ApiModelProperty(hidden = true)
    private Integer levelCount;

    /**
     * 前端总切片个数
     */
    @ApiModelProperty(hidden = true)
    private Integer chunkTotal;

    /**
     * 图片的Md5值
     */
    @ApiModelProperty(hidden = true)
    private String md5;

    /**
     * x轴分辨率
     */
    @ApiModelProperty(hidden = true)
    private String resolutionX;

    /**
     * y轴分辨率
     */
    @ApiModelProperty(hidden = true)
    private String resolutionY;

    /**
     * 原放大倍数
     */
    @ApiModelProperty(hidden = true)
    private Integer sourceLens;

    /**
     * 图片更新状态(-2上传失败，-1图像不可用，0分片合并及生成缩略图处理中，,1合并且生成缩略图（可显示）,2文件以经传输（不可见）)
     */
    @ApiModelProperty(hidden = true)
    private Integer processFlag;

    @ApiModelProperty(hidden = true)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long updateBy;

    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private String remark;

    /**
     * 脏器类型
     */
    @ApiModelProperty(hidden = true)
    private Integer visceraType;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除(0未删除 1已删除)")
    private Integer isDelete;
    
    
    @ApiModelProperty(value = "标注对应annId")
    private Long specialAnnotationId;
    
    @ApiModelProperty(value = "specialImage")
	private Long specialImageId;
    
    @ApiModelProperty(value = "脏器名称")
    @TableField(exist = false)
    private String visceraName;



}
