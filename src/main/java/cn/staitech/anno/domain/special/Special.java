package cn.staitech.anno.domain.special;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author gjt.
 * &#064;data  2023/5/29 8:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_special")
public class Special implements Serializable {

    /**
     * 主键id .
     */
    @TableId(value = "special_id", type = IdType.AUTO)
    private Long specialId;

    /**
     * 专题编号 .
     */
    @TableField(value = "special_number")
    private String specialNumber;

    /**
     * 专题名称 .
     */
    @TableField(value = "special_name")
    private String specialName;

    /**
     * 试验类型 .
     */
    @ApiModelProperty(value = "试验类型")
    private String trialType;

    /**
     * 种属
     */
    @TableField(value = "species")
    private String species;

    /**
     * 染色类型 .
     */
    @TableField(value = "stain_type")
    private String stainType;

    /**
     * 病理指标id
     */
    @TableField(value = "indicator_id")
    private Long indicatorId;

    /**
     * 同行评议(0:需要,1:不需要) .
     */
    @TableField(value = "peer_review")
    private Long peerReview;

    /**
     * 状态(0:待启动,1:进行中,2:暂停,3:锁定,4:已完成)
     */
    @TableField(value = "status")
    private Long status;

    /**
     * 交付状态 0：未交付 1：已交付 .
     */
    @TableField(value = "delivery_status")
    private Long deliveryStatus;

    /**
     * 创建者 .
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 创建时间 .
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**
     * 更新者 .
     */
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 更新时间 .
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    /**
     * 删除标志(0:正常,1:回收站,2:删除) .
     */
    @TableField(value = "del_flag")
    private Long delFlag;

    /**
     * 试验类型 .
     */
    @TableField(value = "trial_type")
    private Long trial_type;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Map<String, Object> createTimeParams;

    @TableField(exist = false)
    @ApiModelProperty(value = "创建者名称")
    private String userName;

    @TableField(exist = false)
    @ApiModelProperty(value = "专题角色id")
    private Long specialUserId;

    @TableField(exist = false)
    @ApiModelProperty(value = "专题角色用户状态")
    private Long sruStatus;
}
