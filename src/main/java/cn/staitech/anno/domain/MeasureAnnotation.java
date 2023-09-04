package cn.staitech.anno.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MeasureAnnotation {
    
    /**
     * 测量自增id .
     */
    @ApiModelProperty(value = "测量自增id")
    private Long maId;
    
    /**
     * 项目id .
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;
    
    /**
     * 图像id .
     */
    @ApiModelProperty(value = "图像id")
    private Long imageId;
    
    /**
     * 切片id .
     */
    @ApiModelProperty(value = "切片id")
    private Long slideId;
    
    
    /**
     * 测量编号 .
     */
    @ApiModelProperty(value = "测量编号")
    private String measureNum;
    
    /**
     * 标注类型 .
     */
    @ApiModelProperty(value = "标注类型")
    private String locationType;
    
    /**
     * 标注地方位置图形数据 .
     */
    @ApiModelProperty(value = "标注地方位置图形数据")
    private String location;
    
    /**
     * 周长 .
     */
    @ApiModelProperty(value = "周长")
    private String perimeter;
    
    /**
     * 面积 .
     */
    @ApiModelProperty(value = "面积")
    private String area;
    
    /**
     * 圆半径
     */
    private String radius;
    
    /**
     * 圆心
     */
    private String centerPoint;
    
    /**
     * 创建人id .
     */
    private Long createBy;
    
    /**
     * 创建时间 .
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    
    /**
     * 更新者 .
     */
    private Long updateBy;
    
    /**
     * 更新时间 .
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
    
}
