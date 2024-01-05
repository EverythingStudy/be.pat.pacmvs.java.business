package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Outline;
import cn.staitech.anno.mapper.OutlineMapper;
import cn.staitech.anno.service.OutlineService;
import cn.staitech.anno.vo.outline.OutlineSelectVO;
import cn.staitech.anno.vo.outline.OutlineStatistic;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Outline)表服务实现类
 *
 * @author wangfeng
 * @since 2024-01-04 10:55:03
 */
@Service
public class OutlineServiceImpl extends ServiceImpl<OutlineMapper, Outline> implements OutlineService {

    /**
     * 列表查询
     *
     * @param selectVO
     * @return
     */
    @Override
    public List<Outline> selectList(OutlineSelectVO selectVO) {
        Double minVal = selectVO.getMinVal() != null ? selectVO.getMinVal() : 0.0;

        LambdaQueryWrapper<Outline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Outline::getProjectId, selectVO.getProjectId());
        queryWrapper.eq(Outline::getImageId, selectVO.getImageId());
        queryWrapper.eq(Outline::getSlideId, selectVO.getSlideId());
        queryWrapper.eq(Outline::getCreateBy, selectVO.getCreateBy());

        // 默认查询面积(面积1，周长2)
        if (selectVO.getBizType().equals(2)) {
            queryWrapper.ge(Outline::getPerimeter, minVal);
            if (selectVO.getMaxVal() != null) {
                queryWrapper.le(Outline::getPerimeter, selectVO.getMaxVal());
            }
        } else {
            queryWrapper.ge(Outline::getArea, minVal);
            if (selectVO.getMaxVal() != null) {
                queryWrapper.le(Outline::getArea, selectVO.getMaxVal());
            }
        }

        queryWrapper.orderByAsc(Outline::getOutlineId);
        return list(queryWrapper);
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
     * 异步删除所有当前用户的记录
     *
     * @param createBy 用户ID
     */
    @Async
    @Override
    public void removeAllBycreateBy(Long createBy) {
        Outline outline = new Outline();
        outline.setCreateBy(createBy);
        remove(new QueryWrapper<>(outline));
    }

    /**
     * 异步删除当前用户、非当前slideId的记录
     *
     * @param createBy 用户ID
     * @param slideId  SlideID
     */
    @Async
    @Override
    public void removeBycreateBySlideId(Long createBy, Long slideId) {
        LambdaQueryWrapper<Outline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Outline::getCreateBy, createBy);
        queryWrapper.ne(Outline::getSlideId, slideId);
        remove(queryWrapper);
    }
}

