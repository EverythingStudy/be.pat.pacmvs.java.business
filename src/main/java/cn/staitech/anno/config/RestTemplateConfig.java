package cn.staitech.anno.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: RestTemplateConfig
 * @Description:
 * @date 2023年8月24日
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
