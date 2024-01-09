package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Outline;
import cn.staitech.anno.mapper.OutlineMapper;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.service.OutlineService;
import cn.staitech.anno.vo.geojson.in.ViewAddIn;
import cn.staitech.anno.vo.outline.OutlineRoot;
import cn.staitech.anno.vo.outline.OutlineSelectVO;
import cn.staitech.anno.vo.outline.OutlineStatistic;
import cn.staitech.common.redis.service.RedisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Outline)表服务实现类
 *
 * @author wangfeng
 * @since 2024-01-04 10:55:03
 */
@Service("OutlineRedisServiceImpl")
public class OutlineRedisServiceImpl extends ServiceImpl<OutlineMapper, Outline> implements OutlineService {

    @Resource
    private MarkingService markingService;

    @Resource
    private RedisService redisService;

    /**
     * 列表查询
     *
     * @param selectVO
     * @return
     */
    @Override
    public List<Outline> selectList(OutlineSelectVO selectVO) {

        Long createBy = selectVO.getCreateBy();
        String rootKey = "OUTLINE_ROOT:" + createBy;
        String listKey = "OUTLINE_LIST:" + createBy + "_";

        OutlineRoot outlineRoot = redisService.getCacheObject(rootKey);
        List<Outline> srcList = redisService.getCacheList(listKey + outlineRoot.getToken());

        if (CollectionUtils.isEmpty(srcList)) {
            return null;
        }

        Double minVal = selectVO.getMinVal() != null ? selectVO.getMinVal() : 0.0;

        List<Outline> list;

        // 默认查询面积(面积1，周长2)
        if (selectVO.getBizType().equals(2)) {
            if (selectVO.getMaxVal() != null) {
                list = srcList.stream()
                        .filter(outline -> outline.getPerimeter() > minVal)
                        .filter(outline -> outline.getPerimeter() < selectVO.getMaxVal())
                        .collect(Collectors.toList());
            } else {
                list = srcList.stream()
                        .filter(outline -> outline.getPerimeter() > minVal)
                        .collect(Collectors.toList());
            }
        } else {
            if (selectVO.getMaxVal() != null) {
                list = srcList.stream()
                        .filter(outline -> outline.getArea() > minVal)
                        .filter(outline -> outline.getArea() < selectVO.getMaxVal())
                        .collect(Collectors.toList());
            } else {
                list = srcList.stream()
                        .filter(outline -> outline.getArea() > minVal)
                        .collect(Collectors.toList());
            }
        }


        return list;
    }

    /**
     * 统计
     *
     * @param list
     * @param bizType
     * @return
     */
    @Override
    public OutlineStatistic statistic(List<Outline> list, Integer bizType) {
        // 查询业务类型：1面积(默认),2周长
        bizType = bizType != null ? bizType : 1;

        // 平均值
        double average;
        // 标准偏差
        Double standardDeviation;
        // 总和
        Double sum;
        // 总个数
        Integer total = list.size();
        // 最小值
        Double minValue;
        // 最大值
        Double maxValue;

        List<Double> doubles;
        // 默认查询面积
        if (bizType.equals(2)) {
            doubles = list.stream().map(Outline::getPerimeter).collect(Collectors.toList());
        } else {
            doubles = list.stream().map(Outline::getArea).collect(Collectors.toList());
        }

        // 对周长或面积求和、最小值、最大值
        sum = doubles.stream().mapToDouble(num -> num).sum();
        minValue = doubles.stream().mapToDouble(num -> num).min().getAsDouble();
        maxValue = doubles.stream().mapToDouble(num -> num).max().getAsDouble();

        // 平均值
        average = sum / total;

        // 对面积、周长求方差
        double sumOfSquares = doubles.stream()
                .mapToDouble(db -> Math.pow((db - average), 2))
                .sum();

        // 母体方差
        double variance = sumOfSquares / total;
        // 样本方差
        // double variance = sumOfSquares / (total - 1);

        // 标准差
        standardDeviation = Math.sqrt(variance);

        OutlineStatistic statistic = new OutlineStatistic();
        statistic.setList(list);
        statistic.setBizType(bizType);
        statistic.setAverage(average);
        statistic.setStandardDeviation(standardDeviation);
        statistic.setSum(sum);
        statistic.setTotal(total);
        statistic.setMinValue(minValue);
        statistic.setMaxValue(maxValue);

        return statistic;
    }

    /**
     * 异步删除所有当前用户、非当前token的记录
     *
     * @param createBy 用户ID
     * @param token    用户token
     */
    @Async
    @Override
    public void removeByCreateByAndToken(Long createBy, String token) {
        String rootKey = "OUTLINE_ROOT:" + createBy;
        String listKey = "OUTLINE_LIST:" + createBy + "_";

        OutlineRoot outlineRoot = redisService.getCacheObject(rootKey);

        if (createBy > 0 && StringUtils.isEmpty(token)) {
            // 清空当前用户非当前token的数据
            // 当前用户所有数据的Key
            Collection<String> keyCollection = redisService.keys(listKey + "*");
            for (String keyStr : keyCollection) {
                if (!keyStr.equals(listKey + outlineRoot.getToken())) {
                    redisService.deleteObject(keyStr);
                }
            }
        } else {
            // 清空当前用户全部数据
            redisService.deleteObject(listKey + "*");
        }
    }

    /**
     * 异步删除所有当前用户、非当前slideId的记录
     *
     * @param createBy 用户ID
     * @param slideId  SlideID
     */
    @Async
    @Override
    public void removeBycreateBySlideId(Long createBy, Long slideId) {
        String rootKey = "OUTLINE_ROOT:" + createBy;
        String listKey = "OUTLINE_LIST:" + createBy + "_";

        OutlineRoot outlineRoot = redisService.getCacheObject(rootKey);

        if (createBy > 0 && slideId > 0 && outlineRoot.getSlideId().equals(slideId)) {
            // 清空当前用户非当前token的数据
            // 当前用户所有数据的Key
            Collection<String> keyCollection = redisService.keys(listKey + "*");
            for (String keyStr : keyCollection) {
                if (!keyStr.equals(listKey + outlineRoot.getToken())) {
                    redisService.deleteObject(keyStr);
                }
            }
        } else {
            // 清空当前用户全部数据
            redisService.deleteObject(listKey + "*");
        }
    }

    /**
     * 批量保存
     *
     * @param list
     * @param categoryId
     * @throws Exception
     */
    @Override
    public void saveAll(List<Outline> list, Long categoryId) throws Exception {
        // 逐一添加
        for (Outline outline : list) {
            ViewAddIn marking = new ViewAddIn();
            marking.setCategory_id(categoryId);
            marking.setGeometry(outline.getGeometry());
            marking.setSlide_id(outline.getSlideId());
            marking.setPerimeter(outline.getPerimeter().toString());
            marking.setArea(outline.getArea().toString());
            marking.setCreate_by(outline.getCreateBy());
            markingService.insert(marking);
        }

        // 删除所有当前用户的记录
        removeByCreateByAndToken(categoryId, null);
    }
}

