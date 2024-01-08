package cn.staitech.anno.service;

import cn.staitech.anno.domain.Outline;
import cn.staitech.anno.vo.outline.OutlineSelectVO;
import cn.staitech.anno.vo.outline.OutlineStatistic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Outline)表服务接口
 *
 * @author makejava
 * @since 2024-01-04 10:55:03
 */
public interface OutlineService extends IService<Outline> {

    /**
     * 查询列表
     *
     * @param selectVO
     * @return
     */
    List<Outline> selectList(OutlineSelectVO selectVO);

    /**
     * 统计
     *
     * @param list
     * @param bziType
     * @return
     */
    OutlineStatistic statistic(List<Outline> list, Integer bziType);

    /**
     * 异步删除所有当前用户的记录
     *
     * @param createBy 用户ID
     */
    void removeByCreateByAndToken(Long createBy, String token);

    /**
     * 异步删除当前用户、非当前slideId的记录
     *
     * @param createBy 用户ID
     * @param slideId  SlideID
     */
    void removeBycreateBySlideId(Long createBy, Long slideId);

    /**
     * 批量保存
     *
     * @param list
     * @param categoryId
     * @throws Exception
     */
    void saveAll(List<Outline> list, Long categoryId) throws Exception;
}

