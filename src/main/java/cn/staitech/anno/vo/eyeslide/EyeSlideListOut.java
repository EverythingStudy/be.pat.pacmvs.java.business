package cn.staitech.anno.vo.eyeslide;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EyeSlideListOut {
    @ApiModelProperty(value = "图片名称")
    private String imageName;

    @ApiModelProperty(value = "文件夹名称")
    private String folderName;

    @ApiModelProperty(value = "专题号")
    private String topicName;

    @ApiModelProperty(value = "上传时间")
    private Date createTime;

    @ApiModelProperty(value = "上传人员")
    private String name;

    @ApiModelProperty(value = "文件夹id")
    private Long folderId;


}
