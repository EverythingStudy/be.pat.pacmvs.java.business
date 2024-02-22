package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.history.Session;
import cn.staitech.anno.service.HistoryService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.vo.history.HistoryDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wangfeng
 * @create: 2024-02-20 18:11:25
 * @Description: 会话
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    public static final ConcurrentHashMap<Long, Session> USER_SESSION_MAP = new ConcurrentHashMap<>();

    @Resource
    MarkingService markingService;

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

    /**
     * 删除Session
     *
     * @param userId
     */
    @Override
    public void remove(Long userId) {
        if (USER_SESSION_MAP.containsKey(userId)) {
            USER_SESSION_MAP.remove(userId);
        }
    }


    /**
     * 清空Session中的列表
     *
     * @param userId
     */
    @Override
    public void clearSessionList(Long userId) {
        if (USER_SESSION_MAP.containsKey(userId)) {
            USER_SESSION_MAP.get(userId).getList().clear();
        }
    }

    /**
     * 撤消
     *
     * @param dto
     */
    @Override
    public void process(HistoryDTO dto) {

        switch (dto.getEnvType()) {
            case 1:
                undo(dto);
                break;
            case 2:
                redo(dto);
                break;
            default:
        }
    }


    public void undo(HistoryDTO dto) {

        switch (dto.getBizType()) {
            case 1:
                markingService.undo(dto);
                break;
            case 2:
                break;
            default:
        }
    }

    public void redo(HistoryDTO dto) {
        switch (dto.getBizType()) {
            case 1:
                markingService.redo(dto);
                break;
            case 2:
                break;
            default:
        }
    }


}
