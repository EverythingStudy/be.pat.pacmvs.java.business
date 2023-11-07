package cn.staitech.anno.vo.eyeslide;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EyeProjectSlideOut {

    @ApiModelProperty(value = "是否是主图（默认为2，1是，2否）")
    private String mainImage;

    @ApiModelProperty(value = "碎片状态默认为0校验通过，1校验不通过")
    private String eyeMent;

    @ApiModelProperty(value = "缩略图url地址")
    private String thumbUrl;

    /**
     * 切片号
     */
    @ApiModelProperty(value = "切片号")
    private String imageName;

    @ApiModelProperty(value = "文件夹名称")
    private String folderName;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;
}


