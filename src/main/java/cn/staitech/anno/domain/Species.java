package cn.staitech.anno.domain;

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
    @TableId(value = "species_id", type = IdType.AUTO)
    @ApiModelProperty(value = "种属ID", hidden = true)
    private String speciesId;

    /**
     * 种属名称
     */
    @ApiModelProperty(value = "种属名称", required = true)
    private String name;

    /**
     * 种属名称EN
     */
    @ApiModelProperty(value = "种属名称En", required = true)
    private String nameEn;
}
