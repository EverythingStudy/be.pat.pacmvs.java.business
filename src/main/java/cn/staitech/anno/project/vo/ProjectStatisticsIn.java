package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectStatisticsIn {

    @ApiModelProperty("项目ID")
    private Long projectId;

    @ApiModelProperty("标签集合")
    private List<Long> categoryList;

    @ApiModelProperty("用户集合")
    private List<Long> userList;

}
