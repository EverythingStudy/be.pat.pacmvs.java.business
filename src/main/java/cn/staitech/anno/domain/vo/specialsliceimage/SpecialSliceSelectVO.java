package cn.staitech.anno.domain.vo.specialsliceimage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class SpecialSliceSelectVO {

    @ApiModelProperty(value = "页码")
    public int pageNum = 1;
    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;
    @ApiModelProperty(value = "新切片编号")
    private String newImageCode;
    @ApiModelProperty(value = "老切片编号")
    private String imageCode;
    @ApiModelProperty(value = "新切片名称")
    private String newImageName;
    @ApiModelProperty(value = "老切片名称")
    private String imageName;
    @ApiModelProperty(required = false, value = "所属专题")
    private Long belongSpecialId;
    @ApiModelProperty(value = "切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中 ")
    private String sliceImageStatus;
    @ApiModelProperty(value = "审核状态 0：待审核 1：审核通过 2：审核不通过 ")
    private String auditStatus;
    @ApiModelProperty(value = "审核时间")
    private Map<String, Object> createTimeParams;

}
