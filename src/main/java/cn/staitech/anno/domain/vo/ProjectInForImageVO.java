package cn.staitech.anno.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@Data
public class ProjectInForImageVO {
    /**
     * 切片ID
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long slideId;

    /**
     * 切片名称
     */
    @ApiModelProperty(value = "切片名称")
    private String imageName;

    /**
     * 标注状态
     */
    @ApiModelProperty(value = "标注状态")
    private Integer processFlag;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 项目id
     */
    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段:人工总数human_annotation_total、切片创建时间create_time、切片更新时间update_time", hidden = true)
    private String orderBy;

    /**
     * 排序顺序
     */
    @ApiModelProperty(value = "排序顺序:倒序desc、正序asc", hidden = true)
    private String sort;

    /**
     * 标注者id
     */
    @ApiModelProperty(value = "标注者id")
    private Integer taggerId;

    @NotNull(message = "{ProjectInforImageVO.pageNum.isnull}")
    @ApiModelProperty(value = "第几页", required = true)
    private Integer pageNum;

    @NotNull(message = "{ProjectInforImageVO.pageSize.isnull}")
    @ApiModelProperty(value = "每页记录数", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "", hidden = true)
    private Long createBy;

    @ApiModelProperty(value = "", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "", hidden = true)
    private Long updateBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("请求参数")
    private Map<String, Object> params;

    /**
     * 标注类别id
     */
    @ApiModelProperty(value = "标注类别id")
    private Integer categoryId;
}
