package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Airepost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * AirepostMapper接口
 *
 * @author wangfeng
 * @date 2023-11-10
 */
public interface AirepostMapper extends BaseMapper<Airepost> {
    /**
     * 查询Airepost列表
     *
     * @param airepost Airepost
     * @return Airepost集合
     */
    List<Airepost> selectAirepostList(Airepost airepost);
}
