package cn.staitech.anno.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 项目成员表
 * tb_project_member
 *
 * @author  王峰
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectMember implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long projectId;

    private Long roleId;

    private Integer roleType;

    private String roleName;

    private String userName;

    private Long createBy;
    private Long updateBy;
}