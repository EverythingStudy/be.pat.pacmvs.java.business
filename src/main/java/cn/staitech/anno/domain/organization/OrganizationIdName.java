package cn.staitech.anno.domain.organization;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangfeng
 */
@Data
public class OrganizationIdName implements Serializable {

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id")
    private Long organizationId;


    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String organizationName;
}
