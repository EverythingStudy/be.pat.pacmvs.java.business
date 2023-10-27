package cn.staitech.anno.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author gjt.
 * @data 2023/6/8 11:08
 */
@Configuration
@EnableScheduling
public class SpecialDelFlagTask {

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void handlerCouponsUserStatusTimeOutToExpired() {

    }
}
