package cn.staitech.anno.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gjt.
 * @data 2023/5/25 13:44
 */
@Data
public class RecentlyVisited {

    @ApiModelProperty(value = "主键id")
    private Long recentlyVisitedId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "图像名称")
    private String imageName;

    @ApiModelProperty(value = "图像url")
    private String thumbUrl;

    @ApiModelProperty(value = "评审轮次id")
    private Long reviewRoundId;

    @ApiModelProperty(value = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;


}
