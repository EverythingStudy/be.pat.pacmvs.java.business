package cn.staitech.anno.task;

import cn.staitech.anno.utils.RocksDBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static cn.staitech.anno.service.impl.HistoryServiceImpl.USER_SESSION_MAP;

/**
 * 清空token失效后Redis对应的数据：上一次执行完毕时间点后30秒再次执行
 *
 * @author wangfeng
 */
@Slf4j
@Configuration
@EnableScheduling
public class RocksdbTask {
    /**
     * 定时任务：按时间执行的定时任务，在每天2:00执行一次。
     */
    // @Scheduled(cron = "0 0 2 * * ?")
    public void clean() {
        try {
            // 清空RocksDB
            // deleteAllColumnFamily();
            USER_SESSION_MAP.clear();
            RocksDBUtil.init();
        } catch (Exception e) {
            log.info("清除rocksdb数据{}", e);
        }
    }
}
