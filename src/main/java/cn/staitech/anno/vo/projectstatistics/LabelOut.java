package cn.staitech.anno.vo.projectstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LabelOut {
    @ApiModelProperty(value = "标签id")
    private Long categoryId;

    @ApiModelProperty(value = "标签名称")
    private String categoryName;
}
