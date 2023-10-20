package cn.staitech.anno.project.vo;

import cn.staitech.anno.project.domain.Project;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ProjectVO extends Project {
    private String indicatorName;
    private String indicatorNameEn;
    @ApiModelProperty("种属")
    private String speciesVal;

    @ApiModelProperty("种属-en")
    private String speciesValEn;
    @ApiModelProperty("机构")
    private String organizationName;
    @ApiModelProperty("创建者")
    private String userName;
    @ApiModelProperty("品系")
    private String psName;
    @ApiModelProperty("品系-en")
    private String psNameEn;

    @TableField(exist = false)
    @ApiModelProperty("项目类型名称")
    private String projectTypeName;
}
