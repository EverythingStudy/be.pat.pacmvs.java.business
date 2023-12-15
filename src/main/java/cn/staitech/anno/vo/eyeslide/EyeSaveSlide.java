package cn.staitech.anno.vo.eyeslide;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;
@Data
public class EyeSaveSlide {
    @ApiModelProperty(value = "项目id")
    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    private Long projectId;

    @ApiModelProperty(value = "文件夹名称")
    private String folderName;

    @ApiModelProperty(value ="上传人员id")
    private Long createBy;

    @ApiModelProperty("上传时间")
    private Map<String, Object> params;

    @Size(min = 0, max = 200, message = "{ImageTopicBatchIdsVO.topicName.length}")
    @ApiModelProperty(value = "所属专题-专题名称")
    private String topicName;

    @ApiModelProperty(value = "机构ID",hidden = true)
    private Long organizationId;


}
