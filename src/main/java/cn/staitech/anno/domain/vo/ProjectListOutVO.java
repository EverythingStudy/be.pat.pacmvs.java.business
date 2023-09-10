package cn.staitech.anno.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ProjectListOutVO {

    /**
     * 切片id
     */
    @ApiModelProperty(hidden = true, value = "切片id")
    private Long slideId;

    /**
     * 图像名称
     */
    @ApiModelProperty(hidden = true, value = "图片名称")
    private String imageName;

    /**
     * 缩略图路径
     */
    @ApiModelProperty(hidden = true, value = "图片缩略图路径")
    private String thumbUrl;

    /**
     * 标注状态名称
     */
    @ApiModelProperty(hidden = true, value = "标注状态")
    private String dimensionStatus;

    /**
     * 人工标注数
     */
    @ApiModelProperty(value = "人工标注数")
    private Integer humanAnnotationTotal;

    /**
     * 人工标注数
     */
    @ApiModelProperty(hidden = true, value = "人工标注数")
    private List<Map<String, String>> manualMarkingList;

    /**
     * 标注类别
     */
    @ApiModelProperty(hidden = true, value = "标注类别")
    private List<Map<String, String>> dimensionCategoryList;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 标注状态
     */
    @ApiModelProperty(hidden = true, value = "标注状态值")
    private Integer processFlag;

    /**
     * 标注状态
     */
    @ApiModelProperty(hidden = true, value = "图片id")
    private Long imageId;

    @ApiModelProperty("更新时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;

    @ApiModelProperty("上传时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;

    /**
     * 大小
     */
    @ApiModelProperty(value = "", hidden = true)
    private String size;
}
