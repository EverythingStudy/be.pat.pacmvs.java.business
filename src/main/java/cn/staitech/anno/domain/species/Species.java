package cn.staitech.anno.domain.species;

/**
 * @author: wangfeng
 * @create: 2023-09-13 16:34:24
 * @Description: 种属
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("tb_species")
public class Species {
    /**
     * 种属key
     */
    @TableId(value = "key", type = IdType.INPUT)
    @ApiModelProperty(value = "种属key", hidden = true)
    private String key;

    /**
     * 种属value
     */
    @ApiModelProperty(value = "种属value", required = true)
    private String value;
}
