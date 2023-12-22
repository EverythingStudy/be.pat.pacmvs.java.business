package cn.staitech.anno.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.domain.Species;
import cn.staitech.anno.mapper.IndicatorMapper;
import cn.staitech.anno.mapper.OrganMapper;
import cn.staitech.anno.mapper.SpeciesMapper;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.indicator.IndicatorAddVO;
import cn.staitech.anno.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.vo.statistic.StatisticIndicatorListInVO;
import cn.staitech.anno.vo.statistic.StatisticIndicatorListOutVO;
import cn.staitech.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

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
    
    @Resource
    private OrganMapper organMapper;
	
	@Resource
	private SpeciesMapper speciesMapper;

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
                // 种属 20231222wangfeng
                obj.setSpeciesName(MapConstant.getSpeciesNameEn(obj.getOrganizationId() + obj.getSpeciesId()));
                // 脏器
                obj.setOrganName(MapConstant.getOrganEn(obj.getOrganizationId() + obj.getSpeciesId() + obj.getOrganId()));
            } else {
                // 种属 20231222wangfeng
                obj.setSpeciesName(MapConstant.getSpeciesName(obj.getOrganizationId() + obj.getSpeciesId()));
                // 脏器
                obj.setOrganName(MapConstant.getOrgan(obj.getOrganizationId() + obj.getSpeciesId() + obj.getOrganId()));
            }
            // 查询总数
            obj.setAnnotationCategoryTotal(pathologicalIndicatorCategoryService.selectCategoryNumber(obj.getIndicatorId()));
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

	@Override
	public int saveCheck(IndicatorAddVO req) {
		int checkTag = 0;
		//种属编号 重复校验
		QueryWrapper<Species> querySpeciesIdWrapper = new QueryWrapper<>();
		querySpeciesIdWrapper.eq("species_id", req.getSpeciesId());
		List<Species> speciesIdWrapperList = speciesMapper.selectList(querySpeciesIdWrapper);
		if(CollectionUtils.isNotEmpty(speciesIdWrapperList)){
			 checkTag = 1;
		}
		//种属名称 重复校验
		QueryWrapper<Species> queryNameWrapper = new QueryWrapper<>();
		queryNameWrapper.eq("name", req.getSpeciesName());
		List<Species> nameList = speciesMapper.selectList(queryNameWrapper);
		if(CollectionUtils.isNotEmpty(nameList)){
			 checkTag = 2;
		}
		//脏器编号 重复校验

		QueryWrapper<Organ> queryOrganIdWrapper = new QueryWrapper<>();
		queryOrganIdWrapper.eq("organ_id", req.getOrganId());
		List<Organ> organWrapperList = organMapper.selectList(queryOrganIdWrapper);
		if(CollectionUtils.isNotEmpty(organWrapperList)){
			 checkTag = 3;
		}
		//脏器名称 重复校验
		QueryWrapper<Organ> queryOrganNameWrapper = new QueryWrapper<>();
		queryOrganNameWrapper.eq("name", req.getOrganName());
		List<Organ> organNameList = organMapper.selectList(queryOrganNameWrapper);
		if(CollectionUtils.isNotEmpty(organNameList)){
			 checkTag = 4;
		}
		return checkTag;
	}
}
