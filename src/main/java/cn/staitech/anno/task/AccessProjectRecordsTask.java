package cn.staitech.anno.task;

import cn.staitech.anno.service.AccessProjectRecordsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
/**
 *
 * @ClassName: AccessProjectRecordsTask
 * @Description:删除项目访问记录
 * @author zmj
 * @date 2024年2月26日
 * @version V1.0
 */
@Configuration
@EnableScheduling
public class AccessProjectRecordsTask {

    @Resource
    private AccessProjectRecordsService accessProjectRecordsService;


    /**
     *
     * @Description: 删除一个月前的项目访问记录
     * @param
     * @return void
     * @throws
     */
    //	10 0 0 * * ?   每天0点触发

    @Scheduled(cron = "10 0 0 * * ?")
    private void delAccessRecords(){
        accessProjectRecordsService.delAccessRecords();
    }
}
