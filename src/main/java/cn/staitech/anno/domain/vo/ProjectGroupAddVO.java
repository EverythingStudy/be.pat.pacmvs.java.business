package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProjectGroupAddVO {
    @ApiModelProperty(value = "项目分组id")
    private Long projectGroupId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "分组id")
    private Long groupId;

    @ApiModelProperty(value = "创建者id")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "切片数")
    private Integer slideTotal;

    private Integer delFlag;


}
