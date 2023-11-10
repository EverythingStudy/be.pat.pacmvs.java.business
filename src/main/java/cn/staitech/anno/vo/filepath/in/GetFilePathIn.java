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
    @ApiModelProperty(value = "请求类型，1：选择图片2：选择json")
    private int flag;
    @ApiModelProperty(value = "当选择图片时必传")
    private Long organizationId;
    @ApiModelProperty(value = "当选择json时必传")
    private Long projectId;
    @ApiModelProperty(value = "传之前写死的路径如：/home/pat_saas/Upload|/home/pat_saas/Slides|等")
    private String oldPath;
}
