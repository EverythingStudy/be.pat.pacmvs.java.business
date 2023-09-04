package cn.staitech.anno.domain.project.in;

import cn.staitech.common.core.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * @Author wudi
 * @Date 2023/5/30 10:06
 * @desc 项目列表查询入参
 */
@Data
public class ProjectListQueryIn extends PageRequest {

    @ApiModelProperty(value = "专题id", notes = "")
    @NotNull(message = "专题不能为空")
    private Long specialId;

    @ApiModelProperty(value = "项目名称", notes = "")
    private String projectName;

    @ApiModelProperty(value = "病理系统id", notes = "")
    private Long systemCode;

    @ApiModelProperty(value = "脏器类型id", notes = "")
    private Long viscusCode;

    @ApiModelProperty(value = "创建者", notes = "")
    private String createName;

    @ApiModelProperty(value = "创建时间范围")
    private Map<String,Date> createTime;


}
