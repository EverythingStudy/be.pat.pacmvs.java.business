package cn.staitech.anno.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gjt.
 * @data 2023/5/25 16:03
 */
@Data
public class ImageVisited {

    @ApiModelProperty(value = "图像名称")
    private String imageName;

    @ApiModelProperty(value = "图像url")
    private String thumbUrl;

    @ApiModelProperty(value = "分组id")
    private Long groupId;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

}
