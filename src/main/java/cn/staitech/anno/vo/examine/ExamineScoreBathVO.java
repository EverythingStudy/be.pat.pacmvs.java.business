package cn.staitech.anno.vo.examine;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamineScoreBathVO {

    @ApiModelProperty(value = "评分id")
    private List<Long> examineScoreIdList;

}
