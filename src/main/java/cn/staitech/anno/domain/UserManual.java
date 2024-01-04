package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户手册
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user_manual")
@ApiModel(value="UserManual对象", description="用户手册")
public class UserManual implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "user_manual_id", type = IdType.AUTO)
    private Long userManualId;

    @ApiModelProperty(value = "用户手册地址")
    private String userManualUrl;

    @ApiModelProperty(value = "语种类型 0:中文；1:英文")
    private Integer messagesType;

    @ApiModelProperty(value = "组织机构ID")
    private Long organizationId;

    @ApiModelProperty(value = "默认为0，1为删除")
    private Integer delFlag;

    @ApiModelProperty(value = "创建者ID")
    private Long createBy;

    @ApiModelProperty(value = "更新者ID")
    private Long updateBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
