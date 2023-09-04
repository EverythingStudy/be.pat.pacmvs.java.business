package cn.staitech.anno.domain.notice.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author wudi
 * @Date 2023/6/6 10:05
 * @desc 消息查询响应
 */
@Data
public class NoticeQueryOut {
    @ApiModelProperty(value = "专题编号")
    private String specialNumber;

    @ApiModelProperty(value = "专题名称")
    private String specialName;

    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

}
