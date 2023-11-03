package cn.staitech.anno.vo.eyeslide;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EyeSlideIn extends Pager {

    @ApiModelProperty(value = "文件夹名称")
    private String folderName;

    @ApiModelProperty(value ="上传人员id")
    private Long userId;

    @ApiModelProperty(value ="专题id")
    private Long topicId;

    @ApiModelProperty("上传时间")
    private Map<String, Object> params;

}
