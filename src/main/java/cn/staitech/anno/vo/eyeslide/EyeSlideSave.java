package cn.staitech.anno.vo.eyeslide;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EyeSlideSave {
    @ApiModelProperty(value = "项目id",required = true)
    @NotNull(message = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "文件id")
    private List<Long> folderList;

}
