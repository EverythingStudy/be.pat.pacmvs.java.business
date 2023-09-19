package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * tb_group
 *
 * @author wangfeng
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableName("tb_group")
public class Group implements Serializable {
    /**
     * 组别id
     */
    @TableId(value = "group_id", type = IdType.AUTO)
    @ApiModelProperty(value = "分组ID", hidden = true)
    private Long groupId;

    /**
     * 组别名称
     */
    @ApiModelProperty(value = "组别名称", required = true)
    private String groupName;

}