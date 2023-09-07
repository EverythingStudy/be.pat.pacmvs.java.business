package cn.staitech.system.domain.vo.organization;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gjt.
 * @data 2023/5/30 17:12
 */
@Data
public class OrganizationUpdateStatusVo {
    @NotNull(message = "机构id不可为空!")
    @ApiModelProperty(value = "机构id", required = true)
    private Long organizationId;

    @ApiModelProperty(value = "状态", required = true)
    private Long status;

}
