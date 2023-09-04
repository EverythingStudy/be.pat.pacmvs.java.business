package cn.staitech.anno.domain;

import cn.staitech.common.core.annotation.Excel;
import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 项目表 tb_project
 *
 * @author staitech
 */
@Data
public class Project extends BaseEntity {

    /**
     * 项目ID
     */
    @Excel(name = "项目ID", cellType = Excel.ColumnType.NUMERIC, prompt = "项目ID")
    @ApiModelProperty(hidden = true)
    private Long projectId;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 组织ID
     */
    @Excel(name = "组织ID")
    @ApiModelProperty(hidden = true)
    private Long tissueID;

    /**
     * 编辑模式
     */
    @ApiModelProperty(hidden = true)
    private String editMode;

    /**
     * 图象数
     */
    @Excel(name = "图像数")
    @ApiModelProperty(hidden = true)
    private Long imageTotal;

    /**
     * 标注类型
     */
    @Excel(name = "标注类型")
    @ApiModelProperty(hidden = true)
    private Long markType;

    /**
     * 审核状态
     */
    @Excel(name = "审核状态", readConverterExp = "0=未审核,1= 已审核")
    @ApiModelProperty(hidden = true)
    private Long examinationFlag;

    /**
     * 描述
     */
    @ApiModelProperty(hidden = true)
    private String examinationFlagName;

    /**
     * 已审核的标注数量
     */
    @ApiModelProperty(hidden = true)
    private Integer examinationNum;

    /**
     * 描述
     */
    @ApiModelProperty(hidden = true)
    private String description;


    /**
     * 病理指标ID
     */
    @ApiModelProperty(value = "病理指标id")
    private Long indicatorId;

    /**
     * 病理指标名称
     */
    @ApiModelProperty(value = "病理指标名称", hidden = true)
    private String indicatorName;


    /**
     * 管理者
     */
    @ApiModelProperty(value = "管理者id")
    private String managerId;

    /**
     * 管理者名称
     */
    @ApiModelProperty(value = "管理者名称")
    private String userName;

    /**
     * 人工标注数
     */
    @ApiModelProperty(hidden = true)
    private Integer annotationTotal;

    /**
     * 创建者id
     */
    @ApiModelProperty(value = "所属管理者ID")
    private Long createBy;

    /**
     * 创建者名称
     */
    @ApiModelProperty(hidden = true)
    private String createByName;

    /**
     * 更新者id
     */
    @ApiModelProperty(hidden = true)
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
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    @ApiModelProperty(hidden = true)
    private String searchValue;

    @ApiModelProperty(hidden = true)
    private String remark;

    @ApiModelProperty(value = "项目状态")
    private String projectStatus;

    @ApiModelProperty(value = "脏器组织id")
    private Long dictCode;
}
