package cn.staitech.anno.domain.assessment.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/10/17 15:24
 * @desc
 */
@Data
public class CreateAssessmentDataIn {

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    /**
     * 缩略图地址
     */
    @ApiModelProperty(value ="缩略图地址" )
    private String thumbUrl;

    /**
     * 标注类型id
     */
    @ApiModelProperty(value = "标注类型id")
    private Integer categoryId;

    /**
     * 标注类型名称
     */
    @ApiModelProperty(value = "标注类型名称")
    private String categoryName;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "切片编号")
    private String imageName;


}
