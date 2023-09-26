package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.domain.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListOutVO;
import cn.staitech.anno.mapper.IndicatorMapper;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.SpeciesService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IndicatorServicelmpl implements IndicatorService {

    @Resource
    private IndicatorMapper indicatorMapper;

    @Resource
    private SpeciesService speciesService;

    @Resource
    private OrganService organService;

    @Resource
    private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;

    /**
     * 添加病例指标
     *
     * @param indicator 添加的字段信息
     * @return 结果
     */
    @Override
    public int insertIndicator(Indicator indicator) {
        return indicatorMapper.insertIndicator(indicator);
    }

    /**
     * 展示病例指标
     *
     * @param indicator 查询的条件
     * @return 结果
     */
    @Override
    public PageMaster<Indicator> selectIndicatorList(Indicator indicator, Integer pageNum, Integer pageSize) {
        // 种属
        Map<Long, String> sepeciesMap = speciesService.selectMap();
        // 脏器
        Map<String, String> organMap = organService.selectMap();

        indicator.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());


        PageHelper.startPage(pageNum, pageSize).setReasonable(true);

        List<Indicator> list = indicatorMapper.selectIndicatorList(indicator);

        for (Indicator obj : list) {
            if (sepeciesMap.containsKey(obj.getSpeciesId())) {
                obj.setSpeciesName(sepeciesMap.get(obj.getSpeciesId()));
            }
            if (organMap.containsKey(obj.getOrganId())) {
                obj.setOrganName(organMap.get(obj.getOrganId()));
            }

            // 查询总数
            obj.setAnnotationCategoryTotal(pathologicalIndicatorCategoryService.selectCategoryNumber(obj.getIndicatorId()));


        }
        PageMaster<Indicator> pageMaster = new PageMaster<>(list);
        return pageMaster;
    }


    /**
     * 展示病例指标
     *
     * @param indicator 查询的条件
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicatorList1(Indicator indicator) {
        // 种属
        Map<Long, String> sepeciesMap = speciesService.selectMap();
        // 脏器
        Map<String, String> organMap = organService.selectMap();

        indicator.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());

        List<Indicator> list = indicatorMapper.selectIndicatorList(indicator);

        for (Indicator obj : list) {
            if (sepeciesMap.containsKey(obj.getSpeciesId())) {
                obj.setSpeciesName(sepeciesMap.get(obj.getSpeciesId()));
            }
            if (organMap.containsKey(obj.getOrganId())) {
                obj.setOrganName(organMap.get(obj.getOrganId()));
            }

            // 查询总数
            obj.setAnnotationCategoryTotal(pathologicalIndicatorCategoryService.selectCategoryNumber(obj.getIndicatorId()));

        }

        return list;
    }

    /**
     * 展示病例指标详情
     *
     * @param indicatorId 病例指标id
     * @return 结果
     */
    @Override
    public Indicator selectIndicatorsById(Long indicatorId) {
        return indicatorMapper.selectIndicatorById(indicatorId);
    }

    /**
     * 修改
     *
     * @param indicator 病例指标id
     * @return 结果
     */
    @Override
    public int updateIndicator(IndicatorReviseVO indicator) {
        return indicatorMapper.updateIndicator(indicator);
    }

    /**
     * 删除
     *
     * @param indicatorId 指标id
     * @return 结果
     */
    @Override
    public int delIndicator(Long indicatorId) {
        return indicatorMapper.delIndicator(indicatorId);
    }

    /**
     * 展示指定的统计病例指标列表
     *
     * @param projectIdList 项目ID数组
     * @return 结果
     */
    @Override
    public List<StatisticIndicatorListOutVO> selectIndicatorStatisticList(StatisticIndicatorListInVO projectIdList) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            projectIdList.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        return indicatorMapper.selectIndicatorStatisticList(projectIdList);
    }


    /**
     * 查询指标列表
     *
     * @param indicator
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicator(Indicator indicator) {
        return indicatorMapper.selectIndicator(indicator);
    }

    /**
     * 根据病理id和名字查询信息
     *
     * @param indicator
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicatorName(IndicatorGetVO indicator) {
        return indicatorMapper.selectIndicatorName(indicator);
    }

    /**
     * 查询所有的病理数量
     */
    @Override
    public Integer selectIndicatorNum() {
        return indicatorMapper.selectIndicatorNum();
    }

    /**
     * 查询专题数量
     */
    @Override
    public Integer selectSpecial(Long indicatorId) {
        return indicatorMapper.selectSpecial(indicatorId);
    }


    // 2.0 新修改====================================

    /**
     * 查询指标列表
     *
     * @param
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicatorInformation(Indicator indicator) {
        return indicatorMapper.selectIndicatorInformation(indicator);
    }
}
