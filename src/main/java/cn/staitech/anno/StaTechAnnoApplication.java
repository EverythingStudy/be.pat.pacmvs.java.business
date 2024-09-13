package cn.staitech.anno;

import cn.staitech.anno.utils.MessageSource;
import cn.staitech.common.security.annotation.EnableCustomConfig;
import cn.staitech.common.security.annotation.EnableRyFeignClients;
import cn.staitech.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.TimeZone;

/**
 * @author 94024
 */
@EnableRetry
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableTransactionManagement
@MapperScan({"cn.staitech.anno.mapper"})
@EnableElasticsearchRepositories(basePackages = {"cn.staitech.common.log.elasticsearchRepositories"})
public class StaTechAnnoApplication {

    public StaTechAnnoApplication(org.springframework.context.MessageSource messageSource) {
        MessageSource.init(messageSource);
    }

    public static void main(String[] args) {
        //jvm参数设置时间 -Duser.timezone="Asia/Shanghai"
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(StaTechAnnoApplication.class, args);
        System.out.println("标注模块启动成功");
    }
}
