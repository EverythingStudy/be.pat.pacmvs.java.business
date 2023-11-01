package cn.staitech.anno.service;

import cn.staitech.anno.domain.ProductSeries;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 种属
 */
public interface ProductSeriesService extends IService<ProductSeries> {

    Map<Integer, String> selectMap();

    Map<Integer, String> selectMapEn();
}
