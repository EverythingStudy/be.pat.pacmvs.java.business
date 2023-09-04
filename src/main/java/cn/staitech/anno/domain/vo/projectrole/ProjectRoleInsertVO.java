package cn.staitech.anno.domain.vo.projectrole;

import cn.staitech.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProjectRoleInsertVO {

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    @Excel(name = "角色序号", cellType = Excel.ColumnType.NUMERIC)
    private Long roleId;

    /**
     * 项目编号
     */
    @ApiModelProperty(value = "项目编号")
    @Excel(name = "项目编号")
    private Long projectId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    @Excel(name = "角色名称")
    private String roleName;

    /**
     * 角色类型
     */
    @ApiModelProperty(value = "角色类型：1、项目代表；2、项目管理者；3、项目贡献者")
    @Excel(name = "角色类型：1、项目代表；2、项目管理者；3、项目贡献者")
    private Integer roleType;

    /**
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty(value = "角色状态（0正常 1停用）")
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;
}
