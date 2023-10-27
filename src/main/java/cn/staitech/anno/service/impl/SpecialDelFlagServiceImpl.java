package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.special.SpecialReclaim;
import cn.staitech.anno.enums.SpecialEnum;
import cn.staitech.anno.mapper.SpecialMapper;
import cn.staitech.anno.mapper.SpecialReclaimMapper;
import cn.staitech.anno.service.SpecialDelFlagService;
import cn.staitech.anno.utils.TimeUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author gjt.
 * @data 2023/6/8 9:28
 */
@Service
public class SpecialDelFlagServiceImpl implements SpecialDelFlagService {
    @Resource
    private SpecialMapper specialMapper;
    @Resource
    private SpecialReclaimMapper specialReclaimMapper;

    @Override
    @Async
    public void specialDelFlagExpire() {
        // 获取专题回收站中的专题信息
        List<Special> specialList = specialMapper.selectSpecialList();

        for (Special special : specialList) {
            // 根据专题id查询专题回收详情信息
            SpecialReclaim specialReclaim = specialReclaimMapper.selectById(special.getSpecialId());
            // 获取当前时间
            Date dates = new Date();
            // 将当前时间转化为字符串
            String time = TimeUtils.conversionStrings(dates);
            //  将当前时间转化为date
            Date date = TimeUtils.conversionDates(time);
            // 获取到期时间
            Date expireTime = TimeUtils.conversionDates(specialReclaim.getExpireTime());
            // 判断是否达到到期时间
            if (date.compareTo(expireTime) == 0) {
                special.setDelFlag(SpecialEnum.DEL_FLAG_2.value());
                // 更新专题表中状态
                specialMapper.updateDelFlag(special);
                // 发送至消息中
            }
        }
    }
}
