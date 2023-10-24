package cn.staitech.anno.domain.projectgroup.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/5/30 16:54
 * @desc 项目分组列表
 */
@Data
public class ProjectGroupListOut {
    @ApiModelProperty(value = "项目分组ID")
    private Long projectGroupId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "分组id")
    private Long groupId;

    @ApiModelProperty(value = "组别")
    private String groupName;

    @ApiModelProperty(value = "性别", notes = "0-雌；1-雄")
    private Integer gender;

    @ApiModelProperty(value = "组别描述")
    private String description;

    @ApiModelProperty(value = "移走原因1-给药结束安乐死；2-恢复期结束安乐死")
    private int reasons;

    @ApiModelProperty(value = "剂量")
    private String dosage;

    @ApiModelProperty(value = "切片数")
    private Integer slideTotal;

}
