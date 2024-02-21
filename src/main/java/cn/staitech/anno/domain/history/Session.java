package cn.staitech.anno.domain.history;

import lombok.Data;

import java.util.LinkedList;

/**
 * @author: wangfeng
 * @create: 2024-02-20 18:07:05
 * @Description:
 */
@Data
public class Session {
    /**
     * 用户ID，会话id
     */
    private Long userId;

    /**
     * 链表
     */
    private LinkedList<Trace> list = new LinkedList<>();


    public Session(Long userId) {
        this.userId = userId;
    }

}
