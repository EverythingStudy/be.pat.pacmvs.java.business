package cn.staitech.anno.domain.history;

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
    private Integer index;


    public Session(Long userId) {
        this.userId = userId;
    }

//    public static void main(String[] args) {
//        Long userId = 1L;
//        Session session = new Session(userId);
//        session.list.addLast(new Trace(userId, "1", false));
//        session.list.addLast(new Trace(userId, "2", true));
//        session.list.addLast(new Trace(userId, UUID.fastUUID().toString(), false));
//
//        log.info("session {}", session);
//
//        session.list.removeFirst();
//        log.info("session {}", session);
//    }

    /**
     * 根据traceId获取Trace
     *
     * @param traceId
     * @return
     */
    public Trace getTraceById(String traceId) {
        for (Trace trace : list) {
            if (traceId.equals(trace.getTraceId())) {
                return trace;
            }
        }
        return null;
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

        // TODO：区分是原始接口，还是redo操作

        // 重置游标
        resetIndex();
    }

    public Integer setUndoIndex() {
        if (index >= 0 && index < list.size()) {
            index--;
            return index;
        }
        return null;
    }

    public Integer setRedoIndex() {
        if (index >= 0 && index < list.size()) {
            index++;
            return index;
        }
        return null;
    }

    public Integer resetIndex() {
        if (index >= 0 && index < list.size()) {
            index = list.size();
            return index;
        }
        return null;
    }

    /**
     * 获取undo是否可用状态
     *
     * @return
     */
    public Boolean undoStatus() {
        if (index > 0 && index < list.size()) {
            return true;
        }
        return false;
    }

    /**
     * 获取redo是是否可用状态
     *
     * @return
     */
    public Boolean redoStatus() {
        if (index >= 0 && index < list.size() - 1) {
            return true;
        }
        return null;
    }


}
