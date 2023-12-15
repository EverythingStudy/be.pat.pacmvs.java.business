package cn.staitech.anno.vo.eyeslide;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EyeSlideIn extends Pager {

    @ApiModelProperty(value = "文件夹名称")
    private String folderName;


    @Size(min = 0, max = 200, message = "{ImageTopicBatchIdsVO.topicName.length}")
    @ApiModelProperty(value = "所属专题-专题名称")
    private String topicName;

    @ApiModelProperty("上传时间")
    private Map<String, Object> params;

    @ApiModelProperty(value = "上传人员id")
    private Long createBy;

    @ApiModelProperty(value = "项目id")
    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    private Long projectId;

    @ApiModelProperty(value = "机构ID",hidden = true)
    private Long organizationId;

}
