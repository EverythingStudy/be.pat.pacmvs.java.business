package cn.staitech.anno.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.db.sql.Wrapper;
import cn.hutool.json.JSONUtil;
import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Structure;
import cn.staitech.anno.mapper.OrganMapper;
import cn.staitech.anno.mapper.StructureMapper;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.algorithm.AlgorithmJson;
import cn.staitech.anno.vo.annotation.CategoryVO;
import cn.staitech.anno.vo.annotation.LabelListVO;
import cn.staitech.anno.vo.annotation.LabelVO;
import cn.staitech.anno.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.vo.indicator.PathologicalIndicatorCategoryVO;
import cn.staitech.anno.vo.pathologicalIndicatorCategory.PathologicalIndicatorCategoryOutVo;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangfeng .
 * @Date 2023/9/14 17:28
 * @desc 配置标签
 */
@Slf4j
@Api(tags = "标签接口")
@RestController
@RequestMapping("/category")
public class PathologicalController {
	@Resource
	private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;

	@Resource
	private IndicatorService indicatorService;
	@Resource
	private MarkingServiceV1 markingServiceV1;

	@Resource
	private StructureService structureService;
	
	@Resource
	private OrganMapper organMapper;
	

	/**
	 * 配置标签-新增标签.
	 * 新增标签接口：[POST]	/anno/category/add
	 * 5个参数
	 * 结构编号	structureId
	 * RGB	rgb
	 * HEX	hex
	 * 图层顺序 orderNumber
	 * 结构指标ID	indicatorId
	 */
	@ApiOperation(value = "标签添加接口", notes = "wangfeng")
	@RequiresPermissions(value = {"project:pathology:tabadd", "project:pathology:tabdefine"}, logical = Logical.OR)
	@Log(title = "配置标签-新增标签", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	public R<String> add(@Validated @RequestBody PathologicalIndicatorCategoryVO vo) {
		//标签类型 0:下拉筛选标签；1:自定义标签
		Integer categoryType = vo.getCategoryType();
		if(null == categoryType){
			categoryType = 0;
		}
		String rgb = vo.getRgb();
		String hex = vo.getHex();
		Long indicatorId = vo.getIndicatorId();
		Integer orderNumber = vo.getOrderNumber(); 
		String structureId = vo.getStructureId();
		
		// 查询Indicator信息
		Indicator indicator = indicatorService.selectIndicatorsById(indicatorId);
		if (indicator == null) {
			return R.fail(MessageSource.M("INDICATOR_ABSENT"));
		}

		// wangfeng20231222
		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
		Long organizationId = sysUser.getOrganizationId();
		Long currentUserId = sysUser.getUserId();

		//验证结构是否已经存在 
		PathologicalIndicatorCategory categoryS = new PathologicalIndicatorCategory();
		categoryS.setIndicatorId(indicatorId);
		categoryS.setStructureId(structureId);
		categoryS.setOrganizationId(organizationId);
		List<PathologicalIndicatorCategory> listS = pathologicalIndicatorCategoryService.selectIndicatorMessage(categoryS);
		if (listS.size() > 0) {
			return R.fail(MessageSource.M("CATEGORY_NAME_CHECK_EXIST"));
		}
		categoryS.setStructureId(null);
		categoryS.setHex(hex);
		// 验证颜色值是否已经存在
		List<PathologicalIndicatorCategory> listR = pathologicalIndicatorCategoryService.selectIndicatorMessage(categoryS);
		if (listR.size() > 0) {
			return R.fail(MessageSource.M("COLOR_NAME_CHECK_EXIST"));
		}
		
		if(categoryType == 1){
			String structureName = vo.getStructureName();
			//校验structureId、structureName 是否已经存在
			String speciesId = indicator.getSpeciesId();
			String organId = indicator.getOrganId();
			
			/*QueryWrapper<Structure> querySidWrapper = new QueryWrapper<>();
			querySidWrapper.eq("structure_id",structureId);
			List<Map<String, Object>> sIdRList = structureService.listMaps(querySidWrapper);
			if(CollectionUtils.isNotEmpty(sIdRList)){
				return R.fail("STRUCTUREID EXIST");
			}*/
			
			
			QueryWrapper<Structure> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("species_id", speciesId);
			queryWrapper.eq("organ_id", organId);
			queryWrapper.eq("structure_id",structureId);
			queryWrapper.eq("organization_id", organizationId);
			List<Map<String, Object>> sIdList = structureService.listMaps(queryWrapper);
//			List<Structure>  sIdList2 = structureService.list(queryWrapper);
			if(CollectionUtils.isNotEmpty(sIdList)){
				return R.fail("STRUCTUREID EXIST");
			}
			queryWrapper.eq("structure_id",null);
			queryWrapper.eq("name",structureName);
//			List<Structure>  sNameList = structureService.list(queryWrapper);
			List<Map<String, Object>> sNameList = structureService.listMaps(queryWrapper);
			if(CollectionUtils.isNotEmpty(sNameList)){
				return R.fail("STRUCTURENAME EXIST");
			}
			//structure表保存结构信息
			List<Structure> structureNewList = new ArrayList<>();
			for(int j=0;j<3;j++){
				Structure structure = new Structure();
				String structureIdNew = structureId;
				String structureNameNew = structureName;
				String structureNameEnNew =structureName ;
				String type = CommonConstant.STRUCTURE_RO;
				if(j==1){
					// ROA:标注区域
					structureIdNew = structureId+CommonConstant.STRUCTURE_ROA;
					structureNameNew = structureName+" 标注区域";
					structureNameEnNew = structureName+CommonConstant.STRUCTURE_ROA;
					type = CommonConstant.STRUCTURE_ROA;
				}else if(j==2){
					// ROE:考核区域
					structureIdNew = structureId+CommonConstant.STRUCTURE_ROE;
					structureNameNew = structureName+" 考核区域";
					structureNameEnNew = structureName+CommonConstant.STRUCTURE_ROE;
					type = CommonConstant.STRUCTURE_ROE;
				}
				structure.setStructureId(structureIdNew);
				structure.setName(structureNameNew);
				structure.setNameEn(structureNameEnNew);
				structure.setSpeciesId(speciesId);
				structure.setOrganId(organId);
				//RO：结构类型  ROA:标注区域 ROE:考核区域
				structure.setType(type);
				structure.setOrganizationId(organizationId);
				structureNewList.add(structure);
			}
			//保存结构（3条）
			structureService.saveBatch(structureNewList);
			
			MapConstant.ORGAN_MAP = selectMap();
			MapConstant.ORGAN_MAP_EN = selectMapEn();
			MapConstant.STRUCTURE_MAP = structureService.selectMap();
			MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
		}
		
		List<String> structureIdList = new ArrayList<String>();
		structureIdList.add(vo.getStructureId());
		//标注区域
		String structureRoaId = structureId+CommonConstant.STRUCTURE_ROA;
		List<Structure>  roaList = structureService.getListByStructureId(structureRoaId);
		if(CollectionUtils.isNotEmpty(roaList)){
			structureIdList.add(structureRoaId);
		}
		//考核区域
		String structureRoeId = structureId+CommonConstant.STRUCTURE_ROE;
		List<Structure>  roeList = structureService.getListByStructureId(structureRoeId);
		if(CollectionUtils.isNotEmpty(roeList)){
			structureIdList.add(structureRoeId);
		}
		Date currentDate = DateUtil.date();
		Snowflake snowflake = new Snowflake();
		String categoryCode = snowflake.nextIdStr();
		for(int i=0;i<structureIdList.size();i++){
			String currentStructureId = structureIdList.get(i);
			PathologicalIndicatorCategory category = new PathologicalIndicatorCategory();
			category.setStructureId(currentStructureId);
			category.setRgb(rgb);
			category.setHex(hex);
			category.setIndicatorId(indicatorId);
			category.setOrderNumber(orderNumber);

			String structureName = "";
			// 获取structureName
			Structure structure = structureService.getOneStructure(indicator.getSpeciesId(), indicator.getOrganId(), currentStructureId);
			if (structure != null) {
				structureName = structure.getName();
			}

			// 生成categoryName
			String categoryName = indicator.getIndicatorName() + structureName;
			category.setCategoryName(categoryName);
			// 生成完整编码
			category.setNumber(currentStructureId);
			category.setCreateBy(currentUserId);
			category.setOrganizationId(organizationId);
			category.setCreateTime(currentDate);
			category.setCategoryCode(categoryCode);
			if(currentStructureId.endsWith("ROA")){
				//标注区域
				category.setGroupNumber(CommonConstant.STRUCTURE_ROA_GROUP_NUMBER);
			}else if(currentStructureId.endsWith("ROE")){
				//考核区域
				category.setGroupNumber(CommonConstant.STRUCTURE_ROE_GROUP_NUMBER);
			}else{
				category.setGroupNumber(CommonConstant.STRUCTURE_RO_GROUP_NUMBER);
			}
			category.setCategoryType(categoryType);
			pathologicalIndicatorCategoryService.insertSelective(category);
			IndicatorReviseVO indicatorReviseVO = IndicatorReviseVO.builder().indicatorId(indicatorId.intValue()).build();
			// 更新病理表数据
			indicatorService.updateIndicator(indicatorReviseVO);
		}
		return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
	}


	/**
	 * 配置标签-标签列表 .
	 */
	@ApiOperation(value = "获取标注类别列表接口", notes = "wangfeng")
	@RequiresPermissions("project:pathology:tablist")
	@Log(title = "配置标签-标签列表", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.QUERY)
	@PostMapping("/all")
	public R<PageMaster<LabelListVO>> list(@RequestBody LabelVO labelVO) {
		PageHelper.startPage(labelVO.getPageNum(), labelVO.getPageSize()).setReasonable(true);
		// 获取病理指标下的标注类别
		List<LabelListVO> categoryList = pathologicalIndicatorCategoryService.selectByIndicator(labelVO);
		PageMaster<LabelListVO> pageMaster = new PageMaster<>(categoryList);
		return R.ok(pageMaster);
	}

	/**
	 * 配置标签-标签列表 .
	 */
	@ApiOperation(value = "根据项目查询结构指标列表", notes = "gjt")
	@Log(title = "配置标签-标签列表", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.QUERY)
	@GetMapping("/selectList")
	public R<List<PathologicalIndicatorCategoryOutVo>> selectList(@RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId) {
		// 获取病理指标下的标注类别
		List<PathologicalIndicatorCategoryOutVo> categoryList = pathologicalIndicatorCategoryService.selectprojectList(projectId);
		return R.ok(categoryList);
	}


	@ApiOperation(value = "根据项目查询结构指标列表(不包含标注区域)", notes = "gjt")
	@Log(title = "配置标签-标签列表", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.QUERY)
	@GetMapping("/selectListFilter")
	public R<List<PathologicalIndicatorCategoryOutVo>> selectListFilter(@RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId) {
		//获取病理指标下的标注类别
		List<PathologicalIndicatorCategoryOutVo> categoryList = pathologicalIndicatorCategoryService.selectProjectListFilter(projectId);
		return R.ok(categoryList);
	}


	/**
	 * 标签详细 .
	 */
	@ApiOperation(value = "获取标签详情接口", notes = "wangfeng")
	@GetMapping(value = "/details")
	public R<PathologicalIndicatorCategory> getInfo(
			@RequestParam @ApiParam(name = "categoryId", value = "标注类别id", required = true) Long categoryId) {
		//根据标注id获取标注类别详情
		PathologicalIndicatorCategory categoryList = pathologicalIndicatorCategoryService.selectByPrimaryKey(
				categoryId);
		if(null != categoryList){
			String structureId = categoryList.getStructureId();
			List<Structure> list = structureService.getListByStructureId(structureId);
			if(CollectionUtils.isNotEmpty(list)){
				categoryList.setStructureName(list.get(0).getName());
			}
		}
		return R.ok(categoryList);
	}

	/**
	 * 配置标签-编辑 .
	 */
	@ApiOperation(value = "标注类别修改接口", notes = "wangfeng")
	@RequiresPermissions("project:pathology:tabedit")
	@Log(title = "配置标签-编辑", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	public R<String> edit(@Validated @RequestBody PathologicalIndicatorCategory category) {
		String structureId = category.getStructureId();
		//标签类型 0:下拉筛选标签；1:自定义标签
		Integer categoryType = category.getCategoryType();
		if(null == categoryType){
			categoryType = 0;
		}
		if (category.getCategoryId() == null || category.getIndicatorId() == null) {
			return R.fail(MessageSource.M("MISSING_REQUIRED_VALUE"));
		}

		// 查询Indicator信息
		Indicator indicator = indicatorService.selectIndicatorsById(category.getIndicatorId());
		if (indicator == null) {
			return R.fail(MessageSource.M("INDICATOR_ABSENT"));
		}

		//确认下原来的structure_id信息
		PathologicalIndicatorCategory sourcePic = pathologicalIndicatorCategoryService.selectByPrimaryKey(category.getCategoryId());
		//验证结构是否已经存在  		BeanUtils.copyProperties(category, targetCategory);
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		PathologicalIndicatorCategory categoryS = new PathologicalIndicatorCategory();
		categoryS.setStructureId(category.getStructureId());
		categoryS.setIndicatorId(category.getIndicatorId());
		categoryS.setOrganizationId(organizationId);
		List<PathologicalIndicatorCategory> listS = pathologicalIndicatorCategoryService.selectIndicatorMessage(categoryS);
		if (listS.size() > 0) {
			//判断是否是自己的，如果非本身、不允许
			boolean tag = true;
			for(PathologicalIndicatorCategory categoryP:listS){
				Long categoryPId = categoryP.getCategoryId();
				if(!category.getCategoryId().equals(categoryPId)){
					tag = false;
					break;
				}
			}
			if(!tag){
				return R.fail(MessageSource.M("CATEGORY_NAME_CHECK_EXIST"));
			}
		}
		categoryS.setStructureId(null);
		categoryS.setHex(category.getHex());
		// 验证颜色值是否已经存在
		List<PathologicalIndicatorCategory> listR = pathologicalIndicatorCategoryService.selectIndicatorMessage(categoryS);
		if (listR.size() > 0) {
			//判断是否是自己的，如果非本身、不允许
			boolean tag = true;
			for(PathologicalIndicatorCategory categoryP:listR){
				if(!sourcePic.getCategoryCode().equals(categoryP.getCategoryCode())){
					tag = false;
					break;
				}
			}
			if(!tag){
				return R.fail(MessageSource.M("COLOR_NAME_CHECK_EXIST"));
			}
		}
		
		if(categoryType == 1){
			String structureName = category.getStructureName();
			//校验structureId、structureName 是否已经存在
			String speciesId = indicator.getSpeciesId();
			String organId = indicator.getOrganId();

			QueryWrapper<Structure> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("species_id", speciesId);
			queryWrapper.eq("structure_id",structureId);
			queryWrapper.eq("organization_id", organizationId);
//			List<Structure>  sIdList = structureService.list(queryWrapper);
			List<Map<String, Object>> sIdList = structureService.listMaps(queryWrapper);

			if(CollectionUtils.isEmpty(sIdList)){
				//structure表保存结构信息
				List<Structure> structureNewList = new ArrayList<>();
				for(int j=0;j<3;j++){
					Structure structure = new Structure();
					String structureIdNew = structureId;
					String structureNameNew = structureName;
					String structureNameEnNew =structureName ;
					String type = CommonConstant.STRUCTURE_RO;
					if(j==1){
						// ROA:标注区域
						structureIdNew = structureId+CommonConstant.STRUCTURE_ROA;
						structureNameNew = structureName+" 标注区域";
						structureNameEnNew = structureName+CommonConstant.STRUCTURE_ROA;
						type = CommonConstant.STRUCTURE_ROA;
					}else if(j==2){
						// ROE:考核区域
						structureIdNew = structureId+CommonConstant.STRUCTURE_ROE;
						structureNameNew = structureName+" 考核区域";
						structureNameEnNew = structureName+CommonConstant.STRUCTURE_ROE;
						type = CommonConstant.STRUCTURE_ROE;
					}

					structure.setStructureId(structureIdNew);
					structure.setName(structureNameNew);
					structure.setNameEn(structureNameEnNew);
					structure.setSpeciesId(speciesId);
					structure.setOrganId(organId);
					//RO：结构类型  ROA:标注区域 ROE:考核区域
					structure.setType(type);
					structure.setOrganizationId(organizationId);
					structureNewList.add(structure);
				}
				//保存结构（3条）
				structureService.saveBatch(structureNewList);
			}else{
				//修改名称
				UpdateWrapper<Structure> updateWrapper=new UpdateWrapper<>();
				updateWrapper.eq("organ_id", indicator.getOrganId());
				updateWrapper.eq("structure_id", category.getStructureId());
				updateWrapper.eq("species_id", indicator.getSpeciesId());
				updateWrapper.eq("organization_id", organizationId);
				Structure st = new Structure();
				st.setName(category.getStructureName());
				st.setNameEn(category.getStructureName());
				structureService.update(st, updateWrapper);
				// ROA:标注区域
//				String structureRoaName = category.getStructureName()+" "+CommonConstant.STRUCTURE_ROA;
				String structureRoaId = category.getStructureId()+CommonConstant.STRUCTURE_ROA;

				UpdateWrapper<Structure> updateROAWrapper=new UpdateWrapper<>();
				updateROAWrapper.eq("organ_id", indicator.getOrganId());
				updateROAWrapper.eq("structure_id", structureRoaId);
				updateROAWrapper.eq("species_id", indicator.getSpeciesId());
				updateROAWrapper.eq("organization_id", organizationId);
				Structure st2 = new Structure();
				st2.setName(category.getStructureName()+" 标注区域");
				st2.setNameEn(category.getStructureName()+CommonConstant.STRUCTURE_ROA);
				structureService.update(st2, updateROAWrapper);
				
				// ROE:考核区域
				String structureRoEId = category.getStructureId()+CommonConstant.STRUCTURE_ROE;

				UpdateWrapper<Structure> updateROEWrapper=new UpdateWrapper<>();
				updateROEWrapper.eq("organ_id", indicator.getOrganId());
				updateROEWrapper.eq("structure_id", structureRoEId);
				updateROEWrapper.eq("species_id", indicator.getSpeciesId());
				updateROEWrapper.eq("organization_id", organizationId);
				Structure st3 = new Structure();
				st3.setName(category.getStructureName()+" 考核区域");
				st3.setNameEn(category.getStructureName()+CommonConstant.STRUCTURE_ROE);
				structureService.update(st3, updateROEWrapper);
				
			}
			
			MapConstant.ORGAN_MAP = selectMap();
			MapConstant.ORGAN_MAP_EN = selectMapEn();
			MapConstant.STRUCTURE_MAP = structureService.selectMap();
			MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
		}


		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
		// 机构ID
		category.setOrganizationId(sysUser.getOrganizationId());
		category.setUpdateBy(sysUser.getUserId());
		//		category.setUpdateBy(1L);
		//		category.setOrganizationId(1L);
		category.setUpdateTime(new Date());
		// 生成完整编码
		category.setNumber(category.getStructureId());

		PathologicalIndicatorCategory targetCategory =  new PathologicalIndicatorCategory();
		BeanUtils.copyProperties(category, targetCategory);
		PathologicalIndicatorCategory targetCategoryRoe =  new PathologicalIndicatorCategory();
		BeanUtils.copyProperties(category, targetCategoryRoe);

		// 获取structureName
		String structureName = structureService.getById(category.getStructureId()).getName();
		// 生成categoryName
		String categoryName = indicator.getIndicatorName() + structureName;
		category.setCategoryName(categoryName);


		// 验证是否存在该条件的记录(排除自己)  A：必填项校验。B：结构编码在当前列表内不可重复；C：结构名称在当前列表内不可重复。D：图层顺序在当前列表内不可重复；E：颜色值在当前列表不可重复
		/*List<PathologicalIndicatorCategory> list = pathologicalIndicatorCategoryService.selectIndicatorMessageForUpdate(category);
		if (list.size() > 0) {
			return R.fail(MessageSource.M("CATEGORY_NAME_EXIST"));
		}*/

		category.setGroupNumber(CommonConstant.STRUCTURE_RO_GROUP_NUMBER);

		//修改标注类别信息
		String retStatus = pathologicalIndicatorCategoryService.updateByPrimaryKeySelective2(category);
		//查看当前结构是否只要结构编码
		//		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		//		boolean containsValue = Arrays.asList(CommonConstant.ORGANIZATION_ID).contains(organizationId);
		//TODO 另外考核区域和标注区域同样处理，structureId、number、categoryName需要单独处理，修改时候需要用自己的categoryId和indicatorId
		if(retStatus.equals("1")){
			updateCategory(targetCategory,targetCategoryRoe,sourcePic,indicator);
		}
		return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
	}

	private void updateCategory(PathologicalIndicatorCategory targetCategory,PathologicalIndicatorCategory targetCategoryRoe,PathologicalIndicatorCategory sourcePic,Indicator indicator){
		//根据原来的structureId查询原来的考核区域和标注区域数据，然后更新structure_id、number
		String sourceStructureId = sourcePic.getStructureId();
		//标注编码
		String categoryCode = sourcePic.getCategoryCode();
		//标注区域
		String structureRoaId = sourceStructureId+CommonConstant.STRUCTURE_ROA;
		List<Structure>  roaList = structureService.getListByStructureId(structureRoaId);
		if(CollectionUtils.isNotEmpty(roaList)){
			updateSourceCategory(structureRoaId, targetCategory, indicator,1,categoryCode);
		}
		//考核区域
		String structureRoeId = sourceStructureId+CommonConstant.STRUCTURE_ROE;
		List<Structure>  roeList = structureService.getListByStructureId(structureRoeId);
		if(CollectionUtils.isNotEmpty(roeList)){
			updateSourceCategory(structureRoeId, targetCategoryRoe, indicator,2,categoryCode);
		}
	}

	private void updateSourceCategory(String hisStructureId,PathologicalIndicatorCategory targetCategory,Indicator indicator,int type,String categoryCode){
		PathologicalIndicatorCategory picRoaVo = new PathologicalIndicatorCategory();
		picRoaVo.setStructureId(hisStructureId);
		picRoaVo.setCategoryCode(categoryCode);
		picRoaVo.setDelFlag(0);
		List<PathologicalIndicatorCategory> picList = pathologicalIndicatorCategoryService.selectIndicatorMessage(picRoaVo);
		if(CollectionUtils.isNotEmpty(picList)){
			PathologicalIndicatorCategory picRoa = picList.get(0);
			Long categoryId = picRoa.getCategoryId();
			//标注区域
			String newStructureId = targetCategory.getStructureId();
			if(type == 1){
				newStructureId =  newStructureId+CommonConstant.STRUCTURE_ROA;
			}else{
				newStructureId =  newStructureId+CommonConstant.STRUCTURE_ROE;
			}
			String newNum = newStructureId;

			// 获取structureName
			String structureName = structureService.getById(newStructureId).getName();
			// 生成categoryName
			String categoryName = indicator.getIndicatorName() + structureName;

			//赋值
			targetCategory.setStructureId(newStructureId);
			targetCategory.setNumber(newNum);
			targetCategory.setCategoryId(categoryId);
			targetCategory.setCategoryName(categoryName);
			if(newStructureId.endsWith("ROA")){
				//标注区域
				targetCategory.setGroupNumber(CommonConstant.STRUCTURE_ROA_GROUP_NUMBER);
			}else if(newStructureId.endsWith("ROE")){
				//考核区域
				targetCategory.setGroupNumber(CommonConstant.STRUCTURE_ROE_GROUP_NUMBER);
			}else{
				targetCategory.setGroupNumber(CommonConstant.STRUCTURE_RO_GROUP_NUMBER);
			}
			//修改标注类别信息
			pathologicalIndicatorCategoryService.updateByPrimaryKeySelective2(targetCategory);
		}
	}

	/**	
	 * 配置标签-删除 .
	 */
	@ApiOperation(value = "标签删除接口", notes = "ZMJ")
	@RequiresPermissions("project:pathology:tabremove")
	@Log(title = "配置标签-删除", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.DELETE)
	@PostMapping("/del")
	public R<String> del(@RequestBody CategoryVO categoryVO) {
		//查询标签数据
		PathologicalIndicatorCategory category = pathologicalIndicatorCategoryService.selectCategoryAll(categoryVO.getCategoryId());
		if (null == category) {
			return R.fail(MessageSource.M("USED"));
		}
		//TODO 校验结构指标、标注区域和考试区域是否可以删除
		//1、根据传入的categoryId查询其他两种类型categoryId 先校验
		R<String> r = delCategory(categoryVO.getCategoryId(),category);
		return r;
	}

	private R<String> delCategory(Long categoryId,PathologicalIndicatorCategory category){
		//1、根据传入的categoryId查询其他两种类型categoryId 先校验
		Boolean tag = true;
		String structureId = category.getStructureId();
		Long indicatorId = category.getIndicatorId();
		//查询这一组（3条）数据
		PathologicalIndicatorCategory categoryQuery = PathologicalIndicatorCategory.builder().structureIds(structureId).indicatorId(indicatorId).delFlag(0).build();
		List<PathologicalIndicatorCategory> categoryList = pathologicalIndicatorCategoryService.selectIndicatorMessage(categoryQuery);
		if(CollectionUtils.isNotEmpty(categoryList)){
			for(PathologicalIndicatorCategory perCategory:categoryList){
				Long perCategoryId = perCategory.getCategoryId();
				//查询标注是否关联标签
				/*Integer num = pathologicalIndicatorCategoryService.selectLabelNum(perCategoryId);
				if (0 < num) {
					tag = false;
					break;
				}*/
				// 查询标注数量，大于0不可删除
				QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
				markingQueryWrapper.eq("category_id",perCategoryId);
				if(markingServiceV1.count(markingQueryWrapper) > 0){
					tag = false;
					break;
				}
			}
		}

		//校验
		if(!tag){
			return R.fail(MessageSource.M("USED"));
		}

		//数据处理
		if(CollectionUtils.isNotEmpty(categoryList)){
			for(PathologicalIndicatorCategory perCategory:categoryList){
				PathologicalIndicatorCategory pathological = PathologicalIndicatorCategory.builder().categoryId(perCategory.getCategoryId()).delFlag(1).build();
				//删除标注类别
				pathologicalIndicatorCategoryService.updateByPrimaryKeySelective(pathological);
				IndicatorReviseVO indicatorReviseVO = IndicatorReviseVO.builder().indicatorId(perCategory.getIndicatorId().intValue()).build();
				//更新病理表数据
				indicatorService.updateIndicator(indicatorReviseVO);
			}
		}	
		return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
	}

	/**
	 * 验证标签名称、颜色、图层顺序、是否存在、是否关联切片2.0
	 * String checkCategory = checkCategory(category);
	 * if (!checkCategory.equals("1")) {
	 * return R.fail(checkCategory);
	 * }
	 */
	public String checkCategory(PathologicalIndicatorCategory annotationCategory) {
		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
		// 机构ID
		annotationCategory.setOrganizationId(sysUser.getOrganizationId());

		PathologicalIndicatorCategory category = PathologicalIndicatorCategory.builder().categoryId(annotationCategory.getCategoryId())
				.categoryName(annotationCategory.getCategoryName()).indicatorId(annotationCategory.getIndicatorId()).build();
		// 获取病理下的标注类别名称是否存在
		List<PathologicalIndicatorCategory> categoryList = pathologicalIndicatorCategoryService.selectIndicatorMessage(
				category);
		category.setCategoryName(null);
		category.setHex(annotationCategory.getHex());
		// 查询病理下的标注类别颜色是否存在
		List<PathologicalIndicatorCategory> categories = pathologicalIndicatorCategoryService.selectIndicatorMessage(
				category);
		category.setHex(null);
		category.setOrderNumber(annotationCategory.getOrderNumber());
		// 查询图层顺序是否已存在
		List<PathologicalIndicatorCategory> orderNumber = pathologicalIndicatorCategoryService.selectIndicatorMessage(
				category);
		if (!categoryList.isEmpty()) {
			return MessageSource.M("CATEGORY_NAME_EXIST");
		} else if (!categories.isEmpty()) {
			return MessageSource.M("COLOR_NAME_EXIST");
		} else if (!orderNumber.isEmpty()) {
			return MessageSource.M("LAYER_ALREADY_EXISTS");
		} else {
			return CommonConstant.NUMBER_1;
		}
	}

	public Map<String, String> selectMap() {
		return select(false);
	}

	public Map<String, String> selectMapEn() {
		return select(true);
	}

	public Map<String, String> select(boolean en) {
		List<Organ> list = organMapper.selectList();
		if (en) {
			return list.stream().collect(Collectors.toMap(item -> item.getSpeciesCode().concat(item.getOrganId()), Organ::getNameEn));
		} else {
			return list.stream().collect(Collectors.toMap(item -> item.getSpeciesCode().concat(item.getOrganId()), Organ::getName));
		}
	}


	@PostMapping("/test")
	public R test() throws ParseException {
		List<Long> dataList = new ArrayList<>();
		//		dataList.add(1638L);
		pathologicalIndicatorCategoryService.handlerCouponsUserStatusTimeOutToExpired(dataList);
		return R.ok();
	}
}
