package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author gjt.
 * @dataData3/6/1 14:21
 */
@Data
public class SpecialRoleUserSelectVo {

    @ApiModelProperty(value = "页码")
    public int pageNum = 1;
    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;
    @NotNull(message = "{SpecialRoleUserSelectVo.specialId.isnull}")
    @ApiModelProperty(value = "专题id")
    private Long specialId;
    @ApiModelProperty(value = "用户名称")
    private String userName;
    @ApiModelProperty(value = "姓名")
    private String nickName;
    @ApiModelProperty(value = "手机号码")
    private String phonenumber;
    @ApiModelProperty(value = "专题角色id")
    private Long specialRoleId;
    @ApiModelProperty(value = "系统角色id")
    private Long roleId;
    @ApiModelProperty(value = "状态")
    private Long status;
    @ApiModelProperty(value = "创建时间")
    private Map<String, Object> createTimeParams;

}
