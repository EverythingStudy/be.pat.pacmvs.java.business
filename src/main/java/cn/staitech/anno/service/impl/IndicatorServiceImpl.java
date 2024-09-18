package cn.staitech.anno.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Species;
import cn.staitech.anno.mapper.IndicatorMapper;
import cn.staitech.anno.mapper.OrganMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.mapper.SpeciesMapper;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.indicator.IndicatorAddVO;
import cn.staitech.anno.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.vo.indicator.IndicatorVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
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
    private PathologicalIndicatorCategoryMapper categoryMapper;

    @Resource
    private OrganMapper organMapper;

    @Resource
    private SpeciesMapper speciesMapper;

	@Resource
	private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;

    
    @Resource
	private StructureService structureService;
    
    @Resource
	private OrganService organService;

    /**
     * 添加病例指标
     *
     * @param indicator 添加的字段信息
     * @return 结果
     */
    @Override
    public R<String> insertIndicator(IndicatorAddVO req) {
    	SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
    	Long organizationId = sysUser.getOrganizationId();
        //标签类型 0:下拉筛选标签；1:自定义标签
        Integer indicatorType = req.getIndicatorType();
        if (null == indicatorType) {
            indicatorType = 0;
        }
        Indicator indicator = new Indicator();
        indicator.setSpeciesId(req.getSpeciesId());
        indicator.setOrganId(req.getOrganId());
        indicator.setOrganizationId(organizationId);
        indicator.setDelFlag(0);
        // 查询结构指标是否存在
        List<Indicator> indicatorList = selectIndicator(indicator);
        if (!indicatorList.isEmpty()) {
            return R.fail(MessageSource.M("INDICATOR_EXIST"));
        }
        if (indicatorType == 1) {
            //校验脏器名称是否已经重复
            QueryWrapper<Organ> queryNameWrapper = new QueryWrapper<>();
            queryNameWrapper.eq("name", req.getOrganName());
            queryNameWrapper.eq("organization_id", organizationId);
            List<Organ> nameList = organMapper.selectList(queryNameWrapper);
            if (CollectionUtils.isNotEmpty(nameList)) {
                return R.fail(MessageSource.M("InsertOrganVO.NAME.EXIST"));
            }

            //校验脏器编码 是否已经重复
            QueryWrapper<Organ> queryOrganIdWrapper = new QueryWrapper<>();
            queryOrganIdWrapper.eq("organ_id", req.getOrganId());
            queryOrganIdWrapper.eq("organization_id", organizationId);

            List<Organ> organWrapperList = organMapper.selectList(queryOrganIdWrapper);
            if (CollectionUtils.isNotEmpty(organWrapperList)) {
                return R.fail(MessageSource.M("InsertOrganVO.ORGANID.EXIST"));
            }

            Organ organ = new Organ();
            organ.setName(req.getOrganName());
            organ.setNameEn(req.getOrganName());
            organ.setOrganId(req.getOrganId());
            organ.setSpeciesCode(req.getSpeciesId());
            organ.setOrganizationId(organizationId);

            //先查询是否有这个脏器
            QueryWrapper<Organ> queryOrganEditWrapper = new QueryWrapper<>();
            queryOrganEditWrapper.eq("organ_id", req.getOrganId());
            queryOrganEditWrapper.eq("name", req.getOrganName());
            queryOrganEditWrapper.eq("species_code", req.getSpeciesId());
            queryOrganEditWrapper.eq("organization_id", organizationId);
            List<Organ> organEditWrapperList = organMapper.selectList(queryOrganEditWrapper);
            if (CollectionUtils.isNotEmpty(organEditWrapperList)) {

            } else {
                organ.setNameEn(req.getOrganName());
                organMapper.insert(organ);
            }


            MapConstant.ORGAN_MAP = organService.selectMap();
            MapConstant.ORGAN_MAP_EN = organService.selectMapEn();
            MapConstant.STRUCTURE_MAP = structureService.selectMap();
            MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
        }

        if (indicatorType == 0) {
            indicator.setIndicatorName(MapConstant.getOrgan(organizationId + req.getSpeciesId() + req.getOrganId()));
            indicator.setIndicatorNameEn(MapConstant.getOrganEn(organizationId + req.getSpeciesId() + req.getOrganId()));
        } else {
            indicator.setIndicatorName(req.getOrganName());
            indicator.setIndicatorNameEn(req.getOrganName());
        }
        indicator.setNumber(indicator.getSpeciesId().concat(indicator.getOrganId()));
        indicator.setCreateBy(sysUser.getUserId());
        indicator.setOrganizationId(organizationId);
        indicator.setIndicatorType(indicatorType);
        //添加结构指标
        indicatorMapper.insertIndicator(indicator);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    
    
    
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
                obj.setSpeciesName(MapConstant.getSpeciesNameEn(obj.getOrganizationId().toString() + obj.getSpeciesId()));
                // 脏器
                obj.setOrganName(MapConstant.getOrganEn(obj.getOrganizationId().toString() + obj.getSpeciesId() + obj.getOrganId()));
            } else {
                // 种属 20231222wangfeng
                obj.setSpeciesName(MapConstant.getSpeciesName(obj.getOrganizationId().toString() + obj.getSpeciesId()));
                // 脏器
                obj.setOrganName(MapConstant.getOrgan(obj.getOrganizationId().toString() + obj.getSpeciesId() + obj.getOrganId()));
            }
            // 查询总数
            obj.setAnnotationCategoryTotal(categoryMapper.selectCategoryNumber(obj.getIndicatorId()));
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
	public List<PathologicalIndicatorCategory> speciesCategory(String species){
		Indicator indicator = new Indicator();
		indicator.setSpeciesId(species);
		indicator.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		List<Indicator> indicatorList = selectIndicator(indicator);
		LambdaQueryWrapper<PathologicalIndicatorCategory> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(PathologicalIndicatorCategory::getIndicatorId, indicatorList).eq(PathologicalIndicatorCategory::getDelFlag,0);
		return pathologicalIndicatorCategoryMapper.selectList(queryWrapper);

	}


    @Override
    public int saveCheck(IndicatorAddVO req,Long organizationId) {
        int checkTag = 0;
        //种属编号 重复校验
        QueryWrapper<Species> querySpeciesIdWrapper = new QueryWrapper<>();
        querySpeciesIdWrapper.eq("species_id", req.getSpeciesId());
        querySpeciesIdWrapper.eq("organization_id", organizationId);
        List<Species> speciesIdWrapperList = speciesMapper.selectList(querySpeciesIdWrapper);
        if (CollectionUtils.isNotEmpty(speciesIdWrapperList)) {
            checkTag = 1;
        }
        //种属名称 重复校验
        QueryWrapper<Species> queryNameWrapper = new QueryWrapper<>();
        queryNameWrapper.eq("name", req.getSpeciesName());
        queryNameWrapper.eq("organization_id", organizationId);
        List<Species> nameList = speciesMapper.selectList(queryNameWrapper);
        if (CollectionUtils.isNotEmpty(nameList)) {
            checkTag = 2;
        }
        //脏器编号 重复校验

        QueryWrapper<Organ> queryOrganIdWrapper = new QueryWrapper<>();
        queryOrganIdWrapper.eq("organ_id", req.getOrganId());
        queryOrganIdWrapper.eq("organization_id", organizationId);
        List<Organ> organWrapperList = organMapper.selectList(queryOrganIdWrapper);
        if (CollectionUtils.isNotEmpty(organWrapperList)) {
            checkTag = 3;
        }
        //脏器名称 重复校验
        QueryWrapper<Organ> queryOrganNameWrapper = new QueryWrapper<>();
        queryOrganNameWrapper.eq("name", req.getOrganName());
        queryOrganNameWrapper.eq("organization_id", organizationId);
        List<Organ> organNameList = organMapper.selectList(queryOrganNameWrapper);
        if (CollectionUtils.isNotEmpty(organNameList)) {
            checkTag = 4;
        }
        return checkTag;
    }

    @Override
    public Integer selectIndicatorCountByIndicator(Indicator indicator) {
        return indicatorMapper.selectIndicatorCountByIndicator(indicator);
    }

	@Override
	public R<IndicatorVO> getInfo(Long indicatorId) {
		// 获取病理信息
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Indicator indicatorQuery = new Indicator();
		indicatorQuery.setOrganizationId(organizationId);
		indicatorQuery.setIndicatorId(indicatorId);
		indicatorQuery.setDelFlag(0);
		List<Indicator> list = selectIndicator(indicatorQuery);
		IndicatorVO indicatorVo = new IndicatorVO();
		if (CollectionUtils.isNotEmpty(list)) {
			Indicator indicator = list.get(0);
			if (indicator != null) {
				// 浅拷贝
				BeanUtils.copyProperties(indicator, indicatorVo);
			}
			//添加关联项目
			//indicatorVo.setProjectVo(projectService.selectProjectInfo(indicatorId));
		}
		return R.ok(indicatorVo);
	}

	@Override
	public R<Integer> edit(IndicatorReviseVO req) {
		// 和项目绑定的不能修改
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Indicator indicatorQuery = new Indicator();
		indicatorQuery.setOrganizationId(organizationId);
		indicatorQuery.setIndicatorId(req.getIndicatorId().longValue());
		Integer num = selectIndicatorCountByIndicator(indicatorQuery);
		if (num > 0) {
			return R.fail(MessageSource.M("ALREADY_BOUND"));
		}

		//标签类型 0:下拉筛选标签；1:自定义标签
		Integer indicatorType = req.getIndicatorType();
		if (null == indicatorType) {
			indicatorType = 0;
		}

		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
		Indicator indicator = new Indicator();
		indicator.setSpeciesId(req.getSpeciesId());
		indicator.setOrganId(req.getOrganId());
		indicator.setOrganizationId(organizationId);
		indicator.setDelFlag(0);
		// 查询结构指标是否存在
		List<Indicator> indicatorList = selectIndicator(indicator);
		if (!indicatorList.isEmpty()) {
			boolean idCheck = true;
			for (Indicator indicatorP : indicatorList) {
				String organ_id = indicatorP.getOrganId();
				if (!organ_id.equals(req.getOrganId())) {
					idCheck = false;
					break;
				}
			}
			if (!idCheck) {
				return R.fail(MessageSource.M("InsertOrganVO.ORGANID.EXIST"));
			}
		}

		if (indicatorType == 1) {
			Organ organ = new Organ();
			organ.setName(req.getOrganName());
			organ.setOrganId(req.getOrganId());
			organ.setSpeciesCode(req.getSpeciesId());
			organ.setOrganizationId(organizationId);
			//先查询是否有这个脏器
			QueryWrapper<Organ> queryOrganEditWrapper = new QueryWrapper<>();
			queryOrganEditWrapper.eq("organ_id", req.getOrganId());
			queryOrganEditWrapper.eq("species_code", req.getSpeciesId());
			queryOrganEditWrapper.eq("organization_id", organizationId);
			List<Organ> organEditWrapperList = organMapper.selectList(queryOrganEditWrapper);
			if (CollectionUtils.isNotEmpty(organEditWrapperList)) {
				Organ o1 = organEditWrapperList.get(0);
				UpdateWrapper<Organ> updateWrapper = new UpdateWrapper();
				updateWrapper.eq("organ_id", o1.getOrganId());
				updateWrapper.eq("species_code", o1.getSpeciesCode());
				updateWrapper.eq("organization_id", organizationId);
				updateWrapper.set("name", req.getOrganName());
				updateWrapper.set("name_en", req.getOrganName());

				organMapper.update(null, updateWrapper);
			} else {
				organ.setNameEn(req.getOrganName());
				organMapper.insert(organ);
			}

			MapConstant.ORGAN_MAP = organService.selectMap();
			MapConstant.ORGAN_MAP_EN = organService.selectMapEn();
			MapConstant.STRUCTURE_MAP = structureService.selectMap();
			MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
		}

		if (indicatorType == 0) {
			indicator.setIndicatorName(MapConstant.getOrgan(organizationId + req.getSpeciesId() + req.getOrganId()));
			indicator.setIndicatorNameEn(MapConstant.getOrganEn(organizationId + req.getSpeciesId() + req.getOrganId()));
		} else {
			indicator.setIndicatorName(req.getOrganName());
			indicator.setIndicatorNameEn(req.getOrganName());
			req.setIndicatorName(req.getOrganName());
		}
		indicator.setNumber(indicator.getSpeciesId().concat(indicator.getOrganId()));
		indicator.setCreateBy(sysUser.getUserId());

		req.setNumber(indicator.getSpeciesId().concat(indicator.getOrganId()));
		// 修改病理指标
		updateIndicator(req);
		return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
	}

	@Override
	public R<String> delIndicator(IndicatorGetVO req) {
		if (!Optional.ofNullable(req.getIndicatorId()).isPresent()) {
			return R.fail(MessageSource.M("INDICATOR_ID_NOTNULL"));
		}
		Indicator indicatorOld = selectIndicatorsById(req.getIndicatorId().longValue());
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Indicator indicatorQuery = new Indicator();
		indicatorQuery.setOrganizationId(organizationId);
		indicatorQuery.setSpeciesId(indicatorOld.getSpeciesId());
		Integer num = selectIndicatorCountByIndicator(indicatorQuery);
		if (num > 0) {
			return R.fail(MessageSource.M("ALREADY_BOUND_NO_DEL"));
		}
		//删除标注类别
		PathologicalIndicatorCategory Pathological = PathologicalIndicatorCategory.builder().indicatorId(req.getIndicatorId().longValue()).organizationId(organizationId).delFlag(1).build();
		categoryMapper.updateByPrimaryKeySelective(Pathological);
		//标签类型 0:下拉筛选标签；1:自定义标签
		if(null != indicatorOld && indicatorOld.getIndicatorType() == 1){
			//脏器id
			String organId = indicatorOld.getOrganId();
			//机构id
			Long baseOrganizationId = indicatorOld.getOrganizationId();
			//种属
			String speciesCode = indicatorOld.getSpeciesId();
			//删除脏器
			QueryWrapper<Organ> removeQueryWrapper = new QueryWrapper<>();
			removeQueryWrapper.eq("organ_id", organId).eq("organization_id", baseOrganizationId).eq("species_code", speciesCode);
			organService.remove(removeQueryWrapper);
		}
		//删除病理指标
		delIndicator(req.getIndicatorId().longValue());
		MapConstant.ORGAN_MAP = organService.selectMap();
		MapConstant.ORGAN_MAP_EN = organService.selectMapEn();
		MapConstant.STRUCTURE_MAP = structureService.selectMap();
		MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
		return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
	}
}
