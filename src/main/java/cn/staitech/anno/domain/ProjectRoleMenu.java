package cn.staitech.anno.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * sys_project_role_menu
 * @author 
 */
@Data
public class ProjectRoleMenu implements Serializable {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
    
    /**
     * 状态
     */
    private Long status;

    private static final long serialVersionUID = 1L;
    
    /**
     * 创建人id .
     */
    private Long createBy;
    
    /**
     * 创建时间 .
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    
    /**
     * 更新者 .
     */
    private Long updateBy;
    
    /**
     * 更新时间 .
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
}