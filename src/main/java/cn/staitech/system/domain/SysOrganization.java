package cn.staitech.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author gjt
 */
@Data
public class SysOrganization {

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id")
    private Long organizationId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构id")
    private String organizationName;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactName;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String phoneNumber;

    /**
     * 帐号状态(0正常开启 1禁用)
     */
    @ApiModelProperty(value = "帐号状态(0正常开启 1禁用)")
    private Long status;

    /**
     * 删除标志(0代表存在 2代表删除)
     */
    @ApiModelProperty(value = "删除标志(0代表存在 2代表删除)")
    private Long delFlag;

    @ApiModelProperty(value = "创建时间")
    private Map<String, Object> createTimeParams;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty("创建者")
    private Long createBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("更新者")
    private Long updateBy;



}
