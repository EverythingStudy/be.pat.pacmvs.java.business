package cn.staitech.anno.service;

import cn.staitech.anno.domain.notice.in.NoticeChangeStatusIn;
import cn.staitech.anno.domain.notice.out.NoticeListQueryOut;
import cn.staitech.anno.domain.notice.out.NoticeQueryOut;
import cn.staitech.common.core.domain.R;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/6/6 10:11
 * @desc
 */
public interface NoticeService {
    /**
     *
     * @return 消息列表1.0
     */
    List<NoticeQueryOut> getNotice();

    /**
     *
     * @return 消息列表2.0
     */
    NoticeListQueryOut getNoticeList();

    /**
     * 消息状态置为已读
     * @return
     */
    R changeStatus(NoticeChangeStatusIn req);
}
