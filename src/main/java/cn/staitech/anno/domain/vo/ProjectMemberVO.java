package cn.staitech.anno.domain.vo;

import cn.staitech.anno.domain.ProjectMember;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目成员详细信息
 */
@Data
public class ProjectMemberVO extends ProjectMember {


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;

}
