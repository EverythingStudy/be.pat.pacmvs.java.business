package cn.staitech.anno.domain.vo;

import cn.staitech.common.core.annotation.Excel;
import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ProjectListVO extends BaseEntity {
    
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
    @ApiModelProperty(value = "组织ID")
    private Long tissueID;
    
    /**
     * 编辑模式
     */
    @ApiModelProperty(value = "编辑模式")
    private String editMode;
    
    /**
     * 图象数
     */
    @Excel(name = "图像数")
    @ApiModelProperty(value = "图象数")
    private Long imageTotal;
    
    /**
     * 标注类型
     */
    @Excel(name = "标注类型")
    @ApiModelProperty(value = "标注类型")
    private Long markType;
    
    /**
     * 审核状态
     */
    @Excel(name = "审核状态", readConverterExp = "0=未审核,1= 已审核")
    @ApiModelProperty(value = "审核状态")
    private Long examinationFlag;
    
    /**
     * 状态名称
     */
    @ApiModelProperty(value = "状态名称")
    private String examinationFlagName;
    
    /**
     * 已审核的标注数量
     */
    @ApiModelProperty(value = "已审核的标注数量")
    private Integer examinationNum;
    
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    
    
    /**
     * 病理指标ID
     */
    @ApiModelProperty(value = "病理指标id")
    private Long indicatorId;
    
    /**
     * 病理指标名称
     */
    @ApiModelProperty(value = "病理指标名称")
    private String indicatorName;
    
    
    /**
     * 管理者
     */
    @ApiModelProperty(value = "管理者")
    private String managerId;
    
    /**
     * 管理者名称
     */
    @ApiModelProperty(value = "管理者名称")
    private String userName;
    
    /**
     * 人工标注数
     */
    @ApiModelProperty(value = "人工标注数")
    private Integer annotationTotal;
    
    /**
     * 创建者id
     */
    @ApiModelProperty(value = "创建者id")
    private Long createBy;
    
    /**
     * 创建者名称
     */
    @ApiModelProperty(value = "创建者名称")
    private String createByName;
    
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
    private List<Map<String,Object>> userMap;

    /**
     *
     */
//    @ApiModelProperty(value = "用户名称列表")
    private String[] userNames;

    /**
     * 图像名称
     * */
    @ApiModelProperty(hidden = true, value = "图片名称")
    private String imageName;

    /**
     * 切片id
     * */
    @ApiModelProperty(hidden = true, value = "切片id")
    private Long slideId;

    /**
     * 项目状态
     * */
    @ApiModelProperty(value = "项目状态")
    private String projectStatus;

    /**
     * 脏器组织名称
     * */
    @ApiModelProperty(value = "脏器组织名称")
    private String dictLabel;

    /**
     * 脏器组织id
     * */
    @ApiModelProperty(value = "脏器组织id")
    private Long dictCode;

    /**
     * 项目角色类型
     */
    @ApiModelProperty(value = "项目角色类型")
    private Integer roleType;
}
