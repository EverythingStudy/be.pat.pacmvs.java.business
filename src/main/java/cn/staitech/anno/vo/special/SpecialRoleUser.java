package cn.staitech.anno.vo.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gjt.
 * @data 2023/6/1 10:14
 */
@Data
public class SpecialRoleUser {

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "专题id")
    private Long specialId;

    @ApiModelProperty(value = "状态")
    private Long status;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;


}
