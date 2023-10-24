package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目表
 *
 * @TableName tb_project
 */
@TableName(value = "tb_project")
@Data
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    @TableId(value = "project_id", type = IdType.AUTO)
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 结构指标ID
     */
    private Long indicatorId;

    /**
     * 系统类型code
     */
    private Long systemCode;

    /**
     * 脏器类型code
     */
    private Long viscusCode;

    /**
     * 种属ID
     */
    private Integer speciesId;

    /**
     * 品系ID
     */
    private Integer productSeriesId;

    /**
     * 染色类型（1RGB，2HEX）
     */
    private Integer colorType;

    /**
     * 关联结构指标
     */
    private Long tagId;

    /**
     * 切片数
     */
    private Integer slideTotal;

    /**
     * 状态:1待启动，2进行中，3暂停，4已完成
     */
    private Integer status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private Boolean delFlag;

    /**
     * 项目类型:1标注2评审3标准训练集
     */
    private String projectType;

    /**
     * 机构ID
     */
    private Long organizationId;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 关联图像总数
     */
    private Long imageTotal;
    @ApiModelProperty("是否已生成考题0-未生成；1-已生成")
    private String ifCreateQuestions;


}