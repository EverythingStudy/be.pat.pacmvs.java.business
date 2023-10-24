package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@Data
public class PorjectVO {

    /**
     * 项目ID
     */
    @NotNull(message = "{PorjectVO.projectId.isnull}")
    @ApiModelProperty(required = true, value = "项目id")
    private Long projectId;

    /**
     * 病理指标ID
     */
    @ApiModelProperty(value = "病理指标id")
    private Long indicatorId;

    /**
     * 病理指标名称
     */
    @ApiModelProperty(value = "", hidden = true)
    private String indicatorName;

    /**
     * 创建时间 create_time
     */
    @ApiModelProperty(value = "", hidden = true)
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long updateBy;

    /**
     * 管理者
     */
    @ApiModelProperty(value = "", hidden = true)
    private String managerId;

    /**
     * 项目名称
     */
    @NotNull(message = "{PorjectVO.projectName.isnull}")
    @Size(min = 0, max = 50, message = "{InsertProjectVO.projectName.length}")
    @ApiModelProperty(required = true, value = "项目名称")
    private String projectName;

    /**
     * 脏器组织id
     */
    @ApiModelProperty(value = "脏器组织id")
    private Long dictCode;

}
