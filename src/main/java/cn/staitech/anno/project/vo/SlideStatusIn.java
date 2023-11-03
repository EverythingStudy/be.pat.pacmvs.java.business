package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/14 09:20:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideStatusIn {
    @ApiModelProperty("切片id集合")
    private List<Long> slideIds;
    @ApiModelProperty("图像状态(0未开始 1标注中 2标注完成 3提交复核(未复核) 4开始复核(复核中) 5复核通过(已复核) 6交付)")
    private String status;
}
