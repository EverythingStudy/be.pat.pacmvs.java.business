package cn.staitech.anno.domain.productseries;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author: wangfeng
 * @create: 2023-09-14 10:36:18
 * @Description: 品系
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("tb_product_series")
public class ProductSeries {
    /**
     * 品系ID
     */
    @TableId(value = "product_series_id", type = IdType.AUTO)
    @ApiModelProperty(value = "品系ID", hidden = true)
    private Integer productSeriesId;

    /**
     * 品系名称
     */
    @ApiModelProperty(value = "品系名称", required = true)
    @NotNull(message = "品系名称不可为空 !")
    private String name;

    /**
     * 品系名称en
     */
    @ApiModelProperty(value = "品系名称en", required = true)
    @NotNull(message = "品系名称不可为空 !")
    private String nameEn;
}
