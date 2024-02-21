package cn.staitech.anno.service;

import cn.staitech.anno.domain.history.Session;

/**
 * @author: wangfeng
 * @create: 2024-02-21 11:14:57
 * @Description: 历史记录服务
 */

public interface HistoryService {
    void put(Long userId);

    Session get(Long userId);

    void remove(Long userId);
}
