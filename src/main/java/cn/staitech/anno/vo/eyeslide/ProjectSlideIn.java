package cn.staitech.anno.vo.eyeslide;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSlideIn extends Pager {

    @ApiModelProperty(value = "图片名称")
    private String imageName;

    @ApiModelProperty(value ="文件夹名称")
    private String folderName;

    @ApiModelProperty(value = "项目id")
    private Long projectId;



}
