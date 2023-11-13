package cn.staitech.anno.vo.project;

import cn.staitech.common.core.annotation.Excel;
import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目列表
 *
 * @author wangf
 * <p>
 * 返回值：
 * 项目ID：     "projectId": 13,
 * 项目名称：projectName
 * 种属ID:speciesId
 * 种属名称：speciesName
 * 品系ID： productSeriesId
 * 品系名称： productSeries
 * 染色类型ID	colorType
 * 染色类型名称	colorTypeName
 * 关联结构指标ID                "indicatorId": 2,
 * 关联结构指标名称 "indicatorName": "犬哈氏腺（右侧）",
 * 项目类型	projectType
 * 项目类型名称 	projectTypeName
 * 描述	description
 * 机构编号	                "organizationId": 1,
 * 机构名称                "organizationName": null
 * 状态ID	status
 * 状态编号 statusName
 * 创建者	userName
 * 创建时间 createTime
 */
@Data
public class ProjectListVO extends BaseEntity {

    /**
     * 项目ID
     */
    @Excel(name = "项目ID", cellType = Excel.ColumnType.NUMERIC, prompt = "项目ID")
    @ApiModelProperty(value = "", hidden = true)
    private Long projectId;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 图象数
     */
    @Excel(name = "图像数")
    @ApiModelProperty(value = "图象数")
    private Long imageTotal;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;


    /**
     * 病理指标ID
     */
    @ApiModelProperty(value = "结构指标ID")
    private Long indicatorId;

    /**
     * 病理指标名称
     */
    @ApiModelProperty(value = "结构指标名称")
    private String indicatorName;

    /**
     * 病理指标名称英文
     */
    @ApiModelProperty(value = "结构指标名称英文")
    private String indicatorNameEn;

    /**
     * 创建者id
     */
    @ApiModelProperty(value = "创建者id")
    private Long createBy;

    /**
     * 创建者名称
     */
    @ApiModelProperty(value = "创建者名称")
    private String userName;
    /**
     * 更新者id
     */
    @ApiModelProperty(value = "更新者id")
    private Long updateBy;

    /**
     * 创建时间 create_time
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间 update_time
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 用户名称列表
     */
    @ApiModelProperty(value = "用户名称列表")
    private List<Map<String, Object>> userMap;

    /**
     *
     */
    @ApiModelProperty(value = "用户名称列表", hidden = true)
    private String[] userNames;

    /**
     * 图像名称
     */
    @ApiModelProperty(hidden = true, value = "图片名称")
    private String imageName;

    /**
     * 切片id
     */
    @ApiModelProperty(hidden = true, value = "切片id")
    private Long slideId;

    /**
     * 项目状态
     */
    @ApiModelProperty(value = "项目状态")
    private Integer status;

    @ApiModelProperty(value = "项目状态名称")
    private String statusName;

    /**
     * 项目角色类型
     */
    @ApiModelProperty(value = "项目角色类型")
    private Integer roleType;


    /**
     * 种属ID
     */
    @ApiModelProperty("种属ID")
    private String speciesId;

    @ApiModelProperty("种属名称")
    private String speciesName;


    @ApiModelProperty("染色类型（1RGB，2HEX）")
    private Integer colorType;
    @ApiModelProperty("染色类型（1RGB，2HEX）")
    private String colorTypeName;

    /**
     * 品系ID
     */
    @ApiModelProperty("品系ID")
    private Integer productSeriesId;


    @ApiModelProperty("品系名称")
    private String productSeries;

    /**
     * 项目类型:1标注2评审3标准训练集
     */
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("项目类型:1标注2评审3标准训练集")
    @Length(max = 255, message = "{projectType.length}")
    private String projectType;

    @ApiModelProperty("项目类型名称")
    private String projectTypeName;

    @ApiModelProperty(value = "机构编号")
    private Long organizationId;
    @ApiModelProperty(value = "机构名称")
    private String organizationName;
}
