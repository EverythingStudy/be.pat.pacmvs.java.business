package cn.staitech.anno.project.service.impl;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.ProjectAnnoStatistics;
import cn.staitech.anno.domain.ProjectLabelStatistics;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.mapper.ProjectAnnoStatisticsMapper;
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

	@Resource
	private ProjectAnnoStatisticsMapper projectAnnoStatisticsMapper;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public PageResponse<ImageAnnoStatisticsVO> pageSlides(SlideQueryIn req) {
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Long currentUserId = SecurityUtils.getUserId();

		cn.staitech.common.core.domain.PageResponse resp = new cn.staitech.common.core.domain.PageResponse<>();

		List<Long> projectIds = new ArrayList<>();
		projectIds = req.getProjectIds();


		if(null == projectIds || CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}


		List<Long> createBys = new ArrayList<>();
		createBys = req.getCreateBys();


		// 测试 project_id 551);
		ProjectAnnoStatistics queryStatisticsVO = new ProjectAnnoStatistics();


		queryStatisticsVO.setProjectIdList(projectIds);

		if(CollectionUtils.isNotEmpty(createBys)){
			queryStatisticsVO.setProjectAnnoUseridList(createBys);

		}
		queryStatisticsVO.setDelFlag("0");
		queryStatisticsVO.setOrganizationId(organizationId);

		com.github.pagehelper.Page<ProjectAnnoStatistics> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
		List<ImageAnnoStatisticsVO> dataList = projectAnnoStatisticsMapper.getProjectAnnoStatistics(queryStatisticsVO);

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

		cn.staitech.system.api.domain.SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
		Long currentUserId = sysUser.getUserId();
		Long organizationId = sysUser.getOrganizationId();

		List<Long> projectIds = new ArrayList<>();
		projectIds = req.getProjectIds();


		if(null == projectIds || CollectionUtils.isEmpty(projectIds)){
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		List<Long> createBys = new ArrayList<>();
		createBys = req.getCreateBys();

		// 测试 project_id 551);
		ProjectAnnoStatistics queryStatisticsVO = new ProjectAnnoStatistics();

		if(CollectionUtils.isNotEmpty(projectIds)){
			queryStatisticsVO.setProjectIdList(projectIds);
		}

		if(CollectionUtils.isNotEmpty(createBys)){
			queryStatisticsVO.setProjectAnnoUseridList(createBys);

		}
		queryStatisticsVO.setDelFlag("0");
		queryStatisticsVO.setOrganizationId(organizationId);

		List<ImageAnnoStatisticsVO> dataList = projectAnnoStatisticsMapper.getProjectAnnoStatistics(queryStatisticsVO);

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

		excelTool.exportExcel(titleData, dataList, response.getOutputStream(), true, false);

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

