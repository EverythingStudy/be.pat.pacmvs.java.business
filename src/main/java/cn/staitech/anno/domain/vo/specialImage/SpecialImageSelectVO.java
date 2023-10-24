package cn.staitech.anno.domain.vo.specialImage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SpecialImageSelectVO {

    @ApiModelProperty(value = "页码")
    public int pageNum = 1;
    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;
    @ApiModelProperty(value = "切片编号")
    private String imageCode;
    @ApiModelProperty(value = "切片名称")
    private String imageName;
    @ApiModelProperty(value = "桩体切片id")
    private Long specialImageId;
    @ApiModelProperty(value = "创建人id")
    private Long createBy;
    @ApiModelProperty(required = false, value = "所属专题")
    private Long belongSpecialId;
    @ApiModelProperty(required = false, value = "添加状态 0：全部 1：已添加 2：未添加 ")
    private int joinStatus;
    @ApiModelProperty(value = "切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中 ")
    private String sliceImageStatus;
    @ApiModelProperty(value = "上传时间")
    private Map<String, Object> createTimeParams;
    @ApiModelProperty(required = false, value = "专题ID ")
    private int topicId;
    @ApiModelProperty(required = false, value = "专题名称 ")
    private String topicName;
    @ApiModelProperty(required = false, value = "所属专题")
    private Long specialId;
    /**
     * 是否可用（0不可用1可用）
     */
    @ApiModelProperty(value = "是否可用 （0不可用1可用）")
    private Integer status;
    @ApiModelProperty(required = false, value = "")
    private List<Long> specialImageIdList;
    /**
     * 逻辑删除状态（0删除，1未删除）
     */
    @ApiModelProperty(value = "逻辑删除状态（0删除，1未删除）")
    private Integer deleteFlag;

}
