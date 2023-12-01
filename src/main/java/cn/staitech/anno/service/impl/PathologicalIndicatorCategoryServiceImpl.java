package cn.staitech.anno.service.impl;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.staitech.anno.vo.pathologicalIndicatorCategory.PathologicalIndicatorCategoryOutVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.lang.Snowflake;
import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Structure;
import cn.staitech.anno.mapper.IndicatorMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.annotation.LabelListVO;
import cn.staitech.anno.vo.annotation.LabelVO;
import cn.staitech.anno.vo.statistic.StatisticCategoryListInVO;
import cn.staitech.anno.vo.statistic.StatisticCategoryListOutVO;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PathologicalIndicatorCategoryServiceImpl implements PathologicalIndicatorCategoryService {
	@Resource
	private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;
	@Resource
	private ProjectMapperV1 projectMapperv1;
	@Resource
	private StructureService structureService;
	@Resource
	private IndicatorMapper indicatorMapper;


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
	 * 获取标注类别统计列表
	 *
	 * @param indicatorProjectIdList 病例指标id列表、项目id列表
	 * @return 结果
	 */
	@Override
	public List<StatisticCategoryListOutVO> selectAnnotationCategoryStatisticList(
			StatisticCategoryListInVO indicatorProjectIdList) {

		if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
			indicatorProjectIdList.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		}
		return pathologicalIndicatorCategoryMapper.selectAnnotationCategoryStatisticList(indicatorProjectIdList);
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
	 * 通过项目id统计标注类别
	 *
	 * @param projectId
	 * @return
	 */
	@Override
	public List<StatisticCategoryListOutVO> selectByProjectId(Long projectId) {
		return pathologicalIndicatorCategoryMapper.selectByProjectId(projectId);
	}

	/**
	 * 通过项目ID查询标注类别
	 *
	 * @param projectId 项目ID
	 * @return 标注类别列表
	 */
	@Override
	public List<PathologicalIndicatorCategory> selectCategoryByProjectId(Long projectId) {
		return pathologicalIndicatorCategoryMapper.selectCategoryByProjectId(projectId);
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
		List<LabelListVO> list = pathologicalIndicatorCategoryMapper.selectByIndicator(labelVO);
		for (LabelListVO listVO : list) {
			/*try {
                Structure structure = structureService.getOneStructure(listVO.getSpeciesId(), listVO.getOrganId(), listVO.getStructureId());
                // 结构名称
                if (structure == null) {
                    if (LanguageUtils.isEn()) {
                        listVO.setStructureName("Unrelated");
                    } else {
                        listVO.setStructureName("无关联");
                    }
                } else {
                    if (LanguageUtils.isEn()) {
                        listVO.setStructureName(structure.getNameEn());
                    } else {
                        listVO.setStructureName(structure.getName());
                    }

                }} catch (Exception e) {
                log.error("{};;;{};;;;{}", listVO.getSpeciesId(), listVO.getOrganId(), listVO.getStructureId());
            }*/
			if (LanguageUtils.isEn()) {
				listVO.setStructureName(listVO.getNameEn());
			}else{
				listVO.setStructureName(listVO.getName());
			}
		}
		return list;
	}

	@Override
	public List<PathologicalIndicatorCategoryOutVo> selectprojectList(Long projectId) {
		Project project = projectMapperv1.selectById(projectId);
		if (project != null) {

			List<PathologicalIndicatorCategoryOutVo> list = pathologicalIndicatorCategoryMapper.selectIndicatorList(project.getIndicatorId());
			for (PathologicalIndicatorCategoryOutVo category : list) {
				// 处理标签集中英文
				if (LanguageUtils.isEn()) {
					Indicator indicator = indicatorMapper.selectIndicatorById(category.getIndicatorId());
					if (indicator != null) {
						String categoryName = indicator.getIndicatorNameEn().concat(" ").concat(MapConstant.getStructureNameEn(category.getStructureId()));
						category.setCategoryName(categoryName);
					}
				}

			}
			return list;
		}
		return new ArrayList<>();
	}

	@Override
	public List<PathologicalIndicatorCategoryOutVo> selectProjectListFilter(Long projectId) {
		Project project = projectMapperv1.selectById(projectId);
		if (project != null) {
			return pathologicalIndicatorCategoryMapper.selectProjectListFilter(project.getIndicatorId());
		}
		return new ArrayList<>();
	}

	/**
	 * 查询标签在标注中的使用数量
	 */
	@Override
	public Integer selectLabelNum(Long categoryId) {
		return pathologicalIndicatorCategoryMapper.selectLabelNum(categoryId);
	}

	@Override
	public Integer selectLabelNumByStructureId(String structureId) {
		return pathologicalIndicatorCategoryMapper.selectLabelNumByStructureId(structureId);
	}


	@Override
	public void handlerCouponsUserStatusTimeOutToExpired(List<Long> dataList) throws ParseException {
		QueryWrapper<PathologicalIndicatorCategory> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("del_flag", "0");
		if(CollectionUtils.isNotEmpty(dataList)){
			queryWrapper.in("category_id", dataList);
		}
		queryWrapper.isNull("category_code");
		queryWrapper.orderByAsc("indicator_id","structure_id");
		List<PathologicalIndicatorCategory> list = pathologicalIndicatorCategoryMapper.selectList(queryWrapper);
		if(CollectionUtils.isNotEmpty(list)){
			//遍历所有数据，先按照structureId分组
			Map<String,Map<Integer,Long>> resultMap = new HashMap<>();
			for(PathologicalIndicatorCategory category:list){
				Long categoryId = category.getCategoryId();
				Long indicatorId = category.getIndicatorId();
				String structureId = category.getStructureId();
				String structureFather = "";
				//type 1:结构指标 2：考试 3：标注
				Integer type = -1;
				if(structureId.contains("ROA")||structureId.contains("ROE")){
					structureFather = structureId.substring(0, 6);
					if(structureId.contains("ROE")){
						//ROE:考核区域
						type = 2;
					}else if(structureId.contains("ROA")){
						//ROA:标注区域
						type = 3;
					} 
				}else{
					//结构指标
					structureFather = structureId;
					type = 1;
				}
				String structureKey = indicatorId+"_"+structureFather;
				//存入resultMap
				if(resultMap.isEmpty()){
					Map<Integer,Long> parmMap = new HashMap<>();
					parmMap.put(type, categoryId);
					resultMap.put(structureKey, parmMap);
				}else{
					//判断是否包括key
					if(resultMap.containsKey(structureKey)){
						//取出原来的数据
						Map<Integer,Long> sourceMap = resultMap.get(structureKey);
						sourceMap.put(type, categoryId);
						resultMap.put(structureKey, sourceMap);
					}else{
						//直接存
						Map<Integer,Long> parmMap = new HashMap<>();
						parmMap.put(type, categoryId);
						resultMap.put(structureKey, parmMap);
					}
				}
			}
			//打印下处理的数据
			for (Map.Entry<String, Map<Integer,Long>> entry : resultMap.entrySet()) {
				// 结构指标key
				String isKey = entry.getKey();
				Map<Integer,Long> parmValue = entry.getValue();
				//处理数据
				//Long indicatorId = Long.valueOf(isKey.split("_")[0]);
				String structureId = isKey.split("_")[1];




				//type 1:结构指标 2：考试 3：标注
				if(parmValue.containsKey(1)){
					PathologicalIndicatorCategory picVo = pathologicalIndicatorCategoryMapper.selectById(parmValue.get(1));
					Indicator indicator = indicatorMapper.selectIndicatorById(picVo.getIndicatorId());
					if(null != indicator){
						//统一添加cageoryCode 
						Snowflake snowflake = new Snowflake();
						String categoryCode = snowflake.nextIdStr();

						PathologicalIndicatorCategory category2 = new PathologicalIndicatorCategory();
						category2.setCategoryCode(categoryCode);
						category2.setCategoryId(parmValue.get(1));
						pathologicalIndicatorCategoryMapper.updateById(category2);

						//其他两个请参考结构指标
						//其他两个有则修改，没有加添加
						String structureROEId = structureId+CommonConstant.STRUCTURE_ROE;
						if(parmValue.containsKey(2)){
							//考试修改
							//PathologicalIndicatorCategory picExamVo = pathologicalIndicatorCategoryMapper.selectById(parmValue.get(2));
							PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
							BeanUtils.copyProperties(picVo, picExamVo2);
							String structureName = "";
							// 获取structureName
							Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROEId);
							if (structure != null) {
								structureName = structure.getName();
							}
							picExamVo2.setCategoryId(parmValue.get(2));
							picExamVo2.setCategoryName( indicator.getIndicatorName() + structureName);
							picExamVo2.setStructureId(structureROEId);
							picExamVo2.setNumber(structureROEId);
							picExamVo2.setCategoryCode(categoryCode);
							//修改考试
							pathologicalIndicatorCategoryMapper.updateById(picExamVo2);
						}else{
							//先确认structure表是否存在structureROEId
							List<Structure>  roeList = structureService.getListByStructureId(structureROEId);
							if(CollectionUtils.isNotEmpty(roeList)){
								//考试添加
								PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
								BeanUtils.copyProperties(picVo, picExamVo2);
								String structureName = "";
								// 获取structureName
								Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROEId);
								if (structure != null) {
									structureName = structure.getName();
								}
								picExamVo2.setCategoryName( indicator.getIndicatorName() + structureName);
								picExamVo2.setStructureId(structureROEId);
								picExamVo2.setNumber(structureROEId);
								picExamVo2.setCategoryId(null);
								picExamVo2.setCategoryCode(categoryCode);
								//add考试
								pathologicalIndicatorCategoryMapper.insertSelective(picExamVo2);
							}
						}

						//type 1:结构指标 2：考试 3：标注
						String structureROAId = structureId+CommonConstant.STRUCTURE_ROA;
						if(parmValue.containsKey(3)){
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
							//修改标注
							pathologicalIndicatorCategoryMapper.updateById(picExamVo2);
						}else{
							//先确认structure表是否存在structureROEId
							List<Structure>  roaList = structureService.getListByStructureId(structureROAId);
							if(CollectionUtils.isNotEmpty(roaList)){
								//标注添加
								PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
								BeanUtils.copyProperties(picVo, picExamVo2);
								String structureName = "";
								// 获取structureName
								Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROAId);
								if (structure != null) {
									structureName = structure.getName();
								}
								picExamVo2.setCategoryName( indicator.getIndicatorName() + structureName);
								picExamVo2.setStructureId(structureROAId);
								picExamVo2.setNumber(structureROAId);
								picExamVo2.setCategoryId(null);
								picExamVo2.setCategoryCode(categoryCode);

								//add标注
								pathologicalIndicatorCategoryMapper.insertSelective(picExamVo2);
							}
						}
					}
				}else{
					//type 1:结构指标 2：考试 3：标注
					if(parmValue.containsKey(3)){
						//统一添加cageoryCode 
						Snowflake snowflake = new Snowflake();
						String categoryCode = snowflake.nextIdStr();

						PathologicalIndicatorCategory category2 = new PathologicalIndicatorCategory();
						category2.setCategoryCode(categoryCode);
						category2.setCategoryId(parmValue.get(3));
						pathologicalIndicatorCategoryMapper.updateById(category2);

						//其他两个请标注（结构指标+考试）
						//结构指标肯定是添加
						PathologicalIndicatorCategory picVo = pathologicalIndicatorCategoryMapper.selectById(parmValue.get(3));
						Indicator indicator = indicatorMapper.selectIndicatorById(picVo.getIndicatorId());
						if(null != indicator){
							PathologicalIndicatorCategory picJGVo = new PathologicalIndicatorCategory();
							BeanUtils.copyProperties(picVo, picJGVo);
							picJGVo.setCategoryId(null);
							picJGVo.setStructureId(structureId);
							String structureName = "";
							// 获取structureName
							Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureId);
							if (structure != null) {
								structureName = structure.getName();
							}
							picJGVo.setCategoryName(indicator.getIndicatorName() + structureName);
							picJGVo.setNumber(structureId);
							picJGVo.setCategoryId(null);
							picJGVo.setCategoryCode(categoryCode);
							pathologicalIndicatorCategoryMapper.insert(picJGVo);

							String structureROEId = structureId+CommonConstant.STRUCTURE_ROE;
							//考试，有就修改，没有就添加
							if(parmValue.containsKey(2)){
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
								//修改考试
								pathologicalIndicatorCategoryMapper.updateById(picExamVo2);
							}else{
								//考试添加
								//先确认structure表是否存在structureROEId
								List<Structure>  roeList = structureService.getListByStructureId(structureROEId);
								if(CollectionUtils.isNotEmpty(roeList)){
									PathologicalIndicatorCategory picExamVo2 = new PathologicalIndicatorCategory();
									BeanUtils.copyProperties(picVo, picExamVo2);
									String structureNameROE = "";
									// 获取structureName
									Structure structure2 = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureROEId);
									if (structure2 != null) {
										structureNameROE = structure2.getName();
									}
									picExamVo2.setCategoryName( indicator.getIndicatorName() + structureNameROE);
									picExamVo2.setStructureId(structureROEId);
									picExamVo2.setNumber(structureROEId);
									picExamVo2.setCategoryId(null);
									picExamVo2.setCategoryCode(categoryCode);

									//add考试
									pathologicalIndicatorCategoryMapper.insertSelective(picExamVo2);
								}
							}
						}
					}else{
						//统一添加cageoryCode 
						Snowflake snowflake = new Snowflake();
						String categoryCode = snowflake.nextIdStr();

						PathologicalIndicatorCategory category2 = new PathologicalIndicatorCategory();
						category2.setCategoryCode(categoryCode);
						category2.setCategoryId(parmValue.get(2));
						pathologicalIndicatorCategoryMapper.updateById(category2);

						//type 1:结构指标 2：考试 3：标注
						//有考试 ==》其他两个请标注（结构指标+标注）
						//结构指标肯定是添加
						PathologicalIndicatorCategory picVo = pathologicalIndicatorCategoryMapper.selectById(parmValue.get(2));
						Indicator indicator = indicatorMapper.selectIndicatorById(picVo.getIndicatorId());
						if(null != indicator){
							PathologicalIndicatorCategory picJGVo = new PathologicalIndicatorCategory();
							BeanUtils.copyProperties(picVo, picJGVo);
							picJGVo.setCategoryId(null);
							picJGVo.setStructureId(structureId);
							picJGVo.setCategoryId(null);
							picJGVo.setCategoryCode(categoryCode);

							String structureName = "";
							// 获取structureName
							Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), structureId);
							if (structure != null) {
								structureName = structure.getName();
							}
							picJGVo.setCategoryName(indicator.getIndicatorName() + structureName);
							picJGVo.setNumber(structureId);
							pathologicalIndicatorCategoryMapper.insert(picJGVo);
							//标注先确定是否存在这个标签，如果有就添加
							String structureRoaId = structureId+CommonConstant.STRUCTURE_ROA;
							List<Structure>  roaList = structureService.getListByStructureId(structureRoaId);
							if(CollectionUtils.isNotEmpty(roaList)){
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
								picExamVo2.setCategoryId(null);
								picExamVo2.setCategoryCode(categoryCode);
								//add标注
								pathologicalIndicatorCategoryMapper.insertSelective(picExamVo2);
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
}
