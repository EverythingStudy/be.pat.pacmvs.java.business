package cn.staitech.anno.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目成员表
 * tb_project_member
 *
 * @author 王峰
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

    private String roleName;

    private String userName;
    @ApiModelProperty(value = "姓名")
    private String nickName;
    @ApiModelProperty(value = "用户性别（0男 1女）")
    private String sex;
    @ApiModelProperty(value = "手机号码")
    private String phonenumber;
    private Long createBy;
    private Long updateBy;
    @ApiModelProperty(name = "createTime", value = "创建时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(name = "createTime", value = "修改时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "机构ID")
    private Long organizationId;
}