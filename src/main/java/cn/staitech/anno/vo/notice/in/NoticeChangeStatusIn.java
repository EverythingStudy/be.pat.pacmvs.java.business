package cn.staitech.anno.vo.notice.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/6/29 10:15
 * @desc
 */
@Data
public class NoticeChangeStatusIn {
    @ApiModelProperty("消息ID列表")
    private List<Long> noticeList;
}
