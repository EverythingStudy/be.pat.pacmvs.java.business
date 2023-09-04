package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * tb_group
 * @author  zmj
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
    @TableId(value = "group_id",type = IdType.AUTO)
    @ApiModelProperty(hidden = true)
    private Long groupId;

    /**
     * 组别名称
     */
    @ApiModelProperty(value = "组别名称", required = true)
    @NotNull(message = "组别不可为空 !")
    private String groupName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别（0雌，1雄）", required = true)
    @NotNull(message = "性别不可为空 !")
    private Integer gender;

    /**
     * 组别描述
     */
    @ApiModelProperty(value = "描述",hidden = true)
    @Size(min = 0, max = 100, message = "描述不可超过100字段")
    private String description;

    /**
     * 更新者id
     */
    @ApiModelProperty(hidden = true)
    private Long updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建者id
     */
    @ApiModelProperty(hidden = true)
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 1删除，默认为0"
     */
    @ApiModelProperty(hidden = true)
    private Integer delFlag;

    /**
     * 专题id
     * */
    @ApiModelProperty(required = true)
    private Long specialId;

    /**
     * 移走原因
     * */
    @ApiModelProperty(required = true)
    private Integer reasons;

    /**
     * 剂量
     * */
    @ApiModelProperty(required = true)
    @Size(min = 0, max = 20, message = "剂量不可超过20字段")
    private String dosage;

    private static final long serialVersionUID = 1L;


}