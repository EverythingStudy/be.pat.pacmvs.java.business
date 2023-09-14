package cn.staitech.anno.service;

import cn.staitech.anno.domain.species.Species;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 种属
 */
public interface SpeciesService extends IService<Species> {

    Map<Integer, String> selectMap();
}
