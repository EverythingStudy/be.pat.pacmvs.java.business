package cn.staitech.anno.domain.geojson;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class JsonExport {

    @ApiModelProperty(value = "专题名称")
    private String  topicName;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "指标id")
    private Long indicatorId;

    @ApiModelProperty(value = "宽，高")
    private String imageShape;

    @ApiModelProperty(value = "图像名称")
    private String imageName;

    @ApiModelProperty(value = "图像后缀")
    private String format;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
