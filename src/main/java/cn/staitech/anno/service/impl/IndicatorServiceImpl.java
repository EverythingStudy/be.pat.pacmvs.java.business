package cn.staitech.anno.service.impl;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.vo.indicator.IndicatorAndOrganizationIdVO;
import cn.staitech.anno.domain.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.domain.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListOutVO;
import cn.staitech.anno.mapper.IndicatorMapper;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangf
 */
@Slf4j
@Service
public class IndicatorServiceImpl implements IndicatorService {
    @Resource
    private IndicatorMapper indicatorMapper;
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
     * 查询List<Indicator>并进行格式化
     *
     * @param indicator
     * @return
     */
    private List<Indicator> getIndicatorList(Indicator indicator) {
        List<Indicator> list = indicatorMapper.selectIndicatorList(indicator);
        for (Indicator obj : list) {
            if (LanguageUtils.isEn()) {
                // 种属
                obj.setSpeciesName(MapConstant.getSpeciesNameEn(obj.getSpeciesId()));
                // 脏器
                obj.setOrganName(MapConstant.getOrganEn(obj.getSpeciesId().toString().concat(obj.getOrganId().toString())));
            } else {
                // 种属
                obj.setSpeciesName(MapConstant.getSpeciesName(obj.getSpeciesId()));
                // 脏器
                obj.setOrganName(MapConstant.getOrgan(obj.getSpeciesId().toString().concat(obj.getOrganId().toString())));
            }

            IndicatorAndOrganizationIdVO indicatorAndOrganizationIdVO = new IndicatorAndOrganizationIdVO();
            indicatorAndOrganizationIdVO.setIndicatorId(obj.getIndicatorId());
            // 查询总数
            obj.setAnnotationCategoryTotal(pathologicalIndicatorCategoryService.selectCategoryNumber(indicatorAndOrganizationIdVO));
        }
        return list;
    }

    /**
     * 展示病例指标
     *
     * @param indicator 查询的条件
     * @return 结果
     */
    @Override
    public PageMaster<Indicator> selectIndicatorList(Indicator indicator, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize).setReasonable(true);
        List<Indicator> list = getIndicatorList(indicator);
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
        List<Indicator> list = getIndicatorList(indicator);
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
     * 查询指标在项目表中的记录数量
     */
    @Override
    public Integer selectIndicatorCountInProject(Long indicatorId) {
        return indicatorMapper.selectIndicatorCountInProject(indicatorId);
    }

    /**
     * 查询指标列表
     *
     * @param
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicatorInformation(Indicator indicator) {
        List<Indicator> list = indicatorMapper.selectIndicatorInformation(indicator);
        for (Indicator obj : list) {
            if (LanguageUtils.isEn()) {
                obj.setIndicatorName(obj.getIndicatorNameEn());
            }
        }
        return list;
    }
}
