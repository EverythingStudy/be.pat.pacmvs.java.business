package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnnotationBroadcastVO {
    
    @ApiModelProperty(value = "切片id")
    private Long slideId;
    
    @ApiModelProperty(value = "标注id")
    private Long annotationId;
    
    @ApiModelProperty(value = "标注类别创建者")
    private String createCategoryName;
    
    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "标注类型")
    private String locationType;
    
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    
    @ApiModelProperty(value = "标注地方位置图形数据")
    private String location;
    
    @ApiModelProperty(value = "用户名称")
    private String userName;
    
    @ApiModelProperty(value = "面积")
    private String measure;
    
    @ApiModelProperty(value = "周长")
    private String perimeter;
    
    @ApiModelProperty(value = "标注类别id")
    private Long categoryId;
    
    @ApiModelProperty(value = "标注类别名称")
    private String categoryName;
    
    @ApiModelProperty(value = "颜色RGB值")
    private String color;
    
    @ApiModelProperty(value = "标注更新者")
    private String updateName;
    
    @ApiModelProperty(value = "标注更新时间")
    private String updateTime;
    
    
}
