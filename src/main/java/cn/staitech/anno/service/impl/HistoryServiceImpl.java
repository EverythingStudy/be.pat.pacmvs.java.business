package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.history.Session;
import cn.staitech.anno.service.HistoryService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wangfeng
 * @create: 2024-02-20 18:11:25
 * @Description: 会话
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    public static final ConcurrentHashMap<Long, Session> USER_SESSION_MAP = new ConcurrentHashMap<>();

    @Override
    public void put(Long userId) {
        if (!USER_SESSION_MAP.containsKey(userId)) {
            USER_SESSION_MAP.put(userId, new Session(userId));
        }
    }

    @Override
    public Session get(Long userId) {
        if (USER_SESSION_MAP.containsKey(userId)) {
            return USER_SESSION_MAP.get(userId);
        }
        return null;
    }

    @Override
    public void remove(Long userId) {
        if (USER_SESSION_MAP.containsKey(userId)) {
            USER_SESSION_MAP.remove(userId);
        }
    }
}
