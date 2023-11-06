package cn.staitech.anno.vo.eyeslide;

import cn.staitech.anno.domain.Image;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProjectSlideOut extends Image {
    @ApiModelProperty(value = "缩略图url地址")
    private String thumbUrl;

    @ApiModelProperty(value = "是否是主图1是，2否")
    private String mainImage;

    @ApiModelProperty(value = "图片名称")
    private String imageName;

    @ApiModelProperty(value ="文件夹名称")
    private String folderName;

    @ApiModelProperty(value = "状态（0校验通过，1校验不通过）")
    private String eyeMent;

    @ApiModelProperty(value = "文件夹id")
    private Long folderId;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "切片预测ID")
    private Long slidePredictionId;


}
