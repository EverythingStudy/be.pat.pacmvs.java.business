package cn.staitech.anno.domain.document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "geo_location")
public class GeometryDoc {

    /** 主键 */
    @Id
    @ApiModelProperty(value = "操作序号ID")
    private Long id;

    /** 标注地方位置图形数据 */
    @Field(type = FieldType.Long, store = true)
    @ApiModelProperty(value = "标注地方位置图形数据")
    private Long marking_id;

    /** 标注地方位置图形数据 */
    @Field(type = FieldType.Text, store = true)
    @ApiModelProperty(value = "标注地方位置图形数据")
    private String geometry;

    @Field(type = FieldType.Long, store = true)
    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "作者(默认0,AI绘制为图片上传作者，其他为操作用户ID)")
    private Long annotation_owner;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "创建时间")
    private String create_time;

    @Field(type = FieldType.Keyword)
    @ApiModelProperty(value = "标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)")
    private String annotation_type;

    @Field(type = FieldType.Keyword)
    @ApiModelProperty(value = "标注数据类型(LineString,Polygon,point,pc,p,L)")
    private String location_type;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "面积")
    private String area;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "周长")
    private String perimeter;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "描述")
    private String description;

    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "标签id")
    private Long category_id;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "标注名称")
    private String measure_full_name;

    @Field(type = FieldType.Keyword,store = true)
    @ApiModelProperty(value = "标签名称")
    private String label_name;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "标注颜色")
    private String label_color;

    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "测量轮廓类型(0:正常,表示有关系,默认为0")
    private Long measure_type;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "测量关系")
    private String measure_relation;

    @Field(type = FieldType.Keyword)
    @ApiModelProperty(value = "测量轮廓表示名称:L")
    private String measure_name;

    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "测量轮廓标识：1")
    private Long measure_number;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = " 周长（圆）")
    private String radius;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "平均间距")
    private String mean_distance;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "最大间距")
    private String max_distance;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "最小间距")
    private String min_distance;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "内角")
    private String inner_angle;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "外角")
    private String exterior_angle;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "中心")
    private String center_point;

    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "点总数")
    private Long point_count;
}
