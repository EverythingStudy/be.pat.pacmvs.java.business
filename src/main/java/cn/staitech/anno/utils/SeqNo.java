package cn.staitech.anno.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class SeqNo {
    private static final String SEQ_KEY = "serial_number:";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param seqKey 根据业务区分
     * @param format 编码格式
     * @return
     */
    public String incr(String seqKey, String format) {
        //serial_number为redis键
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(SEQ_KEY + seqKey, redisTemplate.getConnectionFactory());
        //新增然后获得原子操作
        Long increment = entityIdCounter.incrementAndGet();

        //位数不够，前面补0
        DecimalFormat df = new DecimalFormat(format);
        return df.format(increment);

    }
}