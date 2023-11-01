package cn.staitech.anno.vo.projectmember;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询项目成员
 * 查询条件：用户名、姓名、性别、系统角色
 *
 * @author wangfeng
 * @date 2023/09/17 15:20
 */
@Data
public class ProjectMemberSelectVO extends Pager implements Serializable {
    @NotNull(message = "{PorjectVO.projectId.isnull}")
    @ApiModelProperty(value = "项目ID", required = true)
    private Long projectId;
    @ApiModelProperty(value = "用户名", required = false)
    private String userName;
    @ApiModelProperty(value = "姓名", required = false)
    private String nickName;
    @ApiModelProperty(value = "用户性别（0男 1女）", required = false)
    private String sex;
    @ApiModelProperty(value = "系统角色ID", required = false)
    private Long roleId;
}
