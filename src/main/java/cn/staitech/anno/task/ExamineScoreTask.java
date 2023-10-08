package cn.staitech.anno.task;

import cn.staitech.anno.service.OtherService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class ExamineScoreTask {

    @Resource
    private OtherService otherService;

    // 每两小时执行一次
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void handlerCouponsUserStatusTimeOutToExpired() {
        otherService.atRegularTimeUpdateExamStatus();
    }
}
