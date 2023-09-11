package cn.staitech.anno.domain.image.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author wangfeng
 */
@Data
public class ImageUpdateVO {
    @ApiModelProperty(value = "切片编号-图像ID", required = true)
    private Long imageId;
    @Size(min = 1, max = 100, message = "切片编号长度不能超过100个字符")
    @ApiModelProperty(value = "文件名称-切片编号")
    private String imageName;
    @ApiModelProperty(value = "机构编号")
    private Long organizationId;
    @ApiModelProperty(value = "轮次ID-1到10")
    private Long roundId;
    @ApiModelProperty(value = "所属专题-专题名称", hidden = true)
    private String topicName;
}
