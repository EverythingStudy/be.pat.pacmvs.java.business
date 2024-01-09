package cn.staitech.anno.task;

import cn.staitech.common.core.constant.CacheConstants;
import cn.staitech.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 清空token失效后Redis对应的数据：上一次执行完毕时间点后30秒再次执行
 *
 * @author wangfeng
 */
@Slf4j
@Configuration
@EnableScheduling
public class OutlineRedisTask {
    @Resource
    private RedisService redisService;

    /**
     * 定时任务：Redis版 - 清空token失效后tb_outline对应的数据，频率：上一次执行完毕时间点后30秒再次执行
     */
    @Scheduled(fixedDelay = 30000)
    public void clean() {
        String rootKey = "OUTLINE_ROOT:";
        String listKey = "OUTLINE_LIST:";

        // 在线用户map:<userId, token>
        Map<Long, String> loginMap = new HashMap<>(16);
        Collection<String> loginKeyCollection = redisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        for (String loginKey : loginKeyCollection) {
            if (loginKey.length() == 49) {
                String token = loginKey.replaceFirst(CacheConstants.LOGIN_TOKEN_KEY, "");
                cn.staitech.system.api.model.LoginUser loginUser = redisService.getCacheObject(loginKey);
                loginMap.put(loginUser.getUserid(), token);
            }
        }

        // 遍历OUTLINE_LIST键,删除token无效数据
        Collection<String> listKeyCollection = redisService.keys(listKey + "*");
        for (String cListKey : listKeyCollection) {
            String cListKeyTmp = cListKey.replaceFirst(listKey, "");
            Long userId = Long.valueOf(cListKeyTmp.split("_")[0]);
            String token = cListKeyTmp.split("_")[1];
            if (!loginMap.containsKey(userId)) {
                redisService.deleteObject(cListKey);
            } else if (!loginMap.get(userId).equals(token)) {
                redisService.deleteObject(cListKey);
            }
        }

        // 遍历OUTLINE_ROOT键,删除token无效数据
        Collection<String> rootKeyCollection = redisService.keys(rootKey + "*");
        for (String cRootKey : rootKeyCollection) {
            Long userId = Long.valueOf(cRootKey.replaceFirst(rootKey, ""));
            if (!loginMap.containsKey(userId)) {
                redisService.deleteObject(cRootKey);
            }
        }
    }
}
