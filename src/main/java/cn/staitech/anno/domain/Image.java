package cn.staitech.anno.domain;

import cn.staitech.common.core.web.domain.BaseEntity;
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

import java.util.Date;
import java.util.Map;

/**
 * 图像表 tb_image
 *
 * @author WangFeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="tb_image")
public class Image extends BaseEntity {

    /**
     * 图像id
     */
    @TableId(value = "image_id", type = IdType.AUTO)
    @ApiModelProperty(hidden = true)
    private Long imageId;

    /**
     * 图像名称
     */
    @TableField(value = "image_name")
    @ApiModelProperty(value = "文件名称（文件名）",hidden = true)
    private String imageName;

    /**
     * 图像url地址
     */
    @TableField(value = "image_url")
    @ApiModelProperty(hidden = true)
    private String imageUrl;

    /**
     * 图片绝对路径
     */
    @TableField(value = "image_path")
    @ApiModelProperty(hidden = true)
    private String imagePath;

    /**
     * 缩略图url地址
     */
    @TableField(value = "thumb_url")
    @ApiModelProperty(hidden = true)
    private String thumbUrl;

    /**
     * macro图片URL地址
     */
    @TableField(value = "macro_url")
    @ApiModelProperty(hidden = true)
    private String macroUrl;

    /**
     * label图片URL地址
     */
    @TableField(value = "label_url")
    @ApiModelProperty(hidden = true)
    private String labelUrl;

    /**
     * 文件格式
     */
    @TableField(value = "format")
    @ApiModelProperty(hidden = true)
    private String format;

    /**
     * 宽度
     */
    @TableField(value = "width")
    @ApiModelProperty(hidden = true)
    private String width;

    /**
     * 高度
     */
    @TableField(value = "height")
    @ApiModelProperty(hidden = true)
    private String height;

    /**
     * 深度
     */
    @TableField(value = "depth")
    @ApiModelProperty(hidden = true)
    private String depth;

    /**
     * 大小
     */
    @TableField(value = "size")
    @ApiModelProperty(hidden = true)
    private String size;

    /**
     * 大小
     */
    @TableField(value = "global_size")
    @ApiModelProperty(hidden = true)
    private String globalSize;

    /**
     * 分辨率
     */
    @TableField(value = "resolving_power")
    @ApiModelProperty(hidden = true)
    private String resolvingPower;

    /**
     * 每层的切片个数
     */
    @TableField(value = "tile_count_list")
    @ApiModelProperty(hidden = true)
    private String tileCountList;

    /**
     * 总层数
     */
    @TableField(value = "level_count")
    @ApiModelProperty(hidden = true)
    private Integer levelCount;

    /**
     * 前端总切片个数
     */
    @TableField(value = "chunk_total")
    @ApiModelProperty(hidden = true)
    private Integer chunkTotal;

    /**
     * 图片的Md5值
     */
    @TableField(value = "md5")
    @ApiModelProperty(hidden = true)
    private String md5;

    /**
     * x轴分辨率
     */
    @TableField(value = "resolution_x")
    @ApiModelProperty(hidden = true)
    private String resolutionX;

    /**
     * y轴分辨率
     */
    @TableField(value = "resolution_y")
    @ApiModelProperty(hidden = true)
    private String resolutionY;

    /**
     * 原放大倍数
     */
    @TableField(value = "source_lens")
    @ApiModelProperty(hidden = true)
    private Integer sourceLens;

    /**
     * 图片更新状态(-2上传失败，-1图像不可用，0分片合并及生成缩略图处理中，,1合并且生成缩略图（可显示）,2文件以经传输（不可见）)
     */
    @TableField(value = "process_flag")
    @ApiModelProperty(hidden = true)
    private Integer processFlag;

    @ApiModelProperty(hidden = true)
    private String searchValue;
    @TableField(value = "create_by")
    @ApiModelProperty(hidden = true)
    private Long createBy;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "上传时间 - 创建时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(value = "update_by")
    @ApiModelProperty(hidden = true)
    private Long updateBy;

    @TableField(value = "update_time")
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private String remark;

    @TableField(value = "image_code")
    @ApiModelProperty(value = "切片编号")
    private String imageCode;

    @TableField(value = "topic_id")
    @ApiModelProperty(value = "所属专题", hidden = true)
    private Long topicId;

    @TableField(value = "topic_name")
    @ApiModelProperty(value = "所属专题-专题名称")
    private String topicName;

    @TableField(value = "status")
    @ApiModelProperty(value = "是否可用状态:0不可用1可用")
    private Integer status;

    @TableField(value = "delete_flag")
    @ApiModelProperty(value = "逻辑删除状态:（0删除，1未删除）",hidden = true)
    private Integer deleteFlag;


    @ApiModelProperty(value = "创建时间-查询入参")
    private Map<String, Object> createTimeParams;

    @ApiModelProperty(hidden = true)
    private Map<String, Object> params;

}
