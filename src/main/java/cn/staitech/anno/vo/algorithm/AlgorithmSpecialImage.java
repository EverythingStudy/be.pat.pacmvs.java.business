package cn.staitech.anno.vo.algorithm;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: AlgorithmSpecialImage
 * @Description:算法图片交互记录表
 * @date 2023年8月10日
 */
@Data
public class AlgorithmSpecialImage {

    @ApiModelProperty(name = "algorithmSpecialImageId", value = " 选片ID ")
    private Long algorithmSpecialImageId;

    @ApiModelProperty(name = "algorithmUuid", value = " 算法ID ")
    private String algorithmUuid;

    @ApiModelProperty(name = "createTime", value = " 消息生产时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(name = "specialImageId", value = " 选片ID ")
    private Long specialImageId;

    @ApiModelProperty(name = "status", value = " 状态0：未处理；1：解析中2：处理成功；3：处理失败 ")
    private String status;

    @ApiModelProperty(name = "updateTime", value = " 消息消费时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
