package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 眼科切片预测表
 * </p>
 *
 * @author wanglibei
 * @since 2023-11-02
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("aipre_slide_prediction")
@ApiModel(value="SlidePrediction对象", description="眼科切片预测表")
public class SlidePrediction implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "切片预测ID")
    @TableId(value = "slide_prediction_id", type = IdType.AUTO)
    private Long slidePredictionId;

    @ApiModelProperty(value = "项目ID")
    private Long slideId;

    @ApiModelProperty(value = "图像ID")
    private Long imageId;

    @ApiModelProperty(value = "AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败）")
    private Integer aiAnalyzed;
    
    @ApiModelProperty(value = "是否是主图默认为2，1是，2否")
    private String mainImage;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "机构ID")
    private Long organizationId;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;


}
