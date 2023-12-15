package cn.staitech.anno.service;

import cn.staitech.anno.domain.Airepost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * AirepostService接口
 *
 * @author wangfeng
 * @date 2023-11-10
 */
public interface AirepostService extends IService<Airepost> {

    /**
     * 查询Airepost列表
     *
     * @param airepost Airepost
     * @return Airepost集合
     */
    List<Airepost> selectAirepostList(Airepost airepost);

    /**
     * 重置
     *
     * @param airepost Airepost
     * @return Airepost集合
     */
    boolean reset(Airepost airepost);
}
