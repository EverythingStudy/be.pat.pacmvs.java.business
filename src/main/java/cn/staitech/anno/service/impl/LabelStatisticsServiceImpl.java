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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.ProjectLabelStatistics;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.ProjectStatistics;
import cn.staitech.anno.mapper.LabelStatisticsMapper;
import cn.staitech.anno.project.vo.SelectProjectVO;
import cn.staitech.anno.service.LabelStatisticsService;
import cn.staitech.anno.service.ProjectInStatisticsService;
import cn.staitech.anno.service.ProjectLabelStatisticsService;
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
	private ProjectLabelStatisticsService projectLabelStatisticsService;
	@Resource
	private ProjectInStatisticsService projectStatisticsService;
	@Resource
	private ProjectMemberService projectMemberService;
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
		projectLabelIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		/*projectLabelIn.setUserId(SecurityUtils.getUserId());
        ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").build();
        List<ProjectListOut> projectList = labelStatisticsMapper.projectList(projectInVO);
        //项目id列表
        List<Long> projectIdList = projectList.stream().map(ProjectListOut::getProjectId).collect(Collectors.toList());
        LabelIn labelIn = LabelIn.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).build();
        List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
        //标签id列表
        List<Long> categoryIdList = labelOuts.stream().map(LabelOut::getCategoryId).collect(Collectors.toList());
        categoryIdList.add(0L);

        PageHelper.startPage(projectLabelIn.getPageNum(), projectLabelIn.getPageSize()).setReasonable(true);
        List<ProjectLabelOut> projectLabelOuts = labelStatisticsMapper.projectLabelList(projectLabelIn);
        //查询标注数
        ImageMarkingIn imageMarkingIn = ImageMarkingIn.builder().projectIds(projectIdList).categoryIds(categoryIdList).build();
        List<ImageMarkingOut> markingNums = labelStatisticsMapper.markingNums(imageMarkingIn);
        Map<String,ImageMarkingOut>markingNumsMap=markingNums.stream().collect(Collectors.toMap(ImageMarkingOut::getNum,Function.identity()));
        //查询图像数量
        List<ImageMarkingOut> imageNums = labelStatisticsMapper.imageNums(imageMarkingIn);
        Map<String,ImageMarkingOut>imageNumsMap=imageNums.stream().collect(Collectors.toMap(ImageMarkingOut::getNum,Function.identity()));
        for (ProjectLabelOut projectLabelOut : projectLabelOuts) {
            projectLabelOut.setMarkingNum("0");
            projectLabelOut.setImageNum("0");
            //添加标注数量
            if (markingNumsMap.containsKey(projectLabelOut.getNum())){
                projectLabelOut.setMarkingNum(markingNumsMap.get(projectLabelOut.getNum()).getMarkingNum().toString());
            }
            //添加图像数量
            if (imageNumsMap.containsKey(projectLabelOut.getNum())){
                projectLabelOut.setImageNum(imageNumsMap.get(projectLabelOut.getNum()).getImageNum().toString());
            }
        }*/
		List<Long> projectIds = new ArrayList<>();
		projectIds = projectLabelIn.getProjectIds();
		List<Long> indicatorIds = projectLabelIn.getIndicatorIds();
		List<Long> categoryIds = projectLabelIn.getCategoryIds();

		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getUserId();

		if(CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		//查询
		QueryWrapper<ProjectLabelStatistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("project_id, project_name,project_status,description,project_create_by,create_nick_name,project_create_time,indicator_id,indicator_name,category_id,category_name,image_num,marking_num");
		if(CollectionUtils.isNotEmpty(projectIds)){
			queryWrapper.in("project_id", projectIds);
		}

		if(CollectionUtils.isNotEmpty(indicatorIds)){
			queryWrapper.in("indicator_id", indicatorIds);
		}

		if(CollectionUtils.isNotEmpty(categoryIds)){
			queryWrapper.in("category_id", categoryIds);
		}



		/*if(CollectionUtils.isNotEmpty(userIds)){
        	queryWrapper.in("project_create_by", userIds);
        }*/


		queryWrapper.eq("organization_id", organizationId);
		queryWrapper.eq("del_flag", 0);


		List<ProjectLabelStatistics> list = projectLabelStatisticsService.list(queryWrapper);
		List<ProjectLabelOut> retList = new ArrayList<>(); 
		for (ProjectLabelStatistics statistics : list) {
			ProjectLabelOut projectLabelOut = new ProjectLabelOut();
			BeanUtils.copyProperties(statistics, projectLabelOut);
			retList.add(projectLabelOut);
		}
		PageMaster<ProjectLabelOut> pageMaster = new PageMaster<>(retList);
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
		PageHelper.startPage(projectListIn.getPageNum(), projectListIn.getPageSize()).setReasonable(true);
		/*projectListIn.setProjectType("1");
        projectListIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        projectListIn.setUsers(SecurityUtils.getUserId());
        List<ProjectLabelOut> itemList = labelStatisticsMapper.itemList(projectListIn);
        List<Long> projectIds=itemList.stream().map(ProjectLabelOut::getProjectId).collect(Collectors.toList());
        ImageMarkingIn imageMarkingIns= ImageMarkingIn.builder().projectIds(projectIds).build();
        List<ImageMarkingOut> listSlideNum=labelStatisticsMapper.listSlideNum(imageMarkingIns);
        Map<Long,ImageMarkingOut>listSlideMap=listSlideNum.stream().collect(Collectors.toMap(ImageMarkingOut::getProjectId,Function.identity()));
        ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").build();
        List<ProjectListOut> projectList = labelStatisticsMapper.projectList(projectInVO);
        //项目id列表
        List<Long> projectIdList = projectList.stream().map(ProjectListOut::getProjectId).collect(Collectors.toList());
        //
        ImageMarkingIn imageMarkingIn = ImageMarkingIn.builder().projectIds(projectIdList).build();
        List<ImageMarkingOut> marking = labelStatisticsMapper.projectMarking(imageMarkingIn);
        Map<Long,ImageMarkingOut> markingMap=marking.stream().collect(Collectors.toMap(ImageMarkingOut::getProjectId, Function.identity()));
        //循环添加图像数量和标注数
        for (ProjectLabelOut projectLabelOut : itemList) {
            projectLabelOut.setMarkingNum("0");
            projectLabelOut.setImageNum("0");
            if (markingMap.containsKey(projectLabelOut.getProjectId())){
                projectLabelOut.setMarkingNum(markingMap.get(projectLabelOut.getProjectId()).getMarkingNum().toString());
            }
            if (listSlideMap.containsKey(projectLabelOut.getProjectId())){
                projectLabelOut.setImageNum(listSlideMap.get(projectLabelOut.getProjectId()).getImageNum().toString());
            }
        }*/
		//        {"projectIds":[313,521,522,490],"statusList":["3","2"],"indicatorIds":[1145,1149],"userIds":[14],
		//        "description":"fsfsfsfsf","createTimeParams":{"beginTime":"2024-01-15","endTime":"2024-02-21"},"pageNum":1,"pageSize":10,"total":9}

		Long currentUserId = SecurityUtils.getUserId();
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();

		List<Long> projectIds = new ArrayList<>();

		projectIds = projectListIn.getProjectIds();
		List<Integer> statusList = projectListIn.getStatusList();
		List<Long> indicatorIds = projectListIn.getIndicatorIds();
		List<Long> userIds = projectListIn.getUserIds();
		String description = projectListIn.getDescription();
		Map<String, Object> createTimeParams = projectListIn.getCreateTimeParams();


		if(CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		//查询
		QueryWrapper<ProjectStatistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("project_id, project_name,project_status,description,project_create_by,create_nick_name,project_create_time,image_num,marking_num");
		if(CollectionUtils.isNotEmpty(projectIds)){
			queryWrapper.in("project_id", projectIds);
		}

		if(CollectionUtils.isNotEmpty(statusList)){
			queryWrapper.in("project_status", statusList);
		}

		if(CollectionUtils.isNotEmpty(indicatorIds)){
			queryWrapper.in("indicator_id", indicatorIds);
		}

		if(CollectionUtils.isNotEmpty(userIds)){
			queryWrapper.in("project_create_by", userIds);
		}

		if(StringUtils.isNotEmpty(description)){
			queryWrapper.like("description", description);
		}

		if(!createTimeParams.isEmpty() && createTimeParams.containsKey("beginTime")&& createTimeParams.containsKey("endTime")){
			queryWrapper.between("project_create_time", createTimeParams.get("beginTime"), createTimeParams.get("endTime"));
		}

		queryWrapper.eq("organization_id", organizationId);
		queryWrapper.eq("del_flag", 0);


		List<ProjectStatistics> list = projectStatisticsService.list(queryWrapper);
		List<ProjectLabelOut> retList = new ArrayList<>(); 
		for (ProjectStatistics statistics : list) {
			ProjectLabelOut projectLabelOut = new ProjectLabelOut();
			BeanUtils.copyProperties(statistics, projectLabelOut);
			retList.add(projectLabelOut);
		}
		PageMaster<ProjectLabelOut> pageMaster = new PageMaster<>(retList);
		return R.ok(pageMaster);
	}


	/**
	 * 标签导出
	 */
	@Override
	public void labelExport(ProjectLabelIn projectLabelIn, HttpServletResponse response) throws Exception {
		projectLabelIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		projectLabelIn.setUserId(SecurityUtils.getUserId());
		ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").build();
		/*List<ProjectListOut> projectList = labelStatisticsMapper.projectList(projectInVO);
        //项目id列表
        List<Long> projectIdList = projectList.stream().map(ProjectListOut::getProjectId).collect(Collectors.toList());
        LabelIn labelIn = LabelIn.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).build();
        List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
        //标签id列表
        List<Long> categoryIdList = labelOuts.stream().map(LabelOut::getCategoryId).collect(Collectors.toList());
        categoryIdList.add(0L);
        List<ProjectLabelOut> projectLabelOuts = labelStatisticsMapper.projectLabelList(projectLabelIn);
        //查询标注数
        ImageMarkingIn imageMarkingIn = ImageMarkingIn.builder().projectIds(projectIdList).categoryIds(categoryIdList).build();
        List<ImageMarkingOut> markingNums = labelStatisticsMapper.markingNums(imageMarkingIn);
        Map<String,ImageMarkingOut>markingNumsMap=markingNums.stream().collect(Collectors.toMap(ImageMarkingOut::getNum,Function.identity()));
        //查询图像数量
        List<ImageMarkingOut> imageNums = labelStatisticsMapper.imageNums(imageMarkingIn);
        Map<String,ImageMarkingOut>imageNumsMap=imageNums.stream().collect(Collectors.toMap(ImageMarkingOut::getNum,Function.identity()));

        for (ProjectLabelOut projectLabelOut : projectLabelOuts) {
            projectLabelOut.setStatusName(Container.PROJECT_STATUS.get(projectLabelOut.getStatus()));
            projectLabelOut.setMarkingNum("0");
            projectLabelOut.setImageNum("0");
            //添加标注数量
            if (markingNumsMap.containsKey(projectLabelOut.getNum())){
                projectLabelOut.setMarkingNum(markingNumsMap.get(projectLabelOut.getNum()).getMarkingNum().toString());
            }
            //添加图像数量
            if (imageNumsMap.containsKey(projectLabelOut.getNum())){
                projectLabelOut.setImageNum(imageNumsMap.get(projectLabelOut.getNum()).getImageNum().toString());
            }
        }*/
		List<Long> projectIds = new ArrayList<>();
		projectIds = projectLabelIn.getProjectIds();
		projectIds = projectLabelIn.getProjectIds();
		List<Long> indicatorIds = projectLabelIn.getIndicatorIds();
		List<Long> categoryIds = projectLabelIn.getCategoryIds();

		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getUserId();

		if(CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}


		//查询
		QueryWrapper<ProjectLabelStatistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("project_id, project_name,project_status,description,project_create_by,create_nick_name,project_create_time,indicator_id,indicator_name,category_id,category_name,image_num,marking_num");
		if(CollectionUtils.isNotEmpty(projectIds)){
			queryWrapper.in("project_id", projectIds);
		}

		if(CollectionUtils.isNotEmpty(indicatorIds)){
			queryWrapper.in("indicator_id", indicatorIds);
		}

		if(CollectionUtils.isNotEmpty(categoryIds)){
			queryWrapper.in("category_id", categoryIds);
		}



		/*if(CollectionUtils.isNotEmpty(userIds)){
        	queryWrapper.in("project_create_by", userIds);
        }*/


		queryWrapper.eq("organization_id", organizationId);
		queryWrapper.eq("del_flag", 0);


		List<ProjectLabelStatistics> list = projectLabelStatisticsService.list(queryWrapper);
		List<ProjectLabelOut> retList = new ArrayList<>(); 
		for (ProjectLabelStatistics statistics : list) {
			ProjectLabelOut projectLabelOut = new ProjectLabelOut();
			BeanUtils.copyProperties(statistics, projectLabelOut);
			retList.add(projectLabelOut);
		}
		List<Map<String, String>> titleList = getTitleList(CommonConstant.LABEL_STATISTICS_KEY, CommonConstant.LABEL_STATISTICS_VALUE);
		ExcelTool excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
		List<Column> titleData = excelTool.columnTransformer(titleList);
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
		//        excelTool.exportExcel(titleData, projectLabelOuts, response.getOutputStream(), true, false);
		excelTool.exportExcel(titleData, retList, response.getOutputStream(), true, false);

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
		/*List<ProjectLabelOut> itemList = labelStatisticsMapper.itemList(projectListIn);
        List<Long> projectIds=itemList.stream().map(ProjectLabelOut::getProjectId).collect(Collectors.toList());
        ImageMarkingIn imageMarkingIns= ImageMarkingIn.builder().projectIds(projectIds).build();
        List<ImageMarkingOut> listSlideNum=labelStatisticsMapper.listSlideNum(imageMarkingIns);
        Map<Long,ImageMarkingOut>listSlideMap=listSlideNum.stream().collect(Collectors.toMap(ImageMarkingOut::getProjectId,Function.identity()));
        ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").build();
        List<ProjectListOut> projectList = labelStatisticsMapper.projectList(projectInVO);
        //项目id列表
        List<Long> projectIdList = projectList.stream().map(ProjectListOut::getProjectId).collect(Collectors.toList());
        //
        ImageMarkingIn imageMarkingIn = ImageMarkingIn.builder().projectIds(projectIdList).build();
        List<ImageMarkingOut> marking = labelStatisticsMapper.projectMarking(imageMarkingIn);
        Map<Long,ImageMarkingOut> markingMap=marking.stream().collect(Collectors.toMap(ImageMarkingOut::getProjectId, Function.identity()));
        //循环添加图像数量和标注数
        for (ProjectLabelOut projectLabelOut : itemList) {
            projectLabelOut.setMarkingNum("0");
            projectLabelOut.setImageNum("0");
            if (markingMap.containsKey(projectLabelOut.getProjectId())){
                projectLabelOut.setMarkingNum(markingMap.get(projectLabelOut.getProjectId()).getMarkingNum().toString());
            }
            if (listSlideMap.containsKey(projectLabelOut.getProjectId())){
                projectLabelOut.setImageNum(listSlideMap.get(projectLabelOut.getProjectId()).getImageNum().toString());
            }
            projectLabelOut.setStatusName(Container.PROJECT_STATUS.get(projectLabelOut.getStatus()));
        }*/
		List<ProjectLabelOut> itemList = new ArrayList<>();

		List<Long> projectIds = new ArrayList<>();
		projectIds = projectListIn.getProjectIds();
		List<Integer> statusList = projectListIn.getStatusList();
		List<Long> indicatorIds = projectListIn.getIndicatorIds();
		List<Long> userIds = projectListIn.getUserIds();
		String description = projectListIn.getDescription();
		Map<String, Object> createTimeParams = projectListIn.getCreateTimeParams();

		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getUserId();

		if(CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		//查询
		QueryWrapper<ProjectStatistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("project_id, project_name,project_status,description,project_create_by,create_nick_name,project_create_time,image_num,marking_num");
		if(CollectionUtils.isNotEmpty(projectIds)){
			queryWrapper.in("project_id", projectIds);
		}

		if(CollectionUtils.isNotEmpty(statusList)){
			queryWrapper.in("project_status", statusList);
		}

		if(CollectionUtils.isNotEmpty(indicatorIds)){
			queryWrapper.in("indicator_id", indicatorIds);
		}

		if(CollectionUtils.isNotEmpty(userIds)){
			queryWrapper.in("project_create_by", userIds);
		}

		if(StringUtils.isNotEmpty(description)){
			queryWrapper.like("description", description);
		}

		if(!createTimeParams.isEmpty() && createTimeParams.containsKey("beginTime")&& createTimeParams.containsKey("endTime")){
			queryWrapper.between("project_create_time", createTimeParams.get("beginTime"), createTimeParams.get("endTime"));
		}

		queryWrapper.eq("organization_id", organizationId);
		queryWrapper.eq("del_flag", 0);


		List<ProjectStatistics> list = projectStatisticsService.list(queryWrapper);
		for (ProjectStatistics statistics : list) {
			ProjectLabelOut projectLabelOut = new ProjectLabelOut();
			BeanUtils.copyProperties(statistics, projectLabelOut);
			itemList.add(projectLabelOut);
		}

		// 构造表头的每个列头 定义表头
		List<Map<String, String>> titleList = getTitleList(CommonConstant.PROJECT_STATISTICS_KEY, CommonConstant.PROJECT_STATISTICS_VALUE);
		ExcelTool excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
		List<Column> titleData = excelTool.columnTransformer(titleList);
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
		excelTool.exportExcel(titleData, itemList, response.getOutputStream(), true, false);


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
