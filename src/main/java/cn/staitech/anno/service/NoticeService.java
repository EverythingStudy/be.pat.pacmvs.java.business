package cn.staitech.anno.service;

import cn.staitech.anno.vo.notice.in.NoticeChangeStatusIn;
import cn.staitech.anno.vo.notice.out.NoticeListQueryOut;
import cn.staitech.common.core.domain.R;

/**
 * @Author wudi
 * @Date 2023/6/6 10:11
 * @desc
 */
public interface NoticeService {
    /**
     * @return 消息列表2.0
     */
    NoticeListQueryOut getNoticeList();

    /**
     * 消息状态置为已读
     * @param req
     * @return
     */
    R changeStatus(NoticeChangeStatusIn req);
}
