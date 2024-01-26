package cn.staitech.anno.task;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import cn.staitech.anno.service.ProjectLabelStatisticsService;

/**
 * 
* @ClassName: AnnoStatisticsTask
* @Description:标注统计
* @author wanglibei
* @date 2024年1月24日
* @version V1.0
 */
@Configuration
@EnableScheduling
public class AnnoStatisticsTask {

	
	@Resource
	private ProjectLabelStatisticsService projectLabelStatisticsService;
	
	
	/**
	 * 
	* @Title: handlerCouponsUserStatusTimeOutToExpired
	* @Description: 生成项目标签数据
	* @param 
	* @return void
	* @throws
	 */
	//	10 0 0 * * ?   每天0点触发
	@Scheduled(cron = "10 0 0 * * ?")
	public void generateProjectLabelStatisticsData() {
		projectLabelStatisticsService.generateData();
	}

}
