package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/14 10:49:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideExportVO {

    /**
     * 切片ID
     */
    private Long slideId;
    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;

    @ApiModelProperty("项目名称")
    private String projectName;

    @Size(max = 200, message = "{PathologicalIndicatorCategory.categoryName.length}")
    @ApiModelProperty("缩略图URL地址")
    @Length(max = 200, message = "{PathologicalIndicatorCategory.categoryName.length}")
    private String thumbUrl;

    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("图片（切片）编号")
    @Length(max = 255, message = "{projectType.length}")
    private String imageCode;

    /**
     * 组别
     */
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("组别")
    @Length(max = 255, message = "{projectType.length}")
    private String groupName;
    /**
     * 性别
     */
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("性别")
    @Length(max = 255, message = "{projectType.length}")
    private String gender;
    /**
     * 种属
     */
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("种属")
    @Length(max = 255, message = "{projectType.length}")
    private String species;
    /**
     * 品系
     */
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("品系")
    @Length(max = 255, message = "{projectType.length}")
    private String productSeries;

    @ApiModelProperty("人工标注数")
    private String manualAnnoDetails;

    @ApiModelProperty("人工标注总数")
    private Integer manualAnnoCount;

    @ApiModelProperty("标注类别")
    private String categoryTypes;

    private Map cates;

    private String remark;

}
