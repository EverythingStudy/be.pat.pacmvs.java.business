package cn.staitech.anno.service;

import cn.staitech.anno.domain.round.Round;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 软次
 */
public interface RoundService extends IService<Round> {
    Map<Long, String> selectMap();

    Map<Long, String> selectMapEn();
}
