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

    public static void main(String[] args) {
        Long userId = 1L;
        Session session = new Session(userId);
        session.list.add(new Trace(userId, UUID.fastUUID().toString()));
        session.list.add(new Trace(userId, UUID.fastUUID().toString()));
        session.list.add(new Trace(userId, UUID.fastUUID().toString()));
        session.list.add(new Trace(userId, UUID.fastUUID().toString()));
        session.list.add(new Trace(userId, UUID.fastUUID().toString()));
        session.list.add(new Trace(userId, UUID.fastUUID().toString()));
        session.list.add(new Trace(userId, UUID.fastUUID().toString()));

        log.info("session {}", session);

        session.list.removeFirst();

        log.info("session {}", session);


    }

}
