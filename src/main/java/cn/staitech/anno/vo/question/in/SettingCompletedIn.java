package cn.staitech.anno.vo.question.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/9/26 16:22
 * @desc 考核选片-设置完成
 */
@Data
public class SettingCompletedIn {
    @ApiModelProperty(value = "应标个数")
    private Long shouldMarks;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "题库项目列表id")
    private List<Long> dataList;

}
