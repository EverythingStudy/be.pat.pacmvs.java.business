package cn.staitech.anno.domain.geojson;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JsonExport {

    @ApiModelProperty(value = "专题名称")
    private String topicName;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "指标id")
    private Long indicatorId;

    @ApiModelProperty(value = "宽，高")
    private String imageShape;

    @ApiModelProperty(value = "图像名称")
    private String imageName;

    @ApiModelProperty(value = "图像存储位置")
    private String imageUrl;

    @ApiModelProperty(value = "图像后缀")
    private String format;

    @ApiModelProperty(value = "种属编码")
    private String speciesId;

    @ApiModelProperty(value = "脏器编码")
    private String organId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

}
