package cn.staitech.anno.domain;

import cn.staitech.common.core.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 项目图像表 tb_slide
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableName("tb_slide")
public class Slide {

    /**
     * 切片id
     */
    @TableId(value = "slide_id", type = IdType.AUTO)
    @ApiModelProperty(value = "切片id")
    private Long slideId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    private Long groupId;

    /**
     * 图像id
     */
    @ApiModelProperty(value = "图像id")
    private Long imageId;

    /**
     * 人工标注数
     */
    @ApiModelProperty(value = "人工标注数")
    private Integer humanAnnotationTotal;

    /**
     * 算法标注数
     */
    @ApiModelProperty(value = "算法标注数")
    private Integer algorithmAnnotationTotal;

    /**
     * 已审核的切片数
     */
    @ApiModelProperty(value = "已审核的切片数")
    private Integer examinationSlideTotal;

    /**
     * 是否完成该图像的标注
     */
    @ApiModelProperty(value = "标注状态(0未开始 1标注中 2标注完成 3已提交复核)")
    private Integer processFlag;

    /**
     * 切片审核状态
     */
    @ApiModelProperty(value = "复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核不通过 4交付)")
    private Integer examinationFlag;

    /**
     * 切片描述
     */
    @ApiModelProperty(value = "切片描述")
    private String description;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除(0未删除 1已删除)")
    private Integer isDelete;

    @ApiModelProperty("创建者")
    private Long createBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新者")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "geojson文件url地址")
    private String geojsonUrl;

    @ApiModelProperty(value = "轮次id")
    private Long roundId;

}



