package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author gjt
 * @since 2023-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_algorithm_project_type")
public class AlgorithmProjectType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 算法项目类型主键
     */
    @TableId(value = "algorithm_project_type_id", type = IdType.AUTO)
    private Long algorithmProjectTypeId;

    /**
     * 算法项目类型名称
     */
    private String algorithmProjectTypeName;

    /**
     * 算法项目类型名称英文
     */
    private String algorithmProjectTypeNameEn;


}
