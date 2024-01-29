package cn.staitech.anno.vo.notice.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/6/26 17:58
 * @desc 消息列表查询
 */
@Data
public class NoticeListQueryOut {

    @ApiModelProperty(name = "未读消息列表", notes = "未读消息列表")
    private List<NoticeQueryOutData> unreadList;
    @ApiModelProperty(name = "已读消息列表", notes = "已读消息列表")
    private List<NoticeQueryOutData> readList;
    @ApiModelProperty(name = "公告列表", notes = "公告列表")
    private List<NoticeQueryOutData> boardList;
}
