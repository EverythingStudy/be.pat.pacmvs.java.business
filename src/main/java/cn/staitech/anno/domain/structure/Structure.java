package cn.staitech.anno.domain.structure;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wangfeng
 * @create: 2023-09-13 16:37:44
 * @Description: 结构
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("tb_structure")
public class Structure {
    /**
     * 结构key
     */
    @TableId(value = "key", type = IdType.INPUT)
    @ApiModelProperty(value = "结构key", hidden = true)
    private String key;

    /**
     * 结构value
     */
    @ApiModelProperty(value = "结构value", required = true)
    private String value;
}
