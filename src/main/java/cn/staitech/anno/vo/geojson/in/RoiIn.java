package cn.staitech.anno.vo.geojson.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RoiIn {
    @ApiModelProperty(value = "标注信息",required = true)
    private List<ViewAddIn> viewAddIns;

    @NotNull(message ="{MarkingJsonIn.status.notNull}" )
    @ApiModelProperty(value ="0包含，1删除",required = true)
    private Integer roiStatus;
}
