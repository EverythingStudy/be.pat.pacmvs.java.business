package cn.staitech.anno.task;

import cn.staitech.anno.domain.Outline;
import cn.staitech.anno.service.OutlineService;
import cn.staitech.anno.service.SysUserService;
import cn.staitech.common.core.constant.CacheConstants;
import cn.staitech.common.redis.service.RedisService;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 清空token失效后tb_outline对应的数据：上一次执行完毕时间点后30秒再次执行
 *
 * @author wangfeng
 */
@Configuration
@EnableScheduling
public class OutlineTask {

    @Resource
    private OutlineService outlineService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RedisService redisService;

    /**
     * 清空token失效后tb_outline对应的数据：上一次执行完毕时间点后30秒再次执行
     */
    @Scheduled(fixedDelay = 30000)
    public void clean() {
        // 查tb_outline所有用户：create_by
        QueryWrapper<Outline> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT create_by");
        queryWrapper.orderByAsc("create_by");
        List<Outline> createByList = outlineService.list(queryWrapper);
        List<Long> userIds = createByList.stream().map(Outline::getCreateBy).collect(Collectors.toList());

        // 查所有的用户名：user_name
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.select("user_id", "user_name");
        sysUserQueryWrapper.in("user_id", userIds);
        List<SysUser> sysUserList = sysUserService.list(sysUserQueryWrapper);

        // 查Redis中的token是否存在,若不存在则清空tb_outline中对应的数据
        for (SysUser user : sysUserList) {
            String cacheObject = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY + user.getUserName());
            if (Objects.isNull(cacheObject)) {
                outlineService.removeAllBycreateBy(user.getUserId());
            }
        }
    }
}
