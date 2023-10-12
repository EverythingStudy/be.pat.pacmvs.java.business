package cn.staitech.anno.queue;

import cn.staitech.anno.service.OtherService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Service
public class DelayQueueExample {

    @Resource
    private OtherService otherService;


    @Async
    public  void addDelayQueueExample(Long examineScoreId) throws InterruptedException {
        DelayQueue<DelayedElement> delayQueue = new DelayQueue<>();
        // 添加元素到 DelayQueue 中
        delayQueue.put(new DelayedElement(examineScoreId, 20, TimeUnit.MINUTES));
        // 从 DelayQueue 中取出元素
        while (!delayQueue.isEmpty()) {
            DelayedElement element = null;
            try {
                element = delayQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                otherService.updateExamStatus(element.getExamineScoreId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class DelayedElement implements Delayed {
    private Long examineScoreId;
    private long expireTime;

    public DelayedElement(Long examineScoreId, long delay, TimeUnit timeUnit) {
        this.examineScoreId = examineScoreId;
        this.expireTime = System.currentTimeMillis() + timeUnit.toMillis(delay);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(expireTime, ((DelayedElement) o).expireTime);
    }

    public Long getExamineScoreId() {
        return examineScoreId;
    }
}