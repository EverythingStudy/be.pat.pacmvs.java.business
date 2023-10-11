package cn.staitech.anno.domain.question.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/9/26 15:07
 * @desc
 */
@Data
public class ConfirmSelectionIn {


    @ApiModelProperty(value = "考题id")
    private Long questionId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "切片编号")
    private String imageCode;

    @ApiModelProperty(value = "json文件名称")
    private String jsonName;

    @ApiModelProperty(value = "切片名称")
    private String imageName;
}
