package cn.staitech.anno.domain.image.in;

import cn.staitech.common.core.domain.PageResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author lif
 */
@Data
public class ImageListFindIn extends PageResponse {

    @ApiModelProperty("专题号")
    private String topicName;

    @ApiModelProperty("切片编号")
    private String imageName;

    @ApiModelProperty(value = "机构编号")
    private Long organizationId;

    @ApiModelProperty("上传时间")
    private Map<String,String> createTime;

    @ApiModelProperty("0上传中、1上传失败、2解析中、3解析失败、4可用、5不可用")
    private Integer status;

}