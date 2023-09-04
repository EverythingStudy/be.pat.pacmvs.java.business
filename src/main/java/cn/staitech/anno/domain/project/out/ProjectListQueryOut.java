package cn.staitech.anno.domain.project.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author wudi
 * @Date 2023/5/30 10:23
 * @desc 项目列表查询响应
 */
@Data
public class ProjectListQueryOut {
    @ApiModelProperty(value = "项目ID", notes = "")
    private Long projectId;

    @ApiModelProperty(value = "项目名称", notes = "")
    private String projectName;

    @ApiModelProperty(value = "系统类型code", notes = "")
    private Long systemCode;

    @ApiModelProperty(value = "系统类型name", notes = "")
    private String systemName;

    @ApiModelProperty(value = "脏器类型code", notes = "")
    private Long viscusCode;

    @ApiModelProperty(value = "脏器类型name", notes = "")
    private String viscusName;

    @ApiModelProperty(value = "切片数", notes = "")
    private Integer slideTotal;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）", notes = "")
    private String delFlag;

    @ApiModelProperty(value = "创建者id", notes = "")
    private Long createBy;

    @ApiModelProperty(value = "创建者名称", notes = "")
    private String createName;

    @ApiModelProperty(value = "创建时间", notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
