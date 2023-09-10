package cn.staitech.anno.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class GroupListVO {
    /**
     * 组别id
     */
    @ApiModelProperty(value = "组别id")
    private Long groupId;

    /**
     * 组别名称
     */
    @ApiModelProperty(value = "组别名称")
    private String groupName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别（0雌，1雄）")
    private Integer gender;

    /**
     * 组别描述
     */
    @ApiModelProperty(value = "描述")
    @Size(min = 0, max = 100, message = "描述不可超过100字段")
    private String description;

    /**
     * 更新者id
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建者id
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 1删除，默认为0"
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer delFlag;

    /**
     * 性别名称
     */
    @ApiModelProperty(value = "性别名称")
    private String genderName;

    /**
     * 关联项目数
     * */
    @ApiModelProperty(value = "关联项目数")
    private int total;

    /**
     * 专题id
     * */
    @ApiModelProperty(value = "专题id")
    private Long specialId;

    /**
     * 移走原因标识
     * */
    @ApiModelProperty(value = "移走原因标识")
    private Integer reasons;

    /**
     * 移走原因
     * */
    @ApiModelProperty(value = "移走原因")
    private String cause;

    /**
     * 剂量
     * */
    @ApiModelProperty(value = "剂量")
    private String dosage;



}
