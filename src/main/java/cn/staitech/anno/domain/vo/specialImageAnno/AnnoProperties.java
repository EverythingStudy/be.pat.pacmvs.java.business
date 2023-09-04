package cn.staitech.anno.domain.vo.specialImageAnno;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnnoProperties {

    @ApiModelProperty(value = "作者(默认0,AI绘制为图片上传作者，其他为操作用户ID)")
    private String annotation_owner;

    @ApiModelProperty(value = "标注id")
    private Long marking_id;

    @ApiModelProperty(value = "创建时间")
    private String create_time;

    @ApiModelProperty(value = "标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)")
    private String annotation_type;

    @ApiModelProperty(value = "标注数据类型(LineString,Polygon,point,pc,p,L)")
    private String location_type;

    @ApiModelProperty(value = "面积")
    private String area;

    @ApiModelProperty(value = "周长")
    private String perimeter;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "标签id")
    private Long category_id;

//    @ApiModelProperty(value = "标注名称")
//    private String measure_full_name;

    @ApiModelProperty(value = "标签名称")
    private String label_name;

    @ApiModelProperty(value = "标注颜色")
    private String label_color;

//    @ApiModelProperty(value = "测量轮廓类型(0:正常,表示有关系,默认为0")
//    private Long measure_type;

//    @ApiModelProperty(value = "测量关系")
//    private String measure_relation;

//    @ApiModelProperty(value = "测量轮廓表示名称:L")
//    private String measure_name;

//    @ApiModelProperty(value = "测量轮廓标识：1")
//    private Long measure_number;

    @ApiModelProperty(value = " 周长（圆）")
    private String radius;

//    @ApiModelProperty(value = "平均间距")
//    private String mean_distance;

//    @ApiModelProperty(value = "最大间距")
//    private String max_distance;

//    @ApiModelProperty(value = "最小间距")
//    private String min_distance;

//    @ApiModelProperty(value = "内角")
//    private String inner_angle;

//    @ApiModelProperty(value = "外角")
//    private String exterior_angle;

//    @ApiModelProperty(value = "中心")
//    private String center_point;

//    @ApiModelProperty(value = "标签总点数")
//    private Long point_count;
    
    @ApiModelProperty(value = "修改作者")
    private String annotation_update_owner;

}
