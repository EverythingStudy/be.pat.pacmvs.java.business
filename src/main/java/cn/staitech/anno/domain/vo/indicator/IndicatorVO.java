package cn.staitech.anno.domain.vo.indicator;

import cn.staitech.anno.domain.Project;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class IndicatorVO {
    
    @ApiModelProperty(value = "病例指标id")
    private Integer indicatorId;
    
    @ApiModelProperty(value = "病例指标名称")
    private String indicatorName;
    
    @ApiModelProperty(value = "关联项目数量")
    private Integer projectNum;
    
    @ApiModelProperty(value = "标注类别数量")
    private Integer classesNum;
    
    @ApiModelProperty(value = "创建者id")
    private Long userId;
    
    @ApiModelProperty(value = "创建者名称")
    private String userName;
    
    @ApiModelProperty(value = "状态")
    private Integer isDeleted;
    
    @ApiModelProperty(value = "创建者")
    private String createBy;
    
    @ApiModelProperty(value = "关联的项目")
    private List<Project> projectVo;
    
    
}
