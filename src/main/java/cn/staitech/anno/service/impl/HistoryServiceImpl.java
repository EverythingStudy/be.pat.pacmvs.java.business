package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.history.Session;
import cn.staitech.anno.service.HistoryService;
import cn.staitech.anno.service.MarkingExamineService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.vo.history.Cursor;
import cn.staitech.anno.vo.history.HistoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wangfeng
 * @create: 2024-02-20 18:11:25
 * @Description: 会话
 */
@Slf4j
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
     * 清空Session中的列表、游标置零、清空rocksDB中的数据
     *
     * @param userId
     */
    @Override
    public void clearSessionList(Long userId, Long slideId) {
        String key = userId + "_" + slideId;
        if (USER_SESSION_MAP.containsKey(key)) {
            USER_SESSION_MAP.get(key).cleanList();
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
        Cursor cursor = new Cursor();
        if (USER_SESSION_MAP.containsKey(key)) {
            Session session = USER_SESSION_MAP.get(key);
            cursor.setUndo(session.undoStatus());
            cursor.setRedo(session.redoStatus());
            cursor.setIndex(session.getIndex());
            cursor.setSize(session.getList().size());
        }
        return cursor;
    }


    /**
     * 撤消
     *
     * @param dto
     */
    @Override
    public void process(HistoryDTO dto) {
        switch (dto.getBizType()) {
            case 1:
                markingService.undoOrRedo(dto);
                break;
            case 2:
                markingExamineService.undoOrRedo(dto);
                break;
            default:
        }
    }

}
