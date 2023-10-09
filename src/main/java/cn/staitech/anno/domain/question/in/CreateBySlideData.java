package cn.staitech.anno.domain.question.in;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author wudi
 * @Date 2023/9/26 11:04
 * @desc
 */
@Data
public class CreateBySlideData {

    @ApiModelProperty(value = "切片ID")
    private Long slideId;


    @ApiModelProperty(value = "项目ID")
    private Long projectId;


    @ApiModelProperty(value = "图像ID")
    @NotNull(message = "图像id入参不能为空！")
    private Long imageId;
}
