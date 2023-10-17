package cn.staitech.anno.service;

import cn.staitech.anno.domain.AlgorithmJson;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
public interface AlgorithmJsonService extends IService<AlgorithmJson> {

    void examineComparison(Long algorithmJsonId) throws Exception;


}
