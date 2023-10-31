package cn.staitech.anno.vo.image.in;

import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ImageAllVO extends BaseEntity {
    /**
     * 图像名称
     */
    @ApiModelProperty(value = "文件名称（文件名）")
    private String imageName;

    @ApiModelProperty(value = "", hidden = true)
    private String searchValue;

    @ApiModelProperty(value = "", hidden = true)
    private Long createBy;

    @ApiModelProperty(value = "", hidden = true)
    private Long updateBy;

    @ApiModelProperty(value = "", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "", hidden = true)
    private String remark;

    @ApiModelProperty(hidden = true, value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    @ApiModelProperty(value = "图片（切片）编号")
    private String imageCode;

    @ApiModelProperty(value = "专题ID")
    private Long topicId;
    @ApiModelProperty(value = "专题名称")
    private String topicName;
    @ApiModelProperty(value = "是否可用:0不可用1可用")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除状态:（0删除，1未删除）")
    private Integer deleteFlag;
}
