package cn.staitech.anno.task;

import java.text.ParseException;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import cn.staitech.anno.service.PathologicalIndicatorCategoryService;

@Configuration
@EnableScheduling
public class PathologicalIndicatorCategoryTask {


    
    @Resource
    private PathologicalIndicatorCategoryService categoryService;
    /**
     * 
     * tb_pathological_indicator_category 历史数据处理
     * 1、以结构指标为准 
     * 2、如果没有结构指标，以标注区域为准
     * @throws ParseException
     */
    // @Scheduled(cron = "0 0 0/2 * * ?")
   // @Scheduled(cron = "${myScheduled.myCron}")
    public void handlerCouponsUserStatusTimeOutToExpired() throws ParseException {
    	categoryService.handlerCouponsUserStatusTimeOutToExpired(1040L);
    }
}
