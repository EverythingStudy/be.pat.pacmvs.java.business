package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/14 13:29:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideQueryIn {

    @ApiModelProperty("项目ID")
    private Integer projectId;

    @ApiModelProperty("图片（切片）编号")
    private String imageCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("标注人员")
    private Long annoUser;

    @ApiModelProperty("标注类别")
    private Long annoCategory;

    private Long userId;

    @ApiModelProperty(value = "创建时间-查询入参")
    private TimeRangeIn createTimeParams;

    @ApiModelProperty(value = "修改时间-查询入参")
    private TimeRangeIn updateTimeParams;

    private Integer pageNum;

    private Integer pageSize;
    
    @ApiModelProperty("项目ID")
    private Long[] projectIds;
    
    @ApiModelProperty("参与人员ID")
    private Long[] createBys;
}
