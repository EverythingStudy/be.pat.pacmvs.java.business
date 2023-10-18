package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_algorithm_json")
public class AlgorithmJson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "algorithm_json_id", type = IdType.AUTO)
    private Long algorithmJsonId;

    /**
     * 考核算法主键
     */
    private Long algorithmAssessmentId;

    /**
     * 切片id
     */
    private Long slideId;

    /**
     * 标注json名称
     */
    private String algorithmJsonName;

    /**
     * 标注json路径
     */
    private String algorithmJsonUrl;

    /**
     * 选中状态（0：未选中，1：已选中）
     */
    private String selectedStatus;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标志（0：存在，1：删除）
     */
    private String delFlag;

    /**
     * 类型（0，考题json,1:算法json）
     */
    private String jsonType;


}
