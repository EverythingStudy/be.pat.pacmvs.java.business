package cn.staitech.anno.service;

import cn.staitech.anno.domain.history.Session;
import cn.staitech.anno.vo.history.Cursor;
import cn.staitech.anno.vo.history.HistoryDTO;

/**
 * @author: wangfeng
 * @create: 2024-02-21 11:14:57
 * @Description: 历史记录服务
 */

public interface HistoryService {
    void put(Long userId);

    Session get(Long userId);

    void remove(Long userId);

    /**
     * 清空Session中的列表
     *
     * @param userId
     */
    void clearSessionList(Long userId);


    Cursor getCursor(HistoryDTO dto);

    void process(HistoryDTO dto);
}
