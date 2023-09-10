package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GroupVO {

    /**
     * 组别id
     */
    @NotNull(message = "组别id不可为空 !")
    @ApiModelProperty(value = "组别id", required = true)
    private Long groupId;

    /**
     * 组别名称
     */
    @ApiModelProperty(value = "组别名称", required = true)
    private String groupName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别（0雌，1雄）", required = true)
    private Integer gender;

    /**
     * 组别描述
     */
    @ApiModelProperty(value = "", hidden = true)
    @Size(min = 0, max = 100, message = "描述不可超过100字段")
    private String description;

    /**
     * 更新者id
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long updateBy;

    /**
     * 1删除，默认为0"
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer delFlag;

    /**
     *专题id
     */
    @ApiModelProperty(value = "专题id", required = true)
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


}
