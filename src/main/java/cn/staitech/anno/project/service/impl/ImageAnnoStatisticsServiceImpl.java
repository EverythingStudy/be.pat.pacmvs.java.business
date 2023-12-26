package cn.staitech.anno.project.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.mapper.ProjectMemberMapper;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.service.ImageAnnoStatisticsService;
import cn.staitech.anno.project.vo.ImageAnnoStatisticsVO;
import cn.staitech.anno.project.vo.ImageAnnoUserQueryIn;
import cn.staitech.anno.project.vo.ProjectMemberQuery;
import cn.staitech.anno.project.vo.ProjectPartUserVO;
import cn.staitech.anno.project.vo.ProjectUserAnnoStatisticsVO;
import cn.staitech.anno.project.vo.SelectProjectVO;
import cn.staitech.anno.project.vo.SlideQueryIn;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.security.utils.SecurityUtils;

/**
 * @author 86186
 * @description 针对表【tb_slide(tb_slide)】的数据库操作Service实现
 * @createDate 2023-09-13 17:21:03
 */
@Service("SlideServiceImplV2")
public class ImageAnnoStatisticsServiceImpl extends ServiceImpl<SlideMapperV1, Slide>
implements ImageAnnoStatisticsService {

	@Resource
	private ProjectMemberMapper projectMemberMapper;

	@Resource
	private ProjectMemberService projectMemberService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PageResponse<ImageAnnoStatisticsVO> pageSlides(SlideQueryIn req){
		cn.staitech.common.core.domain.PageResponse resp = new cn.staitech.common.core.domain.PageResponse<>();

		ProjectMemberQuery partQuery = new ProjectMemberQuery();
		//初始化项目id和人员处理
		Long[] projectIds = req.getProjectIds();
		List<Long> pIds = new ArrayList<>();
		if(projectIds != null){
			pIds =  Arrays.asList(projectIds);
		}else{
			//赋值所有参与的项目
			cn.staitech.system.api.domain.SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
			Long userId = sysUser.getUserId();
			Long organizationId = sysUser.getOrganizationId();
			
	    	
			//查询自己参与的项目列表
			ProjectMember projectMember = new ProjectMember();
			projectMember.setUserId(userId);
			projectMember.setOrganizationId(organizationId);
			List<SelectProjectVO> list = projectMemberService.getProjectListByPM(projectMember);
			if(CollectionUtils.isNotEmpty(list)){
				for(SelectProjectVO p:list){
					pIds.add(p.getProjectId());
				}
			}
		}
		partQuery.setProjectIds(pIds);

		//参与人员
		Long[] partUsers = req.getPartUsers();
		List<Long> partUserList = new ArrayList<>();
		if (partUsers != null) {
			partUserList = Arrays.asList(partUsers);
		}else{
			ImageAnnoUserQueryIn query = new ImageAnnoUserQueryIn();
			query.setProjectId(null);
			List<ProjectPartUserVO> queryList = projectMemberService.getUserList(query);
			if(CollectionUtils.isNotEmpty(queryList)){
				for(ProjectPartUserVO p:queryList){
					partUserList.add(p.getUserId());
				}
			}
		}
		partQuery.setPartUsers(partUserList);
		
		com.github.pagehelper.Page<ImageAnnoStatisticsVO> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
		
		List<ImageAnnoStatisticsVO> dataList = projectMemberMapper.getImageCount(partQuery);
		
		if(CollectionUtils.isNotEmpty(dataList)){
			Map<String,Integer>  imageCountMap = new HashMap<>();
			Map<String,Integer>  annoCountMap = new HashMap<>();
			//查询项目人员标注图片数量和标注数量
			List<ProjectUserAnnoStatisticsVO> annCountList = projectMemberMapper.getProjectUserAnnoStatistics(partQuery);
			if(CollectionUtils.isNotEmpty(annCountList)){
				for(ProjectUserAnnoStatisticsVO vo:annCountList){
					Long projectId = vo.getProjectId();
					Long userId = vo.getCreateBy();
					String key = projectId+"_"+userId;
					int imageCount = vo.getImageCount();
					int annoCount = vo.getAnnoCount();
					imageCountMap.put(key, imageCount);
					annoCountMap.put(key, annoCount);
				}
			}

			//赋值
			for(ImageAnnoStatisticsVO asvo:dataList){
				Long projectId = asvo.getProjectId();
				Long userId = asvo.getUserId();
				String key = projectId+"_"+userId;
				//切片数量处理
				if(null !=imageCountMap && !imageCountMap.isEmpty()){
					if(imageCountMap.containsKey(key)){
						asvo.setImageCount(imageCountMap.get(key));
					}else{
						asvo.setImageCount(0);
					}
				}else{
					asvo.setImageCount(0);
				}

				//标注数量处理
				if(null !=annoCountMap && !annoCountMap.isEmpty()){
					if(annoCountMap.containsKey(key)){
						asvo.setMarkingNum(annoCountMap.get(key));
					}else{
						asvo.setMarkingNum(0);
					}
				}else{
					asvo.setMarkingNum(0);
				}
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void slideAnnoStatisticsExport(SlideQueryIn req, HttpServletResponse response) throws Exception {
		ProjectMemberQuery partQuery = new ProjectMemberQuery();
		//初始化项目id和人员处理
		Long[] projectIds = req.getProjectIds();
		List<Long> pIds = new ArrayList<>();
		if(projectIds != null){
			pIds =  Arrays.asList(projectIds);
		}else{
			//赋值所有参与的项目
			cn.staitech.system.api.domain.SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
			Long userId = sysUser.getUserId();
			Long organizationId = sysUser.getOrganizationId();
			//查询自己参与的项目列表
			ProjectMember projectMember = new ProjectMember();
			projectMember.setUserId(userId);
			projectMember.setOrganizationId(organizationId);
			List<SelectProjectVO> list = projectMemberService.getProjectListByPM(projectMember);
			if(CollectionUtils.isNotEmpty(list)){
				for(SelectProjectVO p:list){
					pIds.add(p.getProjectId());
				}
			}
		}
		partQuery.setProjectIds(pIds);

		//参与人员
		Long[] partUsers = req.getPartUsers();
		List<Long> partUserList = new ArrayList<>();
		if (partUsers != null) {
			partUserList = Arrays.asList(partUsers);
		}else{
			ImageAnnoUserQueryIn query = new ImageAnnoUserQueryIn();
			query.setProjectId(null);
			List<ProjectPartUserVO> queryList = projectMemberService.getUserList(query);
			if(CollectionUtils.isNotEmpty(queryList)){
				for(ProjectPartUserVO p:queryList){
					partUserList.add(p.getUserId());
				}
			}
		}
		partQuery.setPartUsers(partUserList);
		
		
		List<ImageAnnoStatisticsVO> list = projectMemberMapper.getImageCount(partQuery);
		if(CollectionUtils.isNotEmpty(list)){
			Map<String,Integer>  imageCountMap = new HashMap<>();
			Map<String,Integer>  annoCountMap = new HashMap<>();
			//查询项目人员标注图片数量和标注数量
			List<ProjectUserAnnoStatisticsVO> annCountList = projectMemberMapper.getProjectUserAnnoStatistics(partQuery);
			if(CollectionUtils.isNotEmpty(annCountList)){
				for(ProjectUserAnnoStatisticsVO vo:annCountList){
					Long projectId = vo.getProjectId();
					Long userId = vo.getCreateBy();
					String key = projectId+"_"+userId;
					int imageCount = vo.getImageCount();
					int annoCount = vo.getAnnoCount();
					imageCountMap.put(key, imageCount);
					annoCountMap.put(key, annoCount);
				}
			}

			//赋值
			for(ImageAnnoStatisticsVO asvo:list){
				Long projectId = asvo.getProjectId();
				Long userId = asvo.getUserId();
				String key = projectId+"_"+userId;
				//切片数量处理
				if(null !=imageCountMap && !imageCountMap.isEmpty()){
					if(imageCountMap.containsKey(key)){
						asvo.setImageCount(imageCountMap.get(key));
					}else{
						asvo.setImageCount(0);
					}
				}else{
					asvo.setImageCount(0);
				}

				//标注数量处理
				if(null !=annoCountMap && !annoCountMap.isEmpty()){
					if(annoCountMap.containsKey(key)){
						asvo.setMarkingNum(annoCountMap.get(key));
					}else{
						asvo.setMarkingNum(0);
					}
				}else{
					asvo.setMarkingNum(0);
				}
			}
		}
		
		// 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(CommonConstant.SLIDE_COUNT_COLHEAD_KEY, CommonConstant.SLIDE_COUNT_COLHEAD_VALUE);
        ExcelTool excelTool = new ExcelTool<>("图像标注统计导出", 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("图像标注统计", "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
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
}

