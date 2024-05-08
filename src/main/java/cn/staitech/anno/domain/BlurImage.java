package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wmy
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fr_blur_image")
@ApiModel(value = "BlurImage对象", description = "")
public class BlurImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "blur_id", type = IdType.AUTO)
    private Integer blurId;

    @ApiModelProperty(value = "切片ID")
    private Integer imageId;

    @ApiModelProperty(value = "切片名称")
    private String imageName;

    @ApiModelProperty(value = "JSON路径")
    private String jsonPath;
    
    @ApiModelProperty(value = "切片路径")
    private String imageUrl;

    @ApiModelProperty(value = "保存目录路径")
    private String savaPath;

    @ApiModelProperty(value = "缩略图路径")
    private String thumbUrl;

    @ApiModelProperty(value = "专题号")
    private String topicName;

    @ApiModelProperty(value = "专题ID")
    private Integer topicId;

    @ApiModelProperty(value = "切片编号")
    private String imageCode;

    @ApiModelProperty(value = "图片大小")
    private String size;

    @ApiModelProperty(value = "机构ID")
    private Integer organizationId;

    @ApiModelProperty(value = "上传时间")
    private Date createTime;

    @ApiModelProperty(value = "组织总块数")
    private String fuzzyCountChunk;

    @ApiModelProperty(value = "模糊总块数")
    private String fuzzyChunk;

    @ApiModelProperty(value = "是否存在条带状模糊1是2否")
    private Integer stripFuzzy;

    @ApiModelProperty(value = "是否模糊1是2否")
    private Integer fuzzyLevel;

    @ApiModelProperty(value = "是否手动修正1是2否")
    private Integer definitionStatus;

    @ApiModelProperty(value = "切片是否多次模糊1是2否")
    private Integer multipleFuzzy;


}
