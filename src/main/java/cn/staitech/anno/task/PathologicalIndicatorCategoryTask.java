package cn.staitech.anno.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import cn.staitech.anno.config.ScheduleConfig;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;

@Configuration
@EnableScheduling
public class PathologicalIndicatorCategoryTask {

    
    @Resource
    private PathologicalIndicatorCategoryService categoryService;
    
    @Autowired
    private ScheduleConfig scheduleConfig;
    
    /**
     * 
     * tb_pathological_indicator_category 历史数据处理
     * 1、以结构指标为准 
     * 2、如果没有结构指标，以标注区域为准
     * @throws ParseException
     */
    // @Scheduled(cron = "0 0 0/2 * * ?")
    @Scheduled(cron = "#{scheduleConfig.getCron()}")
    public void handlerCouponsUserStatusTimeOutToExpired() throws ParseException {
    	List<Long> dataList = new ArrayList<>();
		//dataList.add(1638L);
    	categoryService.handlerCouponsUserStatusTimeOutToExpired(dataList);
    }
}
