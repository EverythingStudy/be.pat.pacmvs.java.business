package cn.staitech.anno.domain.vo.marking.out;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MarkingSelectListVo {

    @ApiModelProperty(value = "标注id")
    private String marking_id;

    @ApiModelProperty(value = "标注id")
    private String area;

    @ApiModelProperty(value = "标注id")
    private String perimeter;

    @ApiModelProperty(value = "平均间距")
    private Double mean_distance;

    @ApiModelProperty(value = "最大间距")
    private Double max_distance;

    @ApiModelProperty(value = "最小间距")
    private Double min_distance;

    @ApiModelProperty(value = "内角")
    private String inner_angle;

    @ApiModelProperty(value = "外角")
    private String exterior_angle;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date create_time;

    @ApiModelProperty(value = "不同标签点的总数")
    private Long point_count;

    @ApiModelProperty(value = "名称")
    private String measure_full_name;


}
