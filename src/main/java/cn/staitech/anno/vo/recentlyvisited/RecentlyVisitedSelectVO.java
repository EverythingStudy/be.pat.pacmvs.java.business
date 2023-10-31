package cn.staitech.anno.vo.recentlyvisited;

import cn.staitech.anno.domain.ImageVisited;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author gjt.
 * @data 2023/5/25 15:20
 */
@Data
public class RecentlyVisitedSelectVO {

    @ApiModelProperty(value = "主键id")
    private Long recentlyVisitedId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目类型(1标注,2评审,3标准训练集)")
    private String projectType;

    @ApiModelProperty(value = "评审轮次id")
    private Long reviewRoundId;

    @ApiModelProperty(value = "图像信息")
    private List<ImageVisited> imageVisited;

    @ApiModelProperty(value = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String visitTime;

}
