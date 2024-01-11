package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: ProjectUserAnnoStatisticsVO
 * @Description:
 * @date 2023年12月26日
 */
@Data
public class ProjectUserAnnoStatisticsVO {

    @ApiModelProperty("项目ID")
    private Long projectId;

    @ApiModelProperty("用户")
    private Long createBy;

    @ApiModelProperty("图像数量")
    private Integer imageCount;

    @ApiModelProperty("标注总数")
    private Integer annoCount;


}
