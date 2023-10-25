package cn.staitech.anno.domain.project;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author mugw
 * @version 1.0
 * @description 项目
 * @date 2023/6/14 14:59:11
 */
@Data
@TableName("tb_project")
public class ProjectPo {

    @ApiModelProperty(name = "项目ID")
    @TableId("project_id")
    private Long projectId;

    @ApiModelProperty(name = "项目名称")
    private String projectName;

    @ApiModelProperty(name = "专题id")
    private Long specialId;

    @ApiModelProperty(name = "系统类型code")
    private Long systemCode;

    @ApiModelProperty(name = "脏器类型code")
    private Long viscusCode;

    @ApiModelProperty(name = "切片数")
    private Integer slideTotal;

    @ApiModelProperty(name = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(name = "创建者id")
    private Long createBy;

    @ApiModelProperty(name = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "更新者")
    private Long updateBy;

    @ApiModelProperty(name = "更新时间")
    private Date updateTime;
}
