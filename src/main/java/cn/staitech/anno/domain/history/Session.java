package cn.staitech.anno.domain.history;

import cn.staitech.common.core.utils.uuid.UUID;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @author: wangfeng
 * @create: 2024-02-20 18:07:05
 * @Description:
 */
@Slf4j
@Data
public class Session {
    /**
     * 队列元素个数最大值
     */
    private static final int LIST_MAX_SIZE = 20;

    /**
     * 用户ID，会话id
     */
    private Long userId;

    /**
     * 链表
     */
    private LinkedList<Trace> list = new LinkedList<>();

    /**
     * 游标
     */
    private Integer cursor;


    public Session(Long userId) {
        this.userId = userId;
    }

    public static void main(String[] args) {
        Long userId = 1L;
        Session session = new Session(userId);
        session.list.addLast(new Trace(userId, "1"));
        session.list.addLast(new Trace(userId, "2"));
        session.list.addLast(new Trace(userId, "3"));
        session.list.addLast(new Trace(userId, "4"));
        session.list.addLast(new Trace(userId, UUID.fastUUID().toString()));

        log.info("session {}", session);

        session.list.removeFirst();
        log.info("session {}", session);
    }

    /**
     * 向列表中添加元素
     *
     * @param trace
     */
    public void addTrace(Trace trace) {
        if (list.size() >= LIST_MAX_SIZE) {
            list.removeFirst();
        }
        list.addLast(trace);
    }

    public Integer setUndoCursor() {
        if (cursor >= 0 && cursor < list.size()) {
            cursor--;
            return cursor;
        }
        return null;
    }

    public Integer setRedoCursor() {
        if (cursor >= 0 && cursor < list.size()) {
            cursor++;
            return cursor;
        }
        return null;
    }

    public Integer resetCursor() {
        if (cursor >= 0 && cursor < list.size()) {
            cursor = list.size();
            return cursor;
        }
        return null;
    }



}
