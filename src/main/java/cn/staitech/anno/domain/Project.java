package cn.staitech.anno.domain;

import cn.staitech.common.core.annotation.Excel;
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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;

/**
 * 项目表 tb_project
 *
 * @author staitech
 */
@Data
@TableName(value = "tb_project")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BaseEntity {

    /**
     * 项目ID
     */
    @Excel(name = "项目ID", cellType = Excel.ColumnType.NUMERIC, prompt = "项目ID")
    @ApiModelProperty(value = "", hidden = true)
    @TableId(value = "project_id", type = IdType.AUTO)
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
    @ApiModelProperty(value = "", hidden = true)
    private Long imageTotal;

    /**
     * 描述
     */
    @ApiModelProperty(value = "", hidden = true)
    private String description;


    /**
     * 病理指标ID
     */
    @ApiModelProperty(value = "病理指标id")
    private Long indicatorId;

    /**
     * 病理指标名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "病理指标名称", hidden = true)
    private String indicatorName;

    /**
     * 创建者id
     */
    @ApiModelProperty(value = "所属管理者ID")
    private Long createBy;

    /**
     * 创建者名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "", hidden = true)
    private String createByName;

    /**
     * 更新者id
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long updateBy;

    /**
     * 创建时间 create_time
     */
    @ApiModelProperty(hidden = true, value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间 update_time
     */
    @ApiModelProperty(value = "", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    @ApiModelProperty(value = "", hidden = true)
    @TableField(exist = false)
    private String searchValue;

    @TableField(exist = false)
    @ApiModelProperty(value = "", hidden = true)
    private String remark;

    @ApiModelProperty(value = "项目状态")
    private Integer status;

    /**
     * 种属ID
     */
    @ApiModelProperty("种属ID")
    private Integer speciesId;

    @ApiModelProperty("染色类型（1RGB，2HEX）")
    private Integer colorType;

    /**
     * 品系ID
     */
    @ApiModelProperty("品系ID")
    private Integer productSeriesId;

    /**
     * 项目类型:1标注2评审3标准训练集
     */
    @Size(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty("项目类型:1标注2评审3标准训练集")
    @Length(max = 255, message = "编码长度不能超过255")
    // @TableField(exist = false)
    private String projectType;

    @ApiModelProperty(value = "机构编号")
    @TableField(value = "organization_id", exist = false)
    private Long organizationId;

    @ApiModelProperty(value = "机构名称")
    @TableField(exist = false)
    private String organizationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "种属名称")
    private String speciesName;

    @TableField(exist = false)
    @ApiModelProperty(value = "品系名称")
    private String productSeries;

    @TableField(exist = false)
    @ApiModelProperty(value = "创建时间-查询入参")
    private Map<String, Object> createTimeParams;

    @TableField(exist = false)
    @ApiModelProperty(value = "品系名称")
    private Map<String, Object> params;
}
