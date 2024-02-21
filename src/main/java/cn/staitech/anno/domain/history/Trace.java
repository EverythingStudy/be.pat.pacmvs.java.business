package cn.staitech.anno.domain.history;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wangfeng
 * @create: 2024-02-20 18:07:05
 * @Description:
 */
@Data
@AllArgsConstructor
public class Trace {

    /**
     * 用户ID、会话ID
     */
    private Long userId;

    /**
     * 请求ID-可UUID
     */
    private String traceId;

    /**
     * 标注ID列表
     */
    private List<String> markingIds;

    public Trace(Long userId, String traceId) {
        this.userId = userId;
        this.traceId = traceId;
        this.markingIds = new ArrayList<>();
    }
}
