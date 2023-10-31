package cn.staitech.anno.domain.specialimageanno;

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
    @ApiModelProperty(value = "标签名称")
    private String label_name;

    @ApiModelProperty(value = "标注颜色")
    private String label_color;
    @ApiModelProperty(value = " 周长（圆）")
    private String radius;
    @ApiModelProperty(value = "修改作者")
    private String annotation_update_owner;
}
