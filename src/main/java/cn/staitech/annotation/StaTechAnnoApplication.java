package cn.staitech.annotation;

import cn.hutool.extra.spring.EnableSpringUtil;
import cn.staitech.common.security.annotation.EnableCustomConfig;
import cn.staitech.common.security.annotation.EnableRyFeignClients;
import cn.staitech.common.swagger.annotation.EnableCustomSwagger2;
import cn.staitech.annotation.utils.MessageSource;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.TimeZone;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2025/5/21 14:33:43
 */
@EnableSpringUtil
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableTransactionManagement
@MapperScan({"cn.staitech.annotation.mapper"})
public class StaTechAnnoApplication {

    public StaTechAnnoApplication(org.springframework.context.MessageSource messageSource) {
        MessageSource.init(messageSource);
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(StaTechAnnoApplication.class, args);}

    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
