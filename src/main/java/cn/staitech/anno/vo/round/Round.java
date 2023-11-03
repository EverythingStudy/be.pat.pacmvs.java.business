package cn.staitech.anno.vo.round;

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
 * @create: 2023-09-10 13:04:08
 * @Description: 轮次
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("tb_round")
public class Round {
    /**
     * 组别id
     */
    @TableId(value = "round_id", type = IdType.AUTO)
    @ApiModelProperty(value = "轮次ID", hidden = true)
    private Long roundId;

    /**
     * 组别名称
     */
    @ApiModelProperty(value = "轮次名称", required = true)
    @NotNull(message = "{Round.roundName.isnull}")
    private String roundName;

    /**
     * 组别名称 en
     */
    @ApiModelProperty(value = "轮次名称en", required = true)
    @NotNull(message = "{Round.roundNameEn.isnull}")
    private String roundNameEn;
}
