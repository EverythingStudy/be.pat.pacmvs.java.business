package cn.staitech.anno.task;

import cn.staitech.anno.service.OtherService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.ParseException;

@Configuration
@EnableScheduling
public class ExamineScoreTask {

    @Resource
    private OtherService otherService;

    /**
     * 两小时执行一次
     *
     * @throws ParseException
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void handlerCouponsUserStatusTimeOutToExpired() throws ParseException {
        otherService.atRegularTimeUpdateExamStatus();
    }
}
