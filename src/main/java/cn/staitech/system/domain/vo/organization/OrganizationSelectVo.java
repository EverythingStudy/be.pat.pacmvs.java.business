package cn.staitech.system.domain.vo.organization;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author gjt.
 * @data 2023/5/23 15:14
 */
@Data
public class OrganizationSelectVo  {

    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系方式")
    private String phoneNumber;

    @ApiModelProperty(value = "状态")
    private Long status;

    @ApiModelProperty(value = "授权时间")
    private Map<String, Object> authorizationTimeParams;

    @ApiModelProperty(value = "到期时间")
    private Map<String, Object> expirationTimeParams;

    @ApiModelProperty(value = "创建时间")
    private Map<String, Object> createTimeParams;

    @ApiModelProperty(value = "当前页数")
    private int pageNum;

    @ApiModelProperty(value = "每页数据条数")
    private int pageSize;




}
