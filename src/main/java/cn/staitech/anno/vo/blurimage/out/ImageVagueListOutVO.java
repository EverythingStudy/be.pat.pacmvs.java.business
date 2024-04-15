package cn.staitech.anno.vo.blurimage.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * ImageVagueListOutVO
 */
@Data
public class ImageVagueListOutVO {
    @ApiModelProperty(value = "切片ID（原图像ID）")
    private Long imageId;
    @ApiModelProperty(value = "图像预览")
    private String thumbUrl;
//    @ApiModelProperty(value = "图片（切片）编号")
//    private String imageCode;
    @ApiModelProperty(value = "图片（切片）编号")
    private String imageName;
    @ApiModelProperty(value = "专题ID")
    private Long topicId;
    @ApiModelProperty(value = "专题名称")
    private String topicName;
    @ApiModelProperty(value = "文件大小")
    private String size;
    @ApiModelProperty(value = "机构编号")
    private Long organizationId;
    @ApiModelProperty(value = "机构名称")
    private String organizationName;
    @ApiModelProperty(value = "创建时间/上传时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "模糊比例")
    private String fuzzyProportion;
    @ApiModelProperty(value = "是否条带状模糊（0：初始值 1：是 2：不是）")
    private Integer stripFuzzy;
    @ApiModelProperty(value = "清晰度状态（0：初始值 1：更正 2：还原）")
    private Integer definitionStatus;
    @ApiModelProperty(value = "模糊程度 （0：初始值 1：模糊 2：不模糊）")
    private Integer fuzzyLevel;
    @ApiModelProperty(value = "是否多次模糊（0：初始值 1：是 2：不是）")
    private Integer multipleFuzzy;
}
