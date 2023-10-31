package cn.staitech.anno.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 新建项目
 * 项目名称 projectName
 * 种属	speciesId
 * 染色类型	colorType	(染色类型（1RGB，2HEX）)
 * 关联结构指标 indicatorId
 * 品系	product_series_id
 * 项目类型 	projectType	(项目类型:1标注2评审3标准训练集)
 * 机构 organizationId
 * 描述 description
 *
 * @author wangf
 */
@Data
public class UpdateProjectStatusVO implements Serializable {
    @ApiModelProperty(required = true, value = "项目ID")
    private Long projectId;
    /**
     * 以下属性暂未使用
     */
    @ApiModelProperty(required = true, hidden = true, value = "状态:1待启动，2进行中，3暂停，4已完成")
    private Integer status;

}
