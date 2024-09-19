package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Structure;
import cn.staitech.anno.mapper.IndicatorMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.mapper.StructureMapper;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.anno.service.remote.AnnoService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.annotation.CategoryStatisticsIn;
import cn.staitech.anno.vo.annotation.CategoryVO;
import cn.staitech.anno.vo.annotation.LabelListVO;
import cn.staitech.anno.vo.annotation.LabelVO;
import cn.staitech.anno.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.vo.indicator.PathologicalIndicatorCategoryOutVO;
import cn.staitech.anno.vo.indicator.PathologicalIndicatorCategoryVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PathologicalIndicatorCategoryServiceImpl implements PathologicalIndicatorCategoryService {
	@Resource
	private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;
	@Resource
	private StructureService structureService;
	@Resource
	private IndicatorMapper indicatorMapper;
	@Resource
	private StructureMapper structureMapper;
	@Resource
	private OrganService organService;
	@Resource
	private IndicatorService indicatorService;

	@Resource
	private AnnoService annoService;

	/**
	 * 添加标签
	 */
	@Override
	public int insertSelective(PathologicalIndicatorCategory pathologicalIndicatorCategory) {
		return pathologicalIndicatorCategoryMapper.insertSelective(pathologicalIndicatorCategory);
	}


	/**
	 * 修改标签
	 */
	@Override
	public String updateByPrimaryKeySelective(PathologicalIndicatorCategory indicator) {
		if (pathologicalIndicatorCategoryMapper.updateByPrimaryKeySelective(indicator) > 0) {
			return MessageSource.M("UPDATE_SUCCESS_1");
		} else {
			return MessageSource.M("NOT_THIS_INDICATOR");
		}
	}

	/**
	 * 删除标签
	 */
	@Override
	public String deleteByPrimaryKey(Long categoryId) {
		// 查询标签是否存在
		if (pathologicalIndicatorCategoryMapper.deleteByPrimaryKey(categoryId) > 0) {
			return MessageSource.M("DELETE_SUCCESS");
		} else {
			return MessageSource.M("NOT_THIS_INDICATOR");
		}
	}

	/**
	 * 查询标签详细信息
	 */
	@Override
	public PathologicalIndicatorCategory selectByPrimaryKey(Long categoryId) {
		return pathologicalIndicatorCategoryMapper.selectByPrimaryKey(categoryId);
	}

	/**
	 * 根据病理指标id获取全部信息
	 *
	 * @param indicatorId 病理指标ID
	 * @return 标签信息
	 */
	@Override
	public List<PathologicalIndicatorCategory> selectIndicatorIdAll(Long indicatorId) {
		return pathologicalIndicatorCategoryMapper.selectIndicatorIdAll(indicatorId);
	}

	/**
	 * 根据病理指标id删除信息
	 *
	 * @param indicatorId 病理指标ID
	 * @return 标签信息
	 */
	@Override
	public int delIndicatorCategory(Long indicatorId) {
		return pathologicalIndicatorCategoryMapper.delIndicatorCategory(indicatorId);
	}

	@Override
	public PathologicalIndicatorCategory selectCategoryAll(Long CategoryId) {
		return pathologicalIndicatorCategoryMapper.selectCategoryAll(CategoryId);
	}


	/**
	 * 条件查询标注类别
	 *
	 * @param Pathological 病例指标id 或 颜色 或 标注类别名称
	 * @return 结果
	 */
	@Override
	public List<PathologicalIndicatorCategory> selectIndicatorMessage(PathologicalIndicatorCategory Pathological) {
		return pathologicalIndicatorCategoryMapper.selectIndicatorMessage(Pathological);
	}


	/**
	 * 条件查询标注类别
	 *
	 * @param Pathological 病例指标id 或 颜色 或 标注类别名称
	 * @return 结果
	 */
	@Override
	public List<PathologicalIndicatorCategory> selectIndicatorMessageForUpdate(PathologicalIndicatorCategory Pathological) {
		return pathologicalIndicatorCategoryMapper.selectIndicatorMessageForUpdate(Pathological);
	}




	/**
	 * 查询病例指标下的标注类别数量
	 */
	@Override
	public Long selectCategoryNumber(Long indicatorId) {
		return pathologicalIndicatorCategoryMapper.selectCategoryNumber(indicatorId);
	}

	/**
	 * 根据projectId查询标注类别
	 */
	@Override
	public List<PathologicalIndicatorCategory> selectProjectCategory(Long projectId) {
		return pathologicalIndicatorCategoryMapper.selectProjectCategory(projectId);
	}

	/**
	 * 根据indicatorId查询标注类别（不包含unLabel）
	 */
	@Override
	public List<LabelListVO> selectByIndicator(LabelVO labelVO) {
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		labelVO.setOrganizationId(organizationId);
		List<LabelListVO> list = pathologicalIndicatorCategoryMapper.selectByIndicator(labelVO);
		for (LabelListVO listVO : list) {
			if (LanguageUtils.isEn()) {
				listVO.setStructureName(listVO.getNameEn());
			} else {
				listVO.setStructureName(listVO.getName());
			}
		}
		return list;
	}


	private Structure getStructure(String organId, String speciesId, Long organizationId, String structureId) {
		Structure retStructure = new Structure();
		Structure structure = new Structure();
		structure.setSpeciesId(speciesId);
		structure.setOrganId(organId);
		structure.setOrganizationId(organizationId);
		structure.setStructureId(structureId);
		List<Structure> list = structureMapper.selectList(structure);
		if (CollectionUtils.isNotEmpty(list)) {
			retStructure = list.get(0);
		}
		return retStructure;
	}


	@Override
	public Integer selectLabelNumByStructureId(String structureId) {
		return pathologicalIndicatorCategoryMapper.selectLabelNumByStructureId(structureId);
	}

	@Override
	public void handlerCouponsUserStatusTimeOutToExpired(List<Long> dataList) throws ParseException {
		QueryWrapper<PathologicalIndicatorCategory> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("del_flag", "0");
		if (CollectionUtils.isNotEmpty(dataList)) {
			queryWrapper.in("category_id", dataList);
		}
		queryWrapper.isNull("category_code");
		//structure_id 为空
		queryWrapper.isNotNull("structure_id");
		queryWrapper.orderByAsc("indicator_id", "structure_id");
		List<PathologicalIndicatorCategory> list = pathologicalIndicatorCategoryMapper.selectList(queryWrapper);
		if (CollectionUtils.isNotEmpty(list)) {
			//遍历所有数据，先按照structureId分组
			Map<String, Map<Integer, Long>> resultMap = new HashMap<>(16);
			for (PathologicalIndicatorCategory category : list) {
				Long categoryId = category.getCategoryId();
				Long indicatorId = category.getIndicatorId();
				String structureId = category.getStructureId();
				String structureFather = "";
				//type 1:结构指标 2：考试 3：标注
				Integer type = -1;
				if (structureId.contains("ROA") || structureId.contains("ROE")) {
					if (StringUtils.isNotEmpty(structureId) && structureId.length() > 6) {
						structureFather = structureId.substring(0, 6);
						if (structureId.contains("ROE")) {
							//ROE:考核区域
							type = 2;
						} else if (structureId.contains("ROA")) {
							//ROA:标注区域
							type = 3;
						}
					}
				} else {
					//结构指标
					structureFather = structureId;
					type = 1;
				}
				if (StringUtils.isNotEmpty(structureFather)) {
					String structureKey = indicatorId + "_" + structureFather;
					//存入resultMap
					if (resultMap.isEmpty()) {
						Map<Integer, Long> parmMap = new HashMap<>(16);
						parmMap.put(type, categoryId);
						resultMap.put(structureKey, parmMap);
					} else {
						//判断是否包括key
						if (resultMap.containsKey(structureKey)) {
							//取出原来的数据
							Map<Integer, Long> sourceMap = resultMap.get(structureKey);
							sourceMap.put(type, categoryId);
							resultMap.put(structureKey, sourceMap);
						} else {
							//直接存
							Map<Integer, Long> parmMap = new HashMap<>(16);
							parmMap.put(type, categoryId);
							resultMap.put(structureKey, parmMap);
						}
					}
				}
			}
			//打印下处理的数据
			for (Map.Entry<String, Map<Integer, Long>> entry : resultMap.entrySet()) {
				// 结构指标key
				String isKey = entry.getKey();
				Map<Integer, Long> parmValue = entry.getValue();
				//处理数据
				if (isKey.split("_").length == 2) {
					String structureId = isKey.split("_")[1];
					//type 1:结构指标 2：考试 3：标注
					if (parmValue.containsKey(1)) {
						PathologicalIndicatorCategory picVo = pathologicalIndicatorCategoryMapper.selectById(parmValue.get(1));
						Indicator indicator = indicatorMapper.selectIndicatorById(picVo.getIndicatorId());
						if (null != indicator) {
							//统一添加cageoryCode
							Snowflake snowflake = new Snowflake();
							String categoryCode = snowflake.nextIdStr();

							PathologicalIndicatorCategory category2 = new PathologicalIndicatorCategory();
							category2.setCategoryCode(categoryCode);
							category2.setCategoryId(parmValue.get(1));
							category2.setGroupNumber(CommonConstant.STRUCTURE_RO_GROUP_NUMBER);
							pathologicalIndicatorCategoryMapper.updateById(category2);
							//其他两个请参考结构指标
							//其他两个有则修改，没有加添加
							String structureROEId = structureId + CommonConstant.STRUCTURE_ROE;
							if (parmValue.containsKey(2)) {
								//考试修改
								PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
								BeanUtils.copyProperties(picVo, picExamVo2);
								String structureName = "";
								Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROEId);
								if (structure != null) {
									structureName = structure.getName();
								}
								picExamVo2.setCategoryId(parmValue.get(2));
								picExamVo2.setCategoryName(indicator.getIndicatorName() + structureName);
								picExamVo2.setStructureId(structureROEId);
								picExamVo2.setNumber(structureROEId);
								picExamVo2.setCategoryCode(categoryCode);
								picExamVo2.setGroupNumber(CommonConstant.STRUCTURE_ROE_GROUP_NUMBER);
								//修改考试
								pathologicalIndicatorCategoryMapper.updateById(picExamVo2);
							} else {
								//先确认structure表是否存在structureROEId
								List<Structure> roeList = structureService.getListByStructureId(structureROEId);
								if (CollectionUtils.isNotEmpty(roeList)) {
									//考试添加
									PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
									BeanUtils.copyProperties(picVo, picExamVo2);
									String structureName = "";
									// 获取structureName
									Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROEId);
									if (structure != null) {
										structureName = structure.getName();
									}
									picExamVo2.setCategoryName(indicator.getIndicatorName() + structureName);
									picExamVo2.setStructureId(structureROEId);
									picExamVo2.setNumber(structureROEId);
//									picExamVo2.setCategoryId(null);
									picExamVo2.setCategoryCode(categoryCode);
									picExamVo2.setGroupNumber(CommonConstant.STRUCTURE_ROE_GROUP_NUMBER);
									//add考试
									pathologicalIndicatorCategoryMapper.insertSelective(picExamVo2);
								}
							}

							//type 1:结构指标 2：考试 3：标注
							String structureROAId = structureId + CommonConstant.STRUCTURE_ROA;
							if (parmValue.containsKey(3)) {
								//标注修改
								PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
								BeanUtils.copyProperties(picVo, picExamVo2);
								String structureName = "";
								// 获取structureName
								Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROAId);
								if (structure != null) {
									structureName = structure.getName();
								}
								picExamVo2.setCategoryId(parmValue.get(3));
								picExamVo2.setCategoryName(indicator.getIndicatorName() + structureName);
								picExamVo2.setStructureId(structureROAId);
								picExamVo2.setNumber(structureROAId);
								picExamVo2.setCategoryCode(categoryCode);
								picExamVo2.setGroupNumber(CommonConstant.STRUCTURE_ROA_GROUP_NUMBER);
								//修改标注
								pathologicalIndicatorCategoryMapper.updateById(picExamVo2);
							} else {
								//先确认structure表是否存在structureROEId
								List<Structure> roaList = structureService.getListByStructureId(structureROAId);
								if (CollectionUtils.isNotEmpty(roaList)) {
									//标注添加
									PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
									BeanUtils.copyProperties(picVo, picExamVo2);
									String structureName = "";
									// 获取structureName
									Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROAId);
									if (structure != null) {
										structureName = structure.getName();
									}
									picExamVo2.setCategoryName(indicator.getIndicatorName() + structureName);
									picExamVo2.setStructureId(structureROAId);
									picExamVo2.setNumber(structureROAId);
//									picExamVo2.setCategoryId(null);
									picExamVo2.setCategoryCode(categoryCode);
									picExamVo2.setGroupNumber(CommonConstant.STRUCTURE_ROA_GROUP_NUMBER);

									//add标注
									pathologicalIndicatorCategoryMapper.insertSelective(picExamVo2);
								}
							}
						}
					} else {
						//type 1:结构指标 2：考试 3：标注
						if (parmValue.containsKey(3)) {
							//统一添加cageoryCode
							Snowflake snowflake = new Snowflake();
							String categoryCode = snowflake.nextIdStr();

							PathologicalIndicatorCategory category2 = new PathologicalIndicatorCategory();
							category2.setCategoryCode(categoryCode);
							category2.setCategoryId(parmValue.get(3));
							category2.setGroupNumber(CommonConstant.STRUCTURE_ROA_GROUP_NUMBER);
							pathologicalIndicatorCategoryMapper.updateById(category2);

							//其他两个请标注（结构指标+考试）
							//结构指标肯定是添加
							PathologicalIndicatorCategory picVo = pathologicalIndicatorCategoryMapper.selectById(parmValue.get(3));
							Indicator indicator = indicatorMapper.selectIndicatorById(picVo.getIndicatorId());
							if (null != indicator) {
								PathologicalIndicatorCategory picJGVo = new PathologicalIndicatorCategory();
								BeanUtils.copyProperties(picVo, picJGVo);
//								picJGVo.setCategoryId(null);
								picJGVo.setStructureId(structureId);
								String structureName = "";
								// 获取structureName
								Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureId);
								if (structure != null) {
									structureName = structure.getName();
								}
								picJGVo.setCategoryName(indicator.getIndicatorName() + structureName);
								picJGVo.setNumber(structureId);
//								picJGVo.setCategoryId(null);
								picJGVo.setCategoryCode(categoryCode);
								picJGVo.setGroupNumber(CommonConstant.STRUCTURE_RO_GROUP_NUMBER);
								pathologicalIndicatorCategoryMapper.insert(picJGVo);

								String structureROEId = structureId + CommonConstant.STRUCTURE_ROE;
								//考试，有就修改，没有就添加
								if (parmValue.containsKey(2)) {
									//考试修改
									PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
									BeanUtils.copyProperties(picVo, picExamVo2);
									String structureNameROE = "";
									// 获取structureName
									Structure structure2 = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROEId);
									if (structure2 != null) {
										structureNameROE = structure2.getName();
									}
									picExamVo2.setCategoryId(parmValue.get(2));
									picExamVo2.setCategoryName(indicator.getIndicatorName() + structureNameROE);
									picExamVo2.setStructureId(structureROEId);
									picExamVo2.setNumber(structureROEId);
									picExamVo2.setCategoryCode(categoryCode);
									picExamVo2.setGroupNumber(CommonConstant.STRUCTURE_ROE_GROUP_NUMBER);

									//修改考试
									pathologicalIndicatorCategoryMapper.updateById(picExamVo2);
								} else {
									//考试添加
									//先确认structure表是否存在structureROEId
									List<Structure> roeList = structureService.getListByStructureId(structureROEId);
									if (CollectionUtils.isNotEmpty(roeList)) {
										PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
										BeanUtils.copyProperties(picVo, picExamVo2);
										String structureNameROE = "";
										// 获取structureName
										Structure structure2 = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROEId);
										if (structure2 != null) {
											structureNameROE = structure2.getName();
										}
										picExamVo2.setCategoryName(indicator.getIndicatorName() + structureNameROE);
										picExamVo2.setStructureId(structureROEId);
										picExamVo2.setNumber(structureROEId);
//										picExamVo2.setCategoryId(null);
										picExamVo2.setCategoryCode(categoryCode);
										picExamVo2.setGroupNumber(CommonConstant.STRUCTURE_ROE_GROUP_NUMBER);
										//add考试
										pathologicalIndicatorCategoryMapper.insertSelective(picExamVo2);
									}
								}
							}
						} else {
							//统一添加cageoryCode
							Snowflake snowflake = new Snowflake();
							String categoryCode = snowflake.nextIdStr();

							PathologicalIndicatorCategory category2 = new PathologicalIndicatorCategory();
							category2.setCategoryCode(categoryCode);
							category2.setCategoryId(parmValue.get(2));
							category2.setGroupNumber(CommonConstant.STRUCTURE_ROE_GROUP_NUMBER);
							pathologicalIndicatorCategoryMapper.updateById(category2);

							//type 1:结构指标 2：考试 3：标注
							//有考试 ==》其他两个请标注（结构指标+标注）
							//结构指标肯定是添加
							PathologicalIndicatorCategory picVo = pathologicalIndicatorCategoryMapper.selectById(parmValue.get(2));
							Indicator indicator = indicatorMapper.selectIndicatorById(picVo.getIndicatorId());
							if (null != indicator) {
								PathologicalIndicatorCategory picJGVo = new PathologicalIndicatorCategory();
								BeanUtils.copyProperties(picVo, picJGVo);
//								picJGVo.setCategoryId(null);
								picJGVo.setStructureId(structureId);
//								picJGVo.setCategoryId(null);
								picJGVo.setCategoryCode(categoryCode);

								String structureName = "";
								// 获取structureName
								Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureId);
								if (structure != null) {
									structureName = structure.getName();
								}
								picJGVo.setCategoryName(indicator.getIndicatorName() + structureName);
								picJGVo.setNumber(structureId);
								picJGVo.setGroupNumber(CommonConstant.STRUCTURE_RO_GROUP_NUMBER);
								pathologicalIndicatorCategoryMapper.insert(picJGVo);
								//标注先确定是否存在这个标签，如果有就添加
								String structureRoaId = structureId + CommonConstant.STRUCTURE_ROA;
								List<Structure> roaList = structureService.getListByStructureId(structureRoaId);
								if (CollectionUtils.isNotEmpty(roaList)) {
									PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
									BeanUtils.copyProperties(picVo, picExamVo2);
									String structureNameROA = "";
									// 获取structureName
									Structure structure2 = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureRoaId);
									if (structure2 != null) {
										structureNameROA = structure2.getName();
									}
									picExamVo2.setCategoryName(indicator.getIndicatorName() + structureNameROA);
									picExamVo2.setStructureId(structureRoaId);
									picExamVo2.setNumber(structureRoaId);
//									picExamVo2.setCategoryId(null);
									picExamVo2.setCategoryCode(categoryCode);
									picExamVo2.setGroupNumber(CommonConstant.STRUCTURE_ROA_GROUP_NUMBER);
									//add标注
									pathologicalIndicatorCategoryMapper.insertSelective(picExamVo2);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String updateByPrimaryKeySelective2(PathologicalIndicatorCategory indicator) {
		if (pathologicalIndicatorCategoryMapper.updateByPrimaryKeySelective(indicator) > 0) {
			return "1";
		} else {
			return "0";
		}
	}


	@Override
	public R<String> add(PathologicalIndicatorCategoryVO vo) {
		//标签类型 0:下拉筛选标签；1:自定义标签
		Integer categoryType = vo.getCategoryType();
		if (null == categoryType) {
			categoryType = 0;
		}
		String rgb = vo.getRgb();
		String hex = vo.getHex();
		Long indicatorId = vo.getIndicatorId();
		Integer orderNumber = vo.getOrderNumber();
		String structureId = vo.getStructureId();

		// 查询Indicator信息
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getLoginUser().getSysUser().getUserId();
		Indicator indicatorQuery = new Indicator();
		indicatorQuery.setIndicatorId(indicatorId);
		indicatorQuery.setOrganizationId(organizationId);
		indicatorQuery.setDelFlag(0);
		// 查询结构指标是否存在
		Indicator indicator = new Indicator();
		List<Indicator> indicatorList = indicatorService.selectIndicator(indicatorQuery);
		if (CollectionUtils.isNotEmpty(indicatorList)) {
			indicator = indicatorList.get(0);
		}
		if (indicator == null) {
			return R.fail(MessageSource.M("INDICATOR_ABSENT"));
		}

		//验证结构是否已经存在
		PathologicalIndicatorCategory categoryS = new PathologicalIndicatorCategory();
		categoryS.setIndicatorId(indicatorId);
		categoryS.setStructureId(structureId);
		categoryS.setOrganizationId(organizationId);
		List<PathologicalIndicatorCategory> listS = selectIndicatorMessage(categoryS);
		if (listS.size() > 0) {
			return R.fail(MessageSource.M("CATEGORY_CODE_CHECK_EXIST"));
		}
		PathologicalIndicatorCategory categoryHex = new PathologicalIndicatorCategory();
		categoryHex.setHex(hex);
		categoryHex.setIndicatorId(indicatorId);
		categoryHex.setOrganizationId(organizationId);
		// 验证颜色值是否已经存在
		List<PathologicalIndicatorCategory> listR = selectIndicatorMessage(categoryHex);
		if (listR.size() > 0) {
			return R.fail(MessageSource.M("COLOR_NAME_CHECK_EXIST"));
		}
		
		PathologicalIndicatorCategory categoryStructureName = new PathologicalIndicatorCategory();
		String oldStructureName = getStructureName(indicator, vo.getStructureId());
		// 生成categoryName
		String categoryNameStr = indicator.getIndicatorName() + oldStructureName;
		categoryStructureName.setCategoryName(categoryNameStr);
		categoryStructureName.setIndicatorId(indicatorId);
		categoryStructureName.setOrganizationId(organizationId);
		// 验证组织名称是否已经存在
		List<PathologicalIndicatorCategory> listN = selectIndicatorMessage(categoryStructureName);
		if (listN.size() > 0) {
			return R.fail(MessageSource.M("CATEGORY_NAME_CHECK_EXIST"));
		}

		if (categoryType == 1) {
			String structureName = vo.getStructureName();
			//校验structureId、structureName 是否已经存在
			String speciesId = indicator.getSpeciesId();
			String organId = indicator.getOrganId();

			QueryWrapper<Structure> queryExitsWrapper = new QueryWrapper<>();
			queryExitsWrapper.eq("structure_id", structureId);
			queryExitsWrapper.eq("organization_id", organizationId);
			List<Map<String, Object>> stList = structureService.listMaps(queryExitsWrapper);
			if (CollectionUtils.isNotEmpty(stList)) {
				return R.fail(MessageSource.M("CATEGORY_CODE_CHECK_EXIST"));
			}


			QueryWrapper<Structure> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("species_id", speciesId);
			queryWrapper.eq("organ_id", organId);
			queryWrapper.eq("organization_id", organizationId);
			queryWrapper.eq("structure_id", structureId);
			List<Map<String, Object>> sIdList = structureService.listMaps(queryWrapper);
			if (CollectionUtils.isNotEmpty(sIdList)) {
				return R.fail(MessageSource.M("CATEGORY_CODE_CHECK_EXIST"));
			}
			
			QueryWrapper<Structure> queryWrapperName = new QueryWrapper<>();
			queryWrapperName.eq("name", structureName);
			queryWrapperName.eq("species_id", speciesId);
			queryWrapperName.eq("organ_id", organId);
			queryWrapperName.eq("organization_id", organizationId);
			List<Map<String, Object>> sNameList = structureService.listMaps(queryWrapperName);
			if (CollectionUtils.isNotEmpty(sNameList)) {
				return R.fail(MessageSource.M("CATEGORY_NAME_CHECK_EXIST"));
			}
			//structure表保存结构信息
			String structureIdNew = structureId;
			String structureNameNew = structureName;
			String structureNameEnNew = structureName;
			String type = CommonConstant.STRUCTURE_RO;

			Structure structure = new Structure();
			structure.setStructureId(structureIdNew);
			structure.setName(structureNameNew);
			structure.setNameEn(structureNameEnNew);
			structure.setSpeciesId(speciesId);
			structure.setOrganId(organId);
			structure.setType(type);
			structure.setOrganizationId(organizationId);
			//保存结构
			structureService.save(structure);

			MapConstant.ORGAN_MAP = organService.selectMap();
			MapConstant.ORGAN_MAP_EN = organService.selectMapEn();
			MapConstant.STRUCTURE_MAP = structureService.selectMap();
			MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
		}


		Date currentDate = DateUtil.date();
		Snowflake snowflake = new Snowflake();
		String categoryCode = snowflake.nextIdStr();

		String currentStructureId = vo.getStructureId();
		PathologicalIndicatorCategory category = new PathologicalIndicatorCategory();
		category.setStructureId(currentStructureId);
		category.setRgb(rgb);
		category.setHex(hex);
		category.setIndicatorId(indicatorId);
		category.setOrderNumber(orderNumber);

		String structureName = getStructureName(indicator, currentStructureId);
		// 生成categoryName
		String categoryName = indicator.getIndicatorName() + structureName;
		category.setCategoryName(categoryName);
		// 生成完整编码
		category.setNumber(currentStructureId);
		category.setCreateBy(currentUserId);
		category.setOrganizationId(organizationId);
		category.setCreateTime(currentDate);
		category.setCategoryCode(categoryCode);
		category.setGroupNumber(CommonConstant.STRUCTURE_RO_GROUP_NUMBER);
		category.setCategoryType(categoryType);
		insertSelective(category);
		//		IndicatorReviseVO indicatorReviseVO = IndicatorReviseVO.builder().indicatorId(indicatorId.intValue()).build();
		// 更新病理表数据
		//		indicatorService.updateIndicator(indicatorReviseVO);
		return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
	}
	
	
	public String getStructureName(Indicator indicator,String structureId) {
		String structureName = "";
		// 获取structureName
		Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureId);
		if (structure != null) {
			structureName = structure.getName();
		}
		return structureName;
	}


	@Override
	public R<String> edit(PathologicalIndicatorCategory category) {
		//1、根据传入的categoryId查询其他两种类型categoryId 先校验
		CategoryStatisticsIn req = new CategoryStatisticsIn();
		req.setCategoryId(category.getCategoryId());
		R<Boolean> r = annoService.categoryStatistics(req);
		boolean allowDel = r.getData();
		if (allowDel) {
			return R.fail(MessageSource.M("CATEGORY_NO_EDIT"));
		}

		String structureId = category.getStructureId();
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		//标签类型 0:下拉筛选标签；1:自定义标签
		Integer categoryType = category.getCategoryType();
		if (null == categoryType) {
			categoryType = 0;
		}
		if (category.getCategoryId() == null || category.getIndicatorId() == null) {
			return R.fail(MessageSource.M("MISSING_REQUIRED_VALUE"));
		}

		// 查询Indicator信息
		Indicator indicatorQuery = new Indicator();
		indicatorQuery.setIndicatorId(category.getIndicatorId());
		indicatorQuery.setOrganizationId(organizationId);
		indicatorQuery.setDelFlag(0);
		// 查询结构指标是否存在
		Indicator indicator = new Indicator();
		List<Indicator> indicatorList = indicatorService.selectIndicator(indicatorQuery);
		if (CollectionUtils.isNotEmpty(indicatorList)) {
			indicator = indicatorList.get(0);
		}

		if (indicator == null) {
			return R.fail(MessageSource.M("INDICATOR_ABSENT"));
		}

		//确认下原来的structure_id信息
		PathologicalIndicatorCategory categoryS = new PathologicalIndicatorCategory();
		categoryS.setStructureId(category.getStructureId());
		categoryS.setIndicatorId(category.getIndicatorId());
		categoryS.setOrganizationId(organizationId);
		List<PathologicalIndicatorCategory> listS = selectIndicatorMessage(categoryS);

		if(CollectionUtils.isNotEmpty(listS)) {
			//判断是否是自己的，如果非本身、不允许
			boolean tag = true;
			for (PathologicalIndicatorCategory categoryP : listS) {
				Long categoryPId = categoryP.getCategoryId();
				if (!category.getCategoryId().equals(categoryPId)) {
					tag = false;
					break;
				}
			}
			if (!tag) {
				return R.fail(MessageSource.M("CATEGORY_CODE_CHECK_EXIST"));
			}
		}

		PathologicalIndicatorCategory categoryHex = new PathologicalIndicatorCategory();
		categoryHex.setHex(category.getHex());
		categoryHex.setOrganizationId(organizationId);
		categoryHex.setIndicatorId(category.getIndicatorId());
		// 验证颜色值是否已经存在
		List<PathologicalIndicatorCategory> listR = selectIndicatorMessage(categoryHex);
		if (listR.size() > 0) {
			//判断是否是自己的，如果非本身、不允许
			boolean tag = true;
			for (PathologicalIndicatorCategory categoryP : listR) {
				Long categoryPId = categoryP.getCategoryId();
				if (!category.getCategoryId().equals(categoryPId)) {
					tag = false;
					break;
				}
			}
			if (!tag) {
				return R.fail(MessageSource.M("COLOR_NAME_CHECK_EXIST"));
			}
		}
		
		PathologicalIndicatorCategory categoryStructureName = new PathologicalIndicatorCategory();
		categoryStructureName.setCategoryName(category.getStructureName());
		categoryStructureName.setOrganizationId(organizationId);
		categoryStructureName.setIndicatorId(category.getIndicatorId());
		// 验证组织名称是否已经存在
		List<PathologicalIndicatorCategory> listN = selectIndicatorMessage(categoryStructureName);
		if (listN.size() > 0) {
			//判断是否是自己的，如果非本身、不允许
			boolean tag = true;
			for (PathologicalIndicatorCategory categoryP : listN) {
				Long categoryPId = categoryP.getCategoryId();
				if (!category.getCategoryId().equals(categoryPId)) {
					tag = false;
					break;
				}
			}
			if (!tag) {
				return R.fail(MessageSource.M("CATEGORY_NAME_CHECK_EXIST"));
			}
			
		}

		if (categoryType == 1) {
			String structureName = category.getStructureName();
			//校验structureId、structureName 是否已经存在
			String speciesId = indicator.getSpeciesId();
			String organId = indicator.getOrganId();

			QueryWrapper<Structure> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("species_id", speciesId);
			queryWrapper.eq("structure_id", structureId);
			queryWrapper.eq("organization_id", organizationId);
			List<Map<String, Object>> sIdList = structureService.listMaps(queryWrapper);

			if (CollectionUtils.isEmpty(sIdList)) {
				//structure表保存结构信息
				Structure structure = new Structure();
				String structureIdNew = structureId;
				String structureNameNew = structureName;
				String structureNameEnNew = structureName;
				String type = CommonConstant.STRUCTURE_RO;

				structure.setStructureId(structureIdNew);
				structure.setName(structureNameNew);
				structure.setNameEn(structureNameEnNew);
				structure.setSpeciesId(speciesId);
				structure.setOrganId(organId);
				//RO：结构类型  ROA:标注区域 ROE:考核区域
				structure.setType(type);
				structure.setOrganizationId(organizationId);
				//保存结构（3条）
				structureService.save(structure);
			} else {
				//修改名称
				UpdateWrapper<Structure> updateWrapper = new UpdateWrapper<>();
				updateWrapper.eq("organ_id", indicator.getOrganId());
				updateWrapper.eq("structure_id", category.getStructureId());
				updateWrapper.eq("species_id", indicator.getSpeciesId());
				updateWrapper.eq("organization_id", organizationId);
				Structure st = new Structure();
				st.setName(category.getStructureName());
				st.setNameEn(category.getStructureName());
				structureService.update(st, updateWrapper);
			}

			MapConstant.ORGAN_MAP = organService.selectMap();
			MapConstant.ORGAN_MAP_EN = organService.selectMapEn();
			MapConstant.STRUCTURE_MAP = structureService.selectMap();
			MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
		}

		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
		// 机构ID
		category.setOrganizationId(sysUser.getOrganizationId());
		category.setUpdateBy(sysUser.getUserId());
		category.setUpdateTime(new Date());
		// 生成完整编码
		category.setNumber(category.getStructureId());

		PathologicalIndicatorCategory targetCategory = new PathologicalIndicatorCategory();
		BeanUtils.copyProperties(category, targetCategory);
		PathologicalIndicatorCategory targetCategoryRoe = new PathologicalIndicatorCategory();
		BeanUtils.copyProperties(category, targetCategoryRoe);

		List<Structure> structureList = structureService.getListByStructureId(category.getStructureId());
		String structureName = "";
		if (CollectionUtils.isNotEmpty(structureList)) {
			Structure structure = structureList.get(0);
			structureName = structure.getName();
		}
		// 生成categoryName
		String categoryName = indicator.getIndicatorName() + structureName;
		category.setCategoryName(categoryName);
		category.setGroupNumber(CommonConstant.STRUCTURE_RO_GROUP_NUMBER);

		//修改标注类别信息
		updateByPrimaryKeySelective2(category);
		return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
	}


	@Override
	public R<String> del(CategoryVO categoryVO) {
		//查询标签数据
		PathologicalIndicatorCategory category = selectCategoryAll(categoryVO.getCategoryId());
		if (null == category) {
			return R.fail(MessageSource.M("USED"));
		}
		//校验是否可以删除
		R<String> r = delCategory(categoryVO.getCategoryId(), category);
		return r;
	}

	private R<String> delCategory(Long categoryId, PathologicalIndicatorCategory category) {
		//1、根据传入的categoryId查询其他两种类型categoryId 先校验
		CategoryStatisticsIn req = new CategoryStatisticsIn();
		req.setCategoryId(categoryId);
		R<Boolean> r = annoService.categoryStatistics(req);
		boolean allowDel = r.getData();
		if (allowDel) {
			return R.fail(MessageSource.M("USED"));
		}
		//数据处理

		Indicator indicator = indicatorMapper.selectIndicatorById(category.getIndicatorId());

		Structure queryStructure = new Structure();
		queryStructure.setOrganId(indicator.getOrganId());
		queryStructure.setStructureIds(category.getStructureId());
		queryStructure.setOrganizationId(category.getOrganizationId());
		queryStructure.setSpeciesId(indicator.getSpeciesId());
		List<Structure> delStructureList =  structureMapper.selectList(queryStructure);
		if(CollectionUtils.isNotEmpty(delStructureList)){
			for(Structure s:delStructureList){
				QueryWrapper<Structure> delWrapper = new QueryWrapper<>();
				delWrapper.eq("species_id", indicator.getSpeciesId()); 
				delWrapper.eq("organ_id", indicator.getOrganId()); 
				delWrapper.eq("structure_id", s.getStructureId()); 
				delWrapper.eq("organization_id", category.getOrganizationId()); 
				structureService.remove(delWrapper);
			}
			MapConstant.STRUCTURE_MAP = structureService.selectMap();
			MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
		}
		
		PathologicalIndicatorCategory pathological = PathologicalIndicatorCategory.builder().categoryId(category.getCategoryId()).delFlag(1).build();
		//删除标注类别
		updateByPrimaryKeySelective(pathological);
		
		IndicatorReviseVO indicatorReviseVO = IndicatorReviseVO.builder().indicatorId(category.getIndicatorId().intValue()).build();
		//更新病理表数据
		indicatorService.updateIndicator(indicatorReviseVO);
		return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
	}


	@Override
	public R<PathologicalIndicatorCategory> getInfo(Long categoryId) {
		//根据标注id获取标注类别详情
		PathologicalIndicatorCategory categoryList = selectByPrimaryKey(categoryId);
		if (null != categoryList) {
			String structureId = categoryList.getStructureId();
			List<Structure> list = structureService.getListByStructureId(structureId);
			if (CollectionUtils.isNotEmpty(list)) {
				categoryList.setStructureName(list.get(0).getName());
			}
		}
		return R.ok(categoryList);
	}

}