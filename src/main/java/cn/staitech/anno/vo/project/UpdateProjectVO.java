package cn.staitech.anno.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 新建项目
 * 项目名称 projectName
 * 种属	speciesId
 * 染色类型	colorType	(染色类型（1RGB，2HEX）)
 * 关联结构指标 indicatorId
 * 品系	product_series_id
 * 项目类型 	projectType	(项目类型:1标注2评审3标准训练集)
 * 机构 organizationId
 * 描述 description
 *
 * @author wangf
 */
@Data
public class UpdateProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(required = true, value = "项目ID")
    private Long projectId;
    @NotBlank(message = "{InsertProjectVO.projectName.isnull}")
    @ApiModelProperty(required = true, value = "项目名称")
    @Size(min = 0, max = 200, message = "{UpdateProjectVO.projectName.length}")
    private String projectName;

    /**
     * 种属ID
     */
    @ApiModelProperty("种属ID")
    private String speciesId;

    @ApiModelProperty("染色类型（1RGB，2HEX）")
    private Integer colorType;

    /**
     * 品系ID
     */
    @ApiModelProperty("品系ID")
    private Integer productSeriesId;

    @ApiModelProperty(value = "结构指标ID")
    private Long indicatorId;

    /**
     * 项目类型:1标注2评审3标准训练集
     */
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("项目类型:1标注2评审3标准训练集")
    @Length(max = 255, message = "{projectType.length}")
    private String projectType;
    @ApiModelProperty(value = "机构编号")
    private Long organizationId;
    @ApiModelProperty(value = "项目描述")
    @Size(min = 0, max = 200, message = "{UpdateProjectVO.projectName.length}")
    private String description;
    @ApiModelProperty("专题ID")
    private Integer topicId;


    /**
     * 以下属性暂未使用
     */
    @ApiModelProperty(required = false, hidden = true, value = "状态:1待启动，2进行中，3暂停，4已完成")
    private Integer status;
    @ApiModelProperty(required = false, hidden = true, value = "切片列表(可以不传参数)")
    private Long[] imageIdList;

    /**
     * 眼科新添加字段
     */
    @ApiModelProperty(value = "病理组织id")
    private Long tissueId;

    @ApiModelProperty(value = "算法模型id")
    private Long modelId;

}
