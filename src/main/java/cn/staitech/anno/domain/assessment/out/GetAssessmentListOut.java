package cn.staitech.anno.domain.assessment.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author wudi
 * @Date 2023/10/17 15:50
 * @desc 算法考核列表分页查询响应
 */
@Data
public class GetAssessmentListOut {

    @ApiModelProperty(value = "算法考核id")
    private Long algorithmAssessmentId;

    @ApiModelProperty(value = "切片编号")
    private String imageName;

    @ApiModelProperty(value = "切片编号")
    private String imageCode;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    /**
     * 缩略图地址
     */
    @ApiModelProperty(value = "缩略图地址")
    private String thumbUrl;

    /**
     * 标注类型id
     */
    @ApiModelProperty(value = "标注类型id")
    private Long categoryId;

    @ApiModelProperty(value = "标注类型描述")
    private String categoryName;

    @ApiModelProperty(value = "标注类型数据集")
    private String[] categoryIds;


    @ApiModelProperty(value = "标注json名称")
    private String annotationJsonName;

    @ApiModelProperty(value = "算法json名称")
    private List<String> algorithmJsonNames;

    @ApiModelProperty(value = "生成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
