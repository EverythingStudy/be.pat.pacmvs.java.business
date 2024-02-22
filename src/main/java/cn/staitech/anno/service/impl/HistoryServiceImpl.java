package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.history.Session;
import cn.staitech.anno.service.HistoryService;
import cn.staitech.anno.service.MarkingExamineService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.vo.history.Cursor;
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

    public static final ConcurrentHashMap<String, Session> USER_SESSION_MAP = new ConcurrentHashMap<>();

    @Resource
    MarkingService markingService;

    @Resource
    MarkingExamineService markingExamineService;

    @Override
    public void put(Long userId, Long slideId) {
        String key = userId + "_" + slideId;
        if (!USER_SESSION_MAP.containsKey(key)) {
            USER_SESSION_MAP.put(key, new Session(userId, slideId));
        }
    }

    @Override
    public Session get(Long userId, Long slideId) {
        String key = userId + "_" + slideId;
        if (USER_SESSION_MAP.containsKey(key)) {
            return USER_SESSION_MAP.get(key);
        }
        return null;
    }

    /**
     * 删除Session
     *
     * @param key
     */
    @Override
    public void remove(String key) {
        if (USER_SESSION_MAP.containsKey(key)) {
            USER_SESSION_MAP.remove(key);
        }
    }


    /**
     * 清空Session中的列表
     *
     * @param userId
     */
    @Override
    public void clearSessionList(Long userId, Long slideId) {
        String key = userId + "_" + slideId;
        if (USER_SESSION_MAP.containsKey(key)) {
            USER_SESSION_MAP.get(key).getList().clear();
        }
    }

    /**
     * 获取撤消、恢复是否可用的状态
     *
     * @param dto
     */
    @Override
    public Cursor getCursor(HistoryDTO dto) {
        String key = dto.getUserId() + "_" + dto.getSlideId();
        if (USER_SESSION_MAP.containsKey(key)) {
            Session session = USER_SESSION_MAP.get(key);
            Cursor cursor = new Cursor();
            cursor.setUndo(session.undoStatus());
            cursor.setRedo(session.redoStatus());
            return cursor;
        }
        return null;
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
                markingExamineService.undo(dto);
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
                markingExamineService.redo(dto);
                break;
            default:
        }
    }
}
