package cn.staitech.anno.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.ProjectLabelStatistics;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.ProjectStatistics;
import cn.staitech.anno.mapper.LabelStatisticsMapper;
import cn.staitech.anno.mapper.ProjectInStatisticsMapper;
import cn.staitech.anno.mapper.ProjectLabelStatisticsMapper;
import cn.staitech.anno.project.vo.SelectProjectVO;
import cn.staitech.anno.service.LabelStatisticsService;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.labelprojectstatistics.ImageMarkingIn;
import cn.staitech.anno.vo.labelprojectstatistics.LabelIn;
import cn.staitech.anno.vo.labelprojectstatistics.LabelOut;
import cn.staitech.anno.vo.labelprojectstatistics.LabelSetIn;
import cn.staitech.anno.vo.labelprojectstatistics.LabelSetOut;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectCreateByOut;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectInVO;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelIn;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelOut;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectListIn;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectListOut;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LabelStatisticsServiceImpl implements LabelStatisticsService {

	@Resource
	private LabelStatisticsMapper labelStatisticsMapper;
	@Resource
	private ProjectInStatisticsMapper projectInStatisticsMapper;
	@Resource
	private ProjectMemberService projectMemberService;
	
	@Resource
	private ProjectLabelStatisticsMapper projectLabelStatisticsMapper;
	/**
	 * 获取项目列表
	 */
	@Override
	public List<ProjectListOut> projectList(ImageMarkingIn imageMarkingIn) {
		ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").statusList(imageMarkingIn.getStatusList()).build();
		return labelStatisticsMapper.projectList(projectInVO);
	}

	/**
	 * 查询标准集
	 */
	@Override
	public List<LabelSetOut> projectLabelSet(LabelSetIn labelSetIn) {
		//没有项目参数时
		if (CollectionUtils.isNotEmpty(labelSetIn.getProjectIds())) {
			return labelStatisticsMapper.projectLabelSet(labelSetIn);
		}
		labelSetIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		labelSetIn.setUserId(SecurityUtils.getUserId());
		return labelStatisticsMapper.projectLabelSet(labelSetIn);
	}


	/**
	 * 查询标签
	 */
	@Override
	public List<LabelOut> labelList(LabelIn labelIn) {
		//无属性标签
		LabelOut labelOut = LabelOut.builder().categoryId(0L).categoryName(MessageSource.M("NO_ATTRIBUTE")).build();
		//只传项目id
		if (CollectionUtils.isNotEmpty(labelIn.getProjectIds()) && labelIn.getIndicatorIds().isEmpty()) {
			List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
			labelOuts.add(labelOut);
			return labelOuts;
		}
		//传项目id和标签集id 或只传标签集id
		if (CollectionUtils.isNotEmpty(labelIn.getIndicatorIds())) {
			labelIn.setProjectIds(null);
			List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
			labelOuts.add(labelOut);
			return labelOuts;
		}
		//不传项目id和标签集id
		labelIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		labelIn.setUserId(SecurityUtils.getUserId());
		List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
		labelOuts.add(labelOut);
		return labelOuts;
	}


	/**
	 * 查询项目标签
	 */
	@Override
	public R<PageMaster<ProjectLabelOut>> projectLabelList(ProjectLabelIn projectLabelIn) {
		List<Long> projectIds = new ArrayList<>();
		projectIds = projectLabelIn.getProjectIds();
		List<Long> indicatorIds = projectLabelIn.getIndicatorIds();
		List<Long> categoryIds = projectLabelIn.getCategoryIds();

		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getUserId();
//		        Long organizationId = 1L;
//		Long currentUserId = 39L;

		if(null == projectIds || CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		//查询
		ProjectLabelStatistics projectLabelStatistics = new ProjectLabelStatistics();
		if(CollectionUtils.isNotEmpty(projectIds)){
			projectLabelStatistics.setProjectIdList(projectIds);
		}

		if(CollectionUtils.isNotEmpty(indicatorIds)){
			projectLabelStatistics.setIndicatorIdList(indicatorIds);
		}

		if(CollectionUtils.isNotEmpty(categoryIds)){
			projectLabelStatistics.setCategoryIdList(categoryIds);
		}

		projectLabelStatistics.setDelFlag("0");
		projectLabelStatistics.setOrganizationId(organizationId);
		
		PageHelper.startPage(projectLabelIn.getPageNum(), projectLabelIn.getPageSize()).setReasonable(true);
		List<ProjectLabelOut> list = new ArrayList<>();
		list = projectLabelStatisticsMapper.getProjectLabelStatistics(projectLabelStatistics);
		
		PageMaster<ProjectLabelOut> pageMaster = new PageMaster<>(list);
		return R.ok(pageMaster);
	}


	/**
	 * 项目创建者
	 */
	@Override
	public List<ProjectCreateByOut> userList(LabelSetIn labelSetIn) {
		//有项目信息的查询项目创建者信息
		if (CollectionUtils.isNotEmpty(labelSetIn.getProjectIds())) {
			return labelStatisticsMapper.userList(labelSetIn);
		}
		//若未选项目，则下拉框包含该机构下所有用户
		labelSetIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		labelSetIn.setCurrentUserId(SecurityUtils.getLoginUser().getSysUser().getUserId());
		return labelStatisticsMapper.userList(labelSetIn);
	}


	/**
	 * 查询项目
	 */
	@Override
	public R<PageMaster<ProjectLabelOut>> itemList(ProjectListIn projectListIn) {
		//        {"projectIds":[313,521,522,490],"statusList":["3","2"],"indicatorIds":[1145,1149],"userIds":[14],
		//        "description":"fsfsfsfsf","createTimeParams":{"beginTime":"2024-01-15","endTime":"2024-02-21"},"pageNum":1,"pageSize":10,"total":9}

		Long currentUserId = SecurityUtils.getUserId();
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
//		        Long organizationId = 1L;
//		Long currentUserId = 39L;

		List<Long> projectIds = new ArrayList<>();

		projectIds = projectListIn.getProjectIds();
		List<Integer> statusList = projectListIn.getStatusList();
		List<Long> indicatorIds = projectListIn.getIndicatorIds();
		List<Long> userIds = projectListIn.getUserIds();
		String description = projectListIn.getDescription();
		Map<String, Object> createTimeParams = projectListIn.getCreateTimeParams();


		if(null == projectIds || CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		//查询
		ProjectStatistics projectStatistics = new ProjectStatistics();
		if(CollectionUtils.isNotEmpty(projectIds)){
			projectStatistics.setProjectIdList(projectIds);
		}

		if(CollectionUtils.isNotEmpty(statusList)){
			projectStatistics.setProjectStatusList(statusList);
		}

		if(CollectionUtils.isNotEmpty(indicatorIds)){
			projectStatistics.setIndicatorIdList(indicatorIds);
		}

		if(CollectionUtils.isNotEmpty(userIds)){
			projectStatistics.setProjectCreateByList(userIds);
		}

		if(StringUtils.isNotEmpty(description)){
			projectStatistics.setDescription(description);
		}

		if(null != createTimeParams && !createTimeParams.isEmpty() && createTimeParams.containsKey("beginTime")&& createTimeParams.containsKey("endTime")){
			projectStatistics.setCreateTimeParams(createTimeParams);
		}
		
		projectStatistics.setOrganizationId(organizationId);
		projectStatistics.setDelFlag("0");
		
		PageHelper.startPage(projectListIn.getPageNum(), projectListIn.getPageSize()).setReasonable(true);

		List<ProjectLabelOut> list =  new ArrayList<>();
		list = projectInStatisticsMapper.getProjectStatistics(projectStatistics);
		PageMaster<ProjectLabelOut> pageMaster = new PageMaster<>(list);
		return R.ok(pageMaster);
	}


	/**
	 * 标签导出
	 */
	@Override
	public void labelExport(ProjectLabelIn projectLabelIn, HttpServletResponse response) throws Exception {
		List<Long> projectIds = new ArrayList<>();
		projectIds = projectLabelIn.getProjectIds();
		projectIds = projectLabelIn.getProjectIds();
		List<Long> indicatorIds = projectLabelIn.getIndicatorIds();
		List<Long> categoryIds = projectLabelIn.getCategoryIds();

		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getUserId();

		if(null == projectIds || CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}


		//查询
		
		ProjectLabelStatistics projectLabelStatistics = new ProjectLabelStatistics();
		if(CollectionUtils.isNotEmpty(projectIds)){
			projectLabelStatistics.setProjectIdList(projectIds);
		}

		if(CollectionUtils.isNotEmpty(indicatorIds)){
			projectLabelStatistics.setIndicatorIdList(indicatorIds);
		}

		if(CollectionUtils.isNotEmpty(categoryIds)){
			projectLabelStatistics.setCategoryIdList(categoryIds);
		}

		projectLabelStatistics.setDelFlag("0");
		projectLabelStatistics.setOrganizationId(organizationId);
		
		List<ProjectLabelOut> list = new ArrayList<>();
		list = projectLabelStatisticsMapper.getProjectLabelStatistics(projectLabelStatistics);
		
		List<Map<String, String>> titleList = getTitleList(CommonConstant.LABEL_STATISTICS_KEY, CommonConstant.LABEL_STATISTICS_VALUE);
		ExcelTool excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
		List<Column> titleData = excelTool.columnTransformer(titleList);
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
		//        excelTool.exportExcel(titleData, projectLabelOuts, response.getOutputStream(), true, false);
		excelTool.exportExcel(titleData, list, response.getOutputStream(), true, false);

	}

	public List<Map<String, String>> getTitleList(String[] colHeadKey, String[] colHeadValue) {
		// 定义表头
		List<Map<String, String>> list = new ArrayList<>();

		for (int i = 0; i < colHeadKey.length; i++) {
			Map<String, String> map = new HashMap<String, String>(1);
			map.put(colHeadKey[i], colHeadValue[i]);
			list.add(map);
		}
		return list;
	}


	/**
	 * 项目导出
	 */
	@Override
	public void projectExport(ProjectListIn projectListIn, HttpServletResponse response) throws Exception {
		projectListIn.setProjectType("1");
		projectListIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		projectListIn.setUsers(SecurityUtils.getUserId());
		

		List<Long> projectIds = new ArrayList<>();
		projectIds = projectListIn.getProjectIds();
		List<Integer> statusList = projectListIn.getStatusList();
		List<Long> indicatorIds = projectListIn.getIndicatorIds();
		List<Long> userIds = projectListIn.getUserIds();
		String description = projectListIn.getDescription();
		Map<String, Object> createTimeParams = projectListIn.getCreateTimeParams();

		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getUserId();

		if(null == projectIds || CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		//查询
		ProjectStatistics projectStatistics = new ProjectStatistics();
		if(CollectionUtils.isNotEmpty(projectIds)){
			projectStatistics.setProjectIdList(projectIds);
		}

		if(CollectionUtils.isNotEmpty(statusList)){
			projectStatistics.setProjectStatusList(statusList);
		}

		if(CollectionUtils.isNotEmpty(indicatorIds)){
			projectStatistics.setIndicatorIdList(indicatorIds);
		}

		if(CollectionUtils.isNotEmpty(userIds)){
			projectStatistics.setProjectCreateByList(userIds);
		}

		if(StringUtils.isNotEmpty(description)){
			projectStatistics.setDescription(description);
		}

		if(null != createTimeParams && !createTimeParams.isEmpty() && createTimeParams.containsKey("beginTime")&& createTimeParams.containsKey("endTime")){
			projectStatistics.setCreateTimeParams(createTimeParams);
		}
		projectStatistics.setOrganizationId(organizationId);
		projectStatistics.setDelFlag("0");
		List<ProjectLabelOut> list =  new ArrayList<>();
		list = projectInStatisticsMapper.getProjectStatistics(projectStatistics);

		// 构造表头的每个列头 定义表头
		List<Map<String, String>> titleList = getTitleList(CommonConstant.PROJECT_STATISTICS_KEY, CommonConstant.PROJECT_STATISTICS_VALUE);
		ExcelTool excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
		List<Column> titleData = excelTool.columnTransformer(titleList);
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
		excelTool.exportExcel(titleData, list, response.getOutputStream(), true, false);


	}


	private List<Long> getProjectIdStatistics(Long userId,Long organizationId){
		//查询当前用户参与的所有项目
		List<Long> projectIdList = new ArrayList<>();
		ProjectMember projectMember = new ProjectMember();
		projectMember.setUserId(userId);
		projectMember.setOrganizationId(organizationId);
		List<SelectProjectVO> list = projectMemberService.getProjectListByPM(projectMember);
		if(CollectionUtils.isNotEmpty(list)){
			for(SelectProjectVO vo:list){
				projectIdList.add(vo.getProjectId());
			}
		}
		return projectIdList;
	}


}
