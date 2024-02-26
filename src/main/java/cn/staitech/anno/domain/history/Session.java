package cn.staitech.anno.domain.history;

import cn.staitech.anno.utils.RocksDBUtil;
import cn.staitech.anno.vo.history.Cursor;
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
     * 用户ID，会话id
     */
    private Long userId;

    private Long slideId;

    /**
     * 全部数据列表
     */
    private LinkedList<Trace> drawList = new LinkedList<>();

    /**
     * 撤消数据列表
     */
    private LinkedList<Trace> undoList = new LinkedList<>();

    public Session(Long userId, Long slideId) {
        this.userId = userId;
        this.slideId = slideId;
    }

    /**
     * 根据traceId获取Trace
     *
     * @param traceId
     * @return
     */
    public Trace getTraceById(String traceId) {
        for (Trace trace : drawList) {
            if (traceId.equals(trace.getTraceId())) {
                return trace;
            }
        }
        return null;
    }

    /**
     * 返回undo/redo状态
     *
     * @return
     */
    public Cursor getStatus() {
        Cursor cursor = new Cursor();
        if (!drawList.isEmpty()) {
            cursor.setUndo(true);
            cursor.setUndoSiz(drawList.size());
            cursor.setDrawList(drawList);
        }

        if (!undoList.isEmpty()) {
            cursor.setRedo(true);
            cursor.setRedoSize(undoList.size());
            cursor.setUndoList(undoList);
        }
        return cursor;
    }


    /**
     * 添加`
     *
     * @param trace
     */
    public void add(Trace trace) {
        drawList.add(trace);
        undoList.clear();
    }


    /**
     * 撤销
     */
    public void undo() {
        if (!drawList.isEmpty()) {
            undoList.add(drawList.get(drawList.size() - 1));
            drawList.remove(drawList.size() - 1);
        }
    }

    /**
     * 恢复
     */
    public void redo() {
        if (!undoList.isEmpty()) {
            drawList.add(undoList.get(undoList.size() - 1));
            undoList.remove(undoList.size() - 1);
        }
    }


    public void cleanList() {
        for (Trace trace : drawList) {
            try {
                RocksDBUtil.cfDeleteIfExist(trace.getTraceId());
            } catch (RocksDBException e) {
                log.info("删除rocksdb数据:{},{}", trace.getTraceId(), e);
            }
        }
        drawList.clear();
        undoList.clear();
    }
}
