package cn.staitech.annotation.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;


/**
 *  @author staitech
 *  @since : 2024/07/18
 * @version :v0.0.1
 */
@Component
public class InitializinConfig implements InitializingBean {
    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * @see LettuceConnectionFactory
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if(redisConnectionFactory instanceof LettuceConnectionFactory){
            LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory)redisConnectionFactory;
            lettuceConnectionFactory.setValidateConnection(true);
        }
    }

}