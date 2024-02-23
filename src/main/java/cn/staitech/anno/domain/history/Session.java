package cn.staitech.anno.domain.history;

import cn.staitech.anno.utils.RocksDBUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.RocksDBException;

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
    private static final int LIST_MAX_SIZE = 5;

    /**
     * 用户ID，会话id
     */
    private Long userId;

    private Long slideId;

    /**
     * 链表
     */
    private LinkedList<Trace> list = new LinkedList<>();

    /**
     * 游标
     */
    private Integer index = -1;


    public Session(Long userId, Long slideId) {
        this.userId = userId;
        this.slideId = slideId;
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
    public void addTrace(Trace trace, Boolean isHistory, Boolean isUndo) {
        try {
            // 超出最大数量删除
            if (list.size() >= LIST_MAX_SIZE) {
                Trace traceFirst = list.getFirst();
                RocksDBUtil.cfDeleteIfExist(traceFirst.getTraceId());
                list.removeFirst();
            }

            list.addLast(trace);
            // 区分是原始接口，还是redo操作
            if (isHistory) {
                if (isUndo) {
                    setUndoIndex();
                } else {
                    setRedoIndex();
                }
            } else {
                // 重置游标
                resetIndex();
            }
        } catch (RocksDBException e) {
            log.info("addTrace:{}", e);
        }
    }

    public Integer setUndoIndex() {
        if (index > -1 && index <= list.size()) {
            index--;
            return index;
        }
        return null;
    }

    public Integer setRedoIndex() {
        if (index >= -1 && index < list.size()) {
            index++;
            return index;
        }
        return null;
    }


    public void resetIndex() {
        index = list.size() - 1;
    }

    /**
     * 获取undo是否可用状态
     *
     * @return
     */
    public Boolean undoStatus() {
        if (index >= 0 && index < list.size()) {
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
        if (index > 0 && index < list.size()) {
            return true;
        }
        return false;
    }

    public void cleanList() {
        for (Trace trace : list) {
            try {
                RocksDBUtil.cfDeleteIfExist(trace.getTraceId());
            } catch (RocksDBException e) {
                log.info("删除rocksdb数据:{},{}", trace.getTraceId(), e);
            }
        }
        list.clear();
        setIndex(-1);
    }
}
