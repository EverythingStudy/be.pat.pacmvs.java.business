package cn.staitech.anno.domain.assessment.out;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AssessmentExportOut {

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "切片名称")
    private String imageName;

    @ApiModelProperty(value = "标注名称")
    private String annotationJsonName;

    @ApiModelProperty(value = "算法json文件名称")
    private String jsonName;

    @ApiModelProperty(value = "考核人员（vs）")
    private String examinePeople;

    @ApiModelProperty(value = "考核标签")
    private String examineCategoryName;

    @ApiModelProperty(value = "轮廓个数（vs）")
    private String outlineNumber;

    @ApiModelProperty(value = "漏检率")
    private String missedDetectionRate;

    @ApiModelProperty(value = "误检率")
    private String falseDetectionRate;

    @ApiModelProperty(value = "miou拟合区间")
    private String miou;

    @ApiModelProperty(value = "fiou拟合区间")
    private String fiou;

    @ApiModelProperty(value = "biou拟合区间")
    private String biou;

    @ApiModelProperty(value = "tiou拟合区间")
    private String tiou;


    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

}
