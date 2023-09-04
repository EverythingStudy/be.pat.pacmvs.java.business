package cn.staitech.anno.task;

import cn.staitech.anno.service.SpecialDelFlagService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author gjt.
 * @data 2023/6/8 11:08
 */
@Configuration
@EnableScheduling
public class SpecialDelFlagTask {

    @Resource
    private SpecialDelFlagService specialDelFlagService;

    // 每分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void handlerCouponsUserStatusTimeOutToExpired() {
        // 暂时搁弃
//        specialDelFlagService.specialDelFlagExpire();
    }
}
