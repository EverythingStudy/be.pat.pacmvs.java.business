package cn.staitech.anno.task;

import cn.staitech.anno.domain.history.Session;
import cn.staitech.anno.domain.history.Trace;
import cn.staitech.anno.service.impl.HistoryServiceImpl;
import cn.staitech.anno.utils.RocksDBUtil;
import cn.staitech.common.core.constant.CacheConstants;
import cn.staitech.common.redis.service.RedisService;
import io.vertx.core.impl.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.RocksDBException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static cn.staitech.anno.constant.CommonConstant.REDIS_OUTLINE_LIST;
import static cn.staitech.anno.constant.CommonConstant.REDIS_OUTLINE_ROOT;
import static cn.staitech.anno.utils.RocksDBUtil.USER_ROCKS_MAP;

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
     * 定时任务：Redis版 - 清空token失效后对应的业务数据，
     * 频率：上一次执行完毕时间点后30秒再次执行
     * business type 1: viewer页面-吸管功能对应redis数据
     * business type 2: viewer页面-撤消、恢复历史记录数据
     */
    //@Scheduled(fixedDelay = 30000)
    @Scheduled(fixedDelay = 30000)
    public void clean() {
        String rootKey = REDIS_OUTLINE_ROOT;
        String listKey = REDIS_OUTLINE_LIST;

        // 获取token有效用户(在线用户)数据，map:<userId, token>
        Map<Long, String> loginMap = new HashMap<>(16);
        ConcurrentHashSet<Long> loginHashSet = new ConcurrentHashSet<>();

        Collection<String> loginKeyCollection = redisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        for (String loginKey : loginKeyCollection) {
            if (loginKey.length() == 49) {
                String token = loginKey.replaceFirst(CacheConstants.LOGIN_TOKEN_KEY, "");
                cn.staitech.system.api.model.LoginUser loginUser = redisService.getCacheObject(loginKey);
                loginMap.put(loginUser.getUserid(), token);
                loginHashSet.add(loginUser.getUserid());
                log.info("loginMap: {}:{}", loginUser.getUserid(), token);
            }
        }

        // 删除token无效数据：遍历OUTLINE_LIST键
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

        // 删除token无效数据：遍历OUTLINE_ROOT键
        Collection<String> rootKeyCollection = redisService.keys(rootKey + "*");
        for (String cRootKey : rootKeyCollection) {
            Long userId = Long.valueOf(cRootKey.replaceFirst(rootKey, ""));
            if (!loginMap.containsKey(userId)) {
                redisService.deleteObject(cRootKey);
            }
        }

        // 删除token无效对应数据:遍历撤消、恢复历史记录 v2
        for (Map.Entry<Long, ConcurrentHashSet<String>> entry : USER_ROCKS_MAP.entrySet()) {
            log.info("[USER_SESSION_MAP]删除token失效用户rocksdb数据:{}", entry);
            Long key = entry.getKey();
            if (!loginHashSet.contains(key)) {
                for (String triceId : entry.getValue()) {
                    try {
                        RocksDBUtil.cfDeleteIfExist(triceId);
                    } catch (RocksDBException e) {
                        log.info("[USER_SESSION_MAP]定时任务删除废弃rocksdb数据:{},{}", triceId, e);
                    }
                }
                USER_ROCKS_MAP.remove(key);
            }
        }


        // 删除token无效对应数据:遍历撤消、恢复历史记录 v1
        for (Map.Entry<String, Session> entry : HistoryServiceImpl.USER_SESSION_MAP.entrySet()) {
            log.info("[USER_SESSION_MAP]删除token失效用户rocksdb数据:{}", entry);
            String key = entry.getKey();
            Long userId = Long.valueOf(key.split("_")[0]);
            if (!loginHashSet.contains(userId)) {
                Session session = entry.getValue();
                LinkedList<Trace> tracesList = session.getDrawList();
                for (Trace trace : tracesList) {
                    try {
                        RocksDBUtil.cfDeleteIfExist(trace.getTraceId());
                    } catch (RocksDBException e) {
                        log.info("[USER_SESSION_MAP]定时任务删除废弃rocksdb数据:{},{}", trace.getTraceId(), e);
                    }
                }
                HistoryServiceImpl.USER_SESSION_MAP.remove(entry.getKey());
            }
        }


    }
}
