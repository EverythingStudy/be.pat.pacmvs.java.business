package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectImageVO {
    
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;
    
    
    /**
     * 图像批量id
     */
    @ApiModelProperty(value = "图像批量id", required = true)
    private Long[] imageIdList;
    
    /**
     * 图像id
     */
    @ApiModelProperty(hidden = true, value = "图像id")
    private Long imageId;
    
    /**
     * 创建者id
     */
    @ApiModelProperty(hidden = true, value = "创建者id")
    private Integer createBy;
}
