package cn.staitech.anno.vo.eyeslide;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EyeProjectSlideIn {
    public int pageNum;
    public int pageSize;
    @ApiModelProperty(value = "图片名称")
    private String imageName;
    @ApiModelProperty(value = "文件夹名称")
    private String folderName;
    @ApiModelProperty(value = "项目id", required = true)
    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    private Long projectId;
    @ApiModelProperty(value = "状态", hidden = true)
    private String eyeMent;


}
