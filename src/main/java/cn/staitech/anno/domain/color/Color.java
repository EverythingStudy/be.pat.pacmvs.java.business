package cn.staitech.anno.domain.color;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wangfeng
 * @create: 2023-09-14 10:48:26
 * @Description: 颜色
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("tb_color")
public class Color {
    /**
     * 颜色
     */
    @TableId(value = "color_id", type = IdType.NONE)
    @ApiModelProperty(value = "颜色ID", hidden = true)
    private String colorId;

    /**
     * 颜色RGB
     */
    @ApiModelProperty(value = "颜色RGB", required = true)
    private String rgb;

    /**
     * 颜色HEX
     */
    @ApiModelProperty(value = "颜色HEX", required = true)
    private String hex;
}