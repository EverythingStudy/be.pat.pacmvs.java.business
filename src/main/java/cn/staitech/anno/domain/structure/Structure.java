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
     * 结构ID
     */
    @TableId(value = "structure_id", type = IdType.NONE)
    @ApiModelProperty(value = "结构ID", hidden = true)
    private String structureId;

    /**
     * 结构名称
     */
    @ApiModelProperty(value = "结构名称", required = true)
    private String name;
    
    /**
     * 种属ID
     */
    @ApiModelProperty(value = "种属ID", required = true)
    private String speciesId;
    
    
    /**
     * 脏器ID
     */
    @ApiModelProperty(value = "脏器ID", required = true)
    private String organId;
}
