package cn.staitech.anno.vo.assessment.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/10/18 10:31
 * @desc
 */
@Data
public class GetJsonInfoDataIn {

    /**
     * 标注json名称
     */
    @ApiModelProperty("算法json名称")
    private String algorithmJsonName;

    /**
     * 标注json路径
     */
    @ApiModelProperty("算法json路径")
    private String algorithmJsonUrl;
}
