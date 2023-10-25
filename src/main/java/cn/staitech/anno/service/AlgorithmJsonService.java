package cn.staitech.anno.service;

import cn.staitech.anno.domain.algorithm.AlgorithmJson;
import cn.staitech.anno.domain.algorithm.in.SelectGeoJson;
import cn.staitech.anno.domain.algorithm.out.SelectGeoJsonList;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
public interface AlgorithmJsonService extends IService<AlgorithmJson> {

    void examineComparison(Long algorithmJsonId) throws Exception;

    JSONObject getGeoJson(SelectGeoJson selectGeoJson) throws Exception;

    SelectGeoJsonList selectUserAndLabelList(SelectGeoJson selectGeoJson);


}
