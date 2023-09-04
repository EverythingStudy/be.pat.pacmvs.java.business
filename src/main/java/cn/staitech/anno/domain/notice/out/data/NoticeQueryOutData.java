package cn.staitech.anno.domain.notice.out.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/6/29 9:56
 * @desc
 */
@Data
public class NoticeQueryOutData {
    @ApiModelProperty("消息id")
    private Long noticeId;
    @ApiModelProperty("消息内容")
    private String noticeContent;
}
