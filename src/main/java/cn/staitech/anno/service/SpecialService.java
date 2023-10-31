package cn.staitech.anno.service;

import cn.staitech.anno.domain.special.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gjt.
 * @data 2023/5/29 9:13
 */
public interface SpecialService extends IService<Special> {

    /**
     * 查询专题阅片下的专题列表
     *
     * @return Special
     */
    List<SpecialResVo> selectSpecialReadFilm(SpecialSelectVo special);

    /**
     * 根据主键查询详情
     *
     * @param specialId 专题id
     * @return Special
     */
    SpecialResVo selectSpecialId(Long specialId);

    /**
     * 根据主键查询详情
     *
     * @param specialId 专题id
     * @return Special
     */
    Special selectSpecialById(Long specialId);

    /**
     * 查询专题信息
     *
     * @param special 专题信息
     * @return List<Special>
     */
    List<SpecialResVo> selectList(Special special);

    /**
     * 专题统计
     *
     * @param special 专题信息
     * @return List<Special>
     */
    List<SpecialStatisticsListVO> specialStatistics(SpecialStatisticsQueryVO special);

    /**
     * 添加专题信息
     *
     * @param special 专题
     * @return List
     */
    int insert(SpecialInsertVo special);

    /**
     * 更新专题信息
     *
     * @param special 专题
     * @return true||false
     */
    int update(SpecialUpdateVo special);

    /**
     * 删除专题表中信息
     *
     * @param req 专题
     * @return true||false
     */
    int updateDelFlag(SpecialDeleteVo req);

    /**
     * 更新专题状态
     *
     * @param special 专题
     * @return true||false
     */
    int updateStatus(SpecialStatusVo special);

    /**
     * 更新专题交付状态
     *
     * @param special 专题
     * @return true||false
     */
    int updateDeliveryStatus(Special special);
}
