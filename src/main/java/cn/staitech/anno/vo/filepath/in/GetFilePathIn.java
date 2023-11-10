package cn.staitech.anno.vo.filepath.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/11/10 9:46
 * @desc
 */
@Data

public class GetFilePathIn {
    @ApiModelProperty(value = "Slides:1;Upload:2")
    private int flag;
    @ApiModelProperty(value = "当选择图片时必传")
    private Long organizationId;
    @ApiModelProperty(value = "当选择json时必传")
    private Long projectId;

}
