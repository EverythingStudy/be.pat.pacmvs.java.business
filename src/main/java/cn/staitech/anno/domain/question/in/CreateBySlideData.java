package cn.staitech.anno.domain.question.in;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/9/26 11:04
 * @desc
 */
@Data
public class CreateBySlideData {

    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    @TableField(value = "project_id")
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @TableField(value = "image_id")
    @ApiModelProperty(value = "图像ID")
    private Long imageId;
}
