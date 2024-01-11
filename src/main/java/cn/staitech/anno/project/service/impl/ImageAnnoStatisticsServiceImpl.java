package cn.staitech.anno.project.service.impl;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.mapper.ProjectMemberMapper;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.service.ImageAnnoStatisticsService;
import cn.staitech.anno.project.vo.*;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.utils.DateUtils;
import cn.staitech.common.security.utils.SecurityUtils;
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public PageResponse<ImageAnnoStatisticsVO> pageSlides(SlideQueryIn req) {
        cn.staitech.common.core.domain.PageResponse resp = new cn.staitech.common.core.domain.PageResponse<>();

        ProjectMemberQuery partQuery = new ProjectMemberQuery();
        //初始化项目id和人员处理
        Long[] projectIds = req.getProjectIds();
        List<Long> pIds = new ArrayList<>();
        if (projectIds != null && projectIds.length > 0) {
            pIds = Arrays.asList(projectIds);
        } else {
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
        partQuery.setProjectIds(pIds);

        //参与人员
        Long[] partUsers = req.getCreateBys();
        List<Long> partUserList = new ArrayList<>();
        if (partUsers != null && partUsers.length > 0) {
            partUserList = Arrays.asList(partUsers);
        } else {
            ImageAnnoUserQueryIn query = new ImageAnnoUserQueryIn();
            Long[] projectIdArray = pIds.toArray(new Long[0]);
            query.setProjectIds(projectIdArray);
            List<ProjectPartUserVO> queryList = projectMemberService.getUserList(query);
            if (CollectionUtils.isNotEmpty(queryList)) {
                for (ProjectPartUserVO p : queryList) {
                    partUserList.add(p.getUserId());
                }
            }
        }
        partQuery.setPartUsers(partUserList);

        com.github.pagehelper.Page<ImageAnnoStatisticsVO> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());

        List<ImageAnnoStatisticsVO> dataList = projectMemberMapper.getImageCount(partQuery);

        if (CollectionUtils.isNotEmpty(dataList)) {
            Map<String, Integer> imageCountMap = new HashMap<>(16);
            Map<String, Integer> annoCountMap = new HashMap<>(16);

            //查询项目人员标注图片数量和标注数量
            List<ProjectUserAnnoStatisticsVO> annCountList = projectMemberMapper.getProjectUserAnnoStatistics1(partQuery);
            if (CollectionUtils.isNotEmpty(annCountList)) {
                for (ProjectUserAnnoStatisticsVO vo : annCountList) {
                    Long projectId = vo.getProjectId();
                    Long userId = vo.getCreateBy();
                    String key = projectId + "_" + userId;
                    Integer imageCount = 0;
                    if (null != vo.getImageCount()) {
                        imageCount = vo.getImageCount();
                    }
                    imageCountMap.put(key, imageCount);
                }
            }


            List<ProjectUserAnnoStatisticsVO> annCountList2 = projectMemberMapper.getProjectUserAnnoStatistics2(partQuery);
            if (CollectionUtils.isNotEmpty(annCountList2)) {
                for (ProjectUserAnnoStatisticsVO vo : annCountList2) {
                    Long projectId = vo.getProjectId();
                    Long userId = vo.getCreateBy();
                    String key = projectId + "_" + userId;
                    Integer annoCount = 0;
                    if (null != vo.getAnnoCount()) {
                        annoCount = vo.getAnnoCount();
                    }
                    annoCountMap.put(key, annoCount);
                }
            }

            //赋值
            for (ImageAnnoStatisticsVO asvo : dataList) {
                Long projectId = asvo.getProjectId();
                Long userId = asvo.getUserId();
                String key = projectId + "_" + userId;
                //切片数量处理
                if (null != imageCountMap && !imageCountMap.isEmpty()) {
                    if (imageCountMap.containsKey(key)) {
                        asvo.setImageCount(imageCountMap.get(key));
                    } else {
                        asvo.setImageCount(0);
                    }
                } else {
                    asvo.setImageCount(0);
                }

                //标注数量处理
                if (null != annoCountMap && !annoCountMap.isEmpty()) {
                    if (annoCountMap.containsKey(key)) {
                        asvo.setMarkingNum(annoCountMap.get(key));
                    } else {
                        asvo.setMarkingNum(0);
                    }
                } else {
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
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void slideAnnoStatisticsExport(SlideQueryIn req, HttpServletResponse response) throws Exception {
        ProjectMemberQuery partQuery = new ProjectMemberQuery();
        //初始化项目id和人员处理
        Long[] projectIds = req.getProjectIds();
        List<Long> pIds = new ArrayList<>();
        if (projectIds != null && projectIds.length > 0) {
            pIds = Arrays.asList(projectIds);
        } else {
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
        partQuery.setProjectIds(pIds);

        //参与人员
        Long[] partUsers = req.getCreateBys();
        List<Long> partUserList = new ArrayList<>();
        if (partUsers != null && partUsers.length > 0) {
            partUserList = Arrays.asList(partUsers);
        } else {
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
        partQuery.setPartUsers(partUserList);


        List<ImageAnnoStatisticsVO> list = projectMemberMapper.getImageCount(partQuery);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Integer> imageCountMap = new HashMap<>(16);
            Map<String, Integer> annoCountMap = new HashMap<>(16);
            //查询项目人员标注图片数量和标注数量
            List<ProjectUserAnnoStatisticsVO> annCountList = projectMemberMapper.getProjectUserAnnoStatistics1(partQuery);
            if (CollectionUtils.isNotEmpty(annCountList)) {
                for (ProjectUserAnnoStatisticsVO vo : annCountList) {
                    Long projectId = vo.getProjectId();
                    Long userId = vo.getCreateBy();
                    String key = projectId + "_" + userId;
                    Integer imageCount = 0;
                    if (null != vo.getImageCount()) {
                        imageCount = vo.getImageCount();
                    }
                    imageCountMap.put(key, imageCount);
                }
            }


            List<ProjectUserAnnoStatisticsVO> annCountList2 = projectMemberMapper.getProjectUserAnnoStatistics2(partQuery);
            if (CollectionUtils.isNotEmpty(annCountList2)) {
                for (ProjectUserAnnoStatisticsVO vo : annCountList2) {
                    Long projectId = vo.getProjectId();
                    Long userId = vo.getCreateBy();
                    String key = projectId + "_" + userId;
                    Integer annoCount = 0;
                    if (null != vo.getAnnoCount()) {
                        annoCount = vo.getAnnoCount();
                    }
                    annoCountMap.put(key, annoCount);
                }
            }

            //赋值
            for (ImageAnnoStatisticsVO asvo : list) {
                Long projectId = asvo.getProjectId();
                Long userId = asvo.getUserId();
                String key = projectId + "_" + userId;
                //切片数量处理
                if (null != imageCountMap && !imageCountMap.isEmpty()) {
                    if (imageCountMap.containsKey(key)) {
                        asvo.setImageCount(imageCountMap.get(key));
                    } else {
                        asvo.setImageCount(0);
                    }
                } else {
                    asvo.setImageCount(0);
                }

                //标注数量处理
                if (null != annoCountMap && !annoCountMap.isEmpty()) {
                    if (annoCountMap.containsKey(key)) {
                        asvo.setMarkingNum(annoCountMap.get(key));
                    } else {
                        asvo.setMarkingNum(0);
                    }
                } else {
                    asvo.setMarkingNum(0);
                }
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

