package cn.staitech.anno.domain.vo;

import cn.staitech.anno.domain.Annotation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnnotationsDetailVO extends Annotation {
    
    @NotNull(message = "{SlideUpdateVO.slideId.isnull}")
    @ApiModelProperty(value = "切片id", required = true)
    private Long slideId;
    
    /**
     * 位置 .
     */
    @ApiModelProperty(value = "标注地方位置图形数据", required = true)
    private String location;
    
    /**
     * 标注类型 .
     */
    @ApiModelProperty(value = "标注类型")
    private String locationType;
    
    /**
     * 标注类别id .
     */
    @ApiModelProperty(value = "标注类别id")
    private Long categoryId = 0L;

    /**
     * 项目id .
     */
    @ApiModelProperty(hidden = true,value = "项目id")
    private Long projectId;

    /**
     * 数量 .
     */
    @ApiModelProperty(hidden = true,value = "数量")
    private int sum;

    /**
     * 标注颜色RGB值 .
     */
    @ApiModelProperty(value = "标注（类别）颜色RGB值")
    private String color;

    @ApiModelProperty(value = "标注类别名称")
    private String categoryName;

    /**
     * 创建者id .
     */
    @ApiModelProperty(hidden = true,value = "创建者id")
    private Long createBy;

    /**
     * 创建者用户名 .
     */
    @ApiModelProperty(value = "标注创建者")
    private String createName;

    /**
     * 标注更新者用户名 .
     */
    @ApiModelProperty(value = "标注更新者用户名")
    private String updateName;

    /**
     * ROI正方形标注边长512或1024;默认512.
     */
    @ApiModelProperty(hidden = true,value = "ROI正方形标注边长512或1024，默认512")
    private int distance = 512 ;
}
