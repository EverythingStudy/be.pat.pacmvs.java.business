package cn.staitech.anno.domain.vo.measurevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MeasureUpdateVO {

    @NotNull(message = "测量对象不可为空")
    @ApiModelProperty(value = "测量id", required = true)
    private Long measureId;

    @NotNull(message = "测量图像不可为空")
    @ApiModelProperty(value = "测量id", required = true)
    private String location;

    @ApiModelProperty(value = "测量id")
    private String locationType;

    @ApiModelProperty(value = "要执行的操作(UNION:相交,DIFFERENCE:相差,null)")
    private String operation;

    @ApiModelProperty(value = "被测量图像A")
    private Long maIdA;

    @ApiModelProperty(value = "被测量图像B")
    private Long maIdB;

    @ApiModelProperty(value = "圆心")
    private String centerPoint;

    @ApiModelProperty(value = "半径")
    private String radius;

    @ApiModelProperty(value = "内角")
    private String innerAngle;

    @ApiModelProperty(value = "外角")
    private String exteriorAngle;

    @ApiModelProperty(value = "平均间距")
    private String meanDistance;

    @ApiModelProperty(value = "最小间距")
    private String minDistance;

    @ApiModelProperty(value = "最大间距")
    private String maxDistance;

    @ApiModelProperty(value = "权限标识", required = true)
    private String permission;
}
