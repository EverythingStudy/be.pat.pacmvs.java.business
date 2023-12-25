package cn.staitech.anno.vo.labelprojectstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LabelOut {
    @ApiModelProperty(value = "标签id")
    private Long categoryId;

    @ApiModelProperty(value = "标签")
    private String categoryName;
}
