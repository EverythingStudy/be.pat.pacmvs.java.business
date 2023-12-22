package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ProductSeries;
import cn.staitech.anno.mapper.ProductSeriesMapper;
import cn.staitech.anno.service.ProductSeriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 种属
 */
@Service
class ProductSeriesServiceImpl extends ServiceImpl<ProductSeriesMapper, ProductSeries> implements ProductSeriesService {
    @Resource
    private ProductSeriesMapper productSeriesMapper;

    @Override
    public Map<String, String> selectMap() {
        return select(false);
    }

    @Override
    public Map<String, String> selectMapEn() {
        return select(true);
    }

    public Map<String, String> select(boolean en) {
        List<ProductSeries> list = productSeriesMapper.selectList();
        if (en) {
            // 20231222wangfeng
            // return list.stream().collect(Collectors.toMap(ProductSeries::getProductSeriesId, ProductSeries::getNameEn));
            return list.stream().collect(Collectors.toMap(item -> item.getOrganizationId().toString() + item.getSpeciesId() + item.getProductSeriesId().toString(), ProductSeries::getNameEn));
        } else {
            return list.stream().collect(Collectors.toMap(item -> item.getOrganizationId().toString() + item.getSpeciesId() + item.getProductSeriesId().toString(), ProductSeries::getName));
        }
    }
}
