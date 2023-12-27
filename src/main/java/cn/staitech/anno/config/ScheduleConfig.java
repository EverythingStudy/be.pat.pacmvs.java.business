package cn.staitech.anno.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取定时任务相关配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "schedule")
public class ScheduleConfig {
    private String cron;

}

