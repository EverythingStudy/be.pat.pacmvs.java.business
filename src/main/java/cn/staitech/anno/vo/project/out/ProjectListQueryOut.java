package cn.staitech.anno.vo.project.out;

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
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "系统类型code")
    private Long systemCode;

    @ApiModelProperty(value = "系统类型name")
    private String systemName;

    @ApiModelProperty(value = "脏器类型code")
    private Long viscusCode;

    @ApiModelProperty(value = "脏器类型name")
    private String viscusName;

    @ApiModelProperty(value = "切片数")
    private Integer slideTotal;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "创建者id")
    private Long createBy;

    @ApiModelProperty(value = "创建者名称")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
