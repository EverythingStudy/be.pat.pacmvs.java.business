package cn.staitech.anno.project.vo;

import cn.staitech.anno.project.domain.Project;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/13 17:44:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectIN {
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("项目名称")
    @Length(max= 50,message="编码长度不能超过50")
    private String projectName;

    @ApiModelProperty("种属ID")
    private Integer speciesId;

    @ApiModelProperty("品系ID")
    private Integer productSeriesId;

    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("项目类型:1标注2评审3标准训练集")
    @Length(max= 255,message="编码长度不能超过255")
    private String projectType;

    @ApiModelProperty("染色类型（1RGB，2HEX）")
    private Integer colorType;

    @ApiModelProperty("状态:1待启动，2进行中，3暂停，4已完成")
    private Integer status;

    @ApiModelProperty("创建者")
    private Long createBy;

    @ApiModelProperty("当前用户")
    private Long userId;

    @ApiModelProperty(value = "创建时间-查询入参")
    private TimeRangeIN createTimeParams;

    private Integer pageNum;

    private Integer pageSize;


}
