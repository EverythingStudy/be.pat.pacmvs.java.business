package cn.staitech.anno.project.service.impl;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.ProjectAnnoStatistics;
import cn.staitech.anno.domain.ProjectLabelStatistics;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.mapper.ProjectMemberMapper;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.service.ImageAnnoStatisticsService;
import cn.staitech.anno.project.vo.*;
import cn.staitech.anno.service.ProjectAnnoStatisticsService;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.utils.DateUtils;
import cn.staitech.common.security.utils.SecurityUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author 86186
 * @description 针对表【tb_slide(tb_slide)】的数据库操作Service实现
 * @createDate 2023-09-13 17:21:03
 */
@Slf4j
@Service("SlideServiceImplV2")
public class ImageAnnoStatisticsServiceImpl extends ServiceImpl<SlideMapperV1, Slide>
implements ImageAnnoStatisticsService {

	@Resource
	private ProjectMemberMapper projectMemberMapper;

	@Resource
	private ProjectMemberService projectMemberService;

	@Resource
	private ProjectAnnoStatisticsService projectAnnoStatisticsService;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public PageResponse<ImageAnnoStatisticsVO> pageSlides(SlideQueryIn req) {
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getUserId();
		

		cn.staitech.common.core.domain.PageResponse resp = new cn.staitech.common.core.domain.PageResponse<>();
		com.github.pagehelper.Page<ImageAnnoStatisticsVO> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());

		List<Long> projectIds = new ArrayList<>();
		projectIds = req.getProjectIds();


		if(null == projectIds || CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}


		List<Long> createBys = new ArrayList<>();
		createBys = req.getCreateBys();


		QueryWrapper<ProjectAnnoStatistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("project_id, project_name,project_status,anno_user_id,anno_nick_name,image_count,marking_num");
		if(CollectionUtils.isNotEmpty(projectIds)){
			queryWrapper.in("project_id", projectIds);
		}

		if(CollectionUtils.isNotEmpty(createBys)){
			queryWrapper.in("anno_user_id", createBys);
		}
		queryWrapper.eq("del_flag", 0);
		queryWrapper.orderByDesc("marking_num");
		// 测试 queryWrapper.eq("project_id", 551);
		List<ProjectAnnoStatistics> dataList = projectAnnoStatisticsService.list(queryWrapper);
		List<ImageAnnoStatisticsVO> retList = new ArrayList<>(); 
		if (CollectionUtils.isNotEmpty(dataList)) {
			for (ProjectAnnoStatistics statistics : dataList) {
				ImageAnnoStatisticsVO statisticsVO = new ImageAnnoStatisticsVO();
				statisticsVO.setProjectName(statistics.getProjectName());
				statisticsVO.setNickName(statistics.getAnnoNickName());
				statisticsVO.setImageCount(statistics.getImageCount().intValue());
				statisticsVO.setImageCount(statistics.getMarkingNum().intValue());
				statisticsVO.setStatus(String.valueOf(statistics.getProjectStatus()));
				retList.add(statisticsVO);
			}
		}

		resp.setTotal(page.getTotal());
		resp.setPages(page.getPages());
		resp.setList(dataList);
		resp.setPageNum(req.getPageNum());
		resp.setPageSize(req.getPageSize());
		return resp;
	}


	/**
	 * 数据导出
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void slideAnnoStatisticsExport(SlideQueryIn req, HttpServletResponse response) throws Exception {
		QueryWrapper<ProjectAnnoStatistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("project_id, project_name,project_status,anno_user_id,anno_nick_name,image_count,marking_num");

		//初始化项目id和人员处理
		List<Long> pIds = new ArrayList<>();
		if (null == pIds || CollectionUtils.isEmpty(pIds)){
			//赋值所有参与的项目
			cn.staitech.system.api.domain.SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
			Long userId = sysUser.getUserId();
			Long organizationId = sysUser.getOrganizationId();

			//查询自己参与的项目列表
			ProjectMember projectMember = new ProjectMember();
			projectMember.setUserId(userId);
			projectMember.setOrganizationId(organizationId);
			List<SelectProjectVO> list = projectMemberService.getProjectListByPM(projectMember);
			if (CollectionUtils.isNotEmpty(list)) {
				for (SelectProjectVO p : list) {
					pIds.add(p.getProjectId());
				}
			}
		}
		
		queryWrapper.in("project_id", pIds);

		//参与人员
		/*List<Long> partUserList = req.getCreateBys();
		if (null == partUserList || CollectionUtils.isEmpty(partUserList)) {
			ImageAnnoUserQueryIn query = new ImageAnnoUserQueryIn();
			//赋值项目id
			Long[] projectIdArray = pIds.toArray(new Long[0]);
			query.setProjectIds(projectIdArray);
			List<ProjectPartUserVO> queryList = projectMemberService.getUserList(query);
			if (CollectionUtils.isNotEmpty(queryList)) {
				for (ProjectPartUserVO p : queryList) {
					partUserList.add(p.getUserId());
				}
			}
		}
		queryWrapper.in("anno_user_id", partUserList);*/

		queryWrapper.eq("del_flag", 0);
		queryWrapper.orderByDesc("marking_num");
		List<ProjectAnnoStatistics> queryList =  projectAnnoStatisticsService.list(queryWrapper);
		
		List<ImageAnnoStatisticsVO> retList = new ArrayList<ImageAnnoStatisticsVO>();
		if(CollectionUtils.isNotEmpty(queryList)){
			for(ProjectAnnoStatistics annoStatistics:queryList){
				ImageAnnoStatisticsVO statisticsVO = new ImageAnnoStatisticsVO();
				statisticsVO.setProjectName(annoStatistics.getProjectName());
				statisticsVO.setNickName(annoStatistics.getAnnoNickName());
				statisticsVO.setImageCount(annoStatistics.getImageCount().intValue());
				statisticsVO.setMarkingNum(annoStatistics.getMarkingNum().intValue());
				statisticsVO.setStatus(String.valueOf(annoStatistics.getProjectStatus()));
				statisticsVO.setStatusDesc(Container.PROJECT_STATUS.get(annoStatistics.getProjectStatus()));
				retList.add(statisticsVO);
			}
		}
		// 构造表头的每个列头 定义表头
		List<Map<String, String>> titleList = getTitleList(CommonConstant.SLIDE_COUNT_COLHEAD_KEY, CommonConstant.SLIDE_COUNT_COLHEAD_VALUE);
		ExcelTool excelTool = new ExcelTool(CommonConstant.SLIDE_COUNT_DATA_SEARCH_TITLE, 20, 20);
		List<Column> titleData = excelTool.columnTransformer(titleList);
		String currentTime = DateUtils.parseDateToStr("yyyyMMddHHmm", new Date());
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(CommonConstant.SLIDE_COUNT_DATA_SEARCH_TITLE, "UTF-8") + currentTime + ".xlsx");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "text/html;charset=utf-8");
		response.setContentType("text/html;charset=utf-8");

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

