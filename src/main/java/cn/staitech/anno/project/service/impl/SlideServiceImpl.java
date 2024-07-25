package cn.staitech.anno.project.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.project.domain.*;
import cn.staitech.anno.project.mapper.*;
import cn.staitech.anno.project.service.SlideService;
import cn.staitech.anno.project.vo.*;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.model.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.Mark;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 86186
 * @description 针对表【tb_slide(tb_slide)】的数据库操作Service实现
 * @createDate 2023-09-13 17:21:03
 */
@Service("SlideServiceImplV1")
public class SlideServiceImpl extends ServiceImpl<SlideMapperV1, Slide>
        implements SlideService {
    @Resource
    private PathologicalIndicatorCategoryMapperV1 pathologicalIndicatorCategoryMapperV1;
    @Resource
    private SysUserMapperV1 sysUserMapperV1;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Resource
    private SysUserMapperV1 userMapperV1;

    @Resource
    private MarkingMapperV1 markingMapperV1;

    @Resource
    private ProjectMemberMapperV1 projectMemberMapperV1;

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private SlideMapperV1 slideMapperV1;

    @Resource
    private ProjectMapperV1 projectMapperV1;

    @Resource
    private ProjectService projectService;

    public void reviewHandle(List<Long> slideIds) {
        QueryWrapper<Review> queryWrapper = Wrappers.query();
        queryWrapper.in("slide_id", slideIds);
        queryWrapper.select("slide_id", "group_concat(score separator '-') as score").groupBy("slide_id");
        List<Map<String, Object>> reviewList = reviewMapper.selectMaps(queryWrapper);
        Map<Long, String> resp = new HashMap<>(16);
        reviewList.forEach(r -> {
            resp.put(Long.parseLong(r.get("slide_id").toString()), r.get("score").toString());
        });
    }

    @Override
    public PageMaster<SlideVO> pageSlides(Page page, SlideQueryIn params) throws Exception {
        getBaseMapper().pageSlides(page, params);
        List<SlideVO> list = page.getRecords();
        List<Long> slideIds = new ArrayList<>();
        Map<Long, SlideVO> map = new HashMap<>(16);
        if (list != null && !list.isEmpty()) {
            list.forEach(slideVO -> {
                slideIds.add(slideVO.getSlideId());
                map.put(slideVO.getSlideId(), slideVO);
            });
            List<Marking> annotationList = queryAnnotation(slideIds, params);
            if (!annotationList.isEmpty() && !map.isEmpty()) {
                handleAnnoList(annotationList, map);// TODO: wangfeng
            }
        }
        PageMaster<SlideVO> pageMaster = PageMaster.of(list);
        pageMaster.setTotal(page.getTotal());
        return pageMaster;
    }

    @Override
    public PageMaster<ReviewSlideVO> pageReviewSlide(Page page, ReviewSlideIn params) {
        //判断是否是项目管理员（22：项目管理所有权限）
        boolean isProjectAmin = isProjectAmin(SecurityUtils.getLoginUser(), params.getProjectId());
        if (!isProjectAmin) {
            params.setCreateBy(SecurityUtils.getUserId());
        }
        params.setCurrentUser(SecurityUtils.getUserId());
        getBaseMapper().pageReviewSlide(page, params);
        List<ReviewSlideVO> list = page.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ReviewSlideVO vo : list) {
                if (StringUtils.isEmpty(vo.getSelfReviewStatus())) {
                    vo.setSelfReviewStatus("1");
                }
                //TODO 分数处理
                String score = vo.getScore();
                if (StringUtils.isNotEmpty(score)) {
                    score = trans2Score(score);
                }
                vo.setScore(score);
            }
        }
        PageMaster<ReviewSlideVO> pageMaster = PageMaster.of(list);
        pageMaster.setTotal(page.getTotal());
        return pageMaster;
    }


    private String trans2Score(String score) {
        StringBuffer buffer = new StringBuffer();
        String[] scoreArray = score.split(":");
        for (int i = 0; i < scoreArray.length; i++) {
            String perScore = scoreArray[i];
            if (perScore.equals(" -1") || perScore.equals("-1")) {
                perScore = perScore.replaceAll("-1", CommonConstant.NOT_EVALUATING);
            }
            buffer.append(perScore).append(":");
        }
        String scoreStr = buffer.substring(0, buffer.length() - 1);
        return scoreStr;
    }

    private Integer getAnnoCount(Integer projectId) throws Exception {
        QueryWrapper<Slide> queryWrapperSlide = Wrappers.query();
        queryWrapperSlide.eq("project_id", projectId);
        queryWrapperSlide.in("status", Arrays.asList("7", "6"));
        queryWrapperSlide.select("slide_id");
        List<Slide> list = getBaseMapper().selectList(queryWrapperSlide);
        Integer count = 0;
        List<Long> slideIds = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            list.forEach(slideVO -> {
                slideIds.add(slideVO.getSlideId());
            });
            QueryWrapper<Marking> queryWrapper = Wrappers.query();
            queryWrapper.in("slide_id", slideIds);
            queryWrapper.eq("annotation_type", "Draw");
            count = markingMapperV1.selectCount(queryWrapper);
        }
        return count;
    }


    /**
     * 查看标注数目
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public List<SlideAnnoStatisticsVO> getSlideAnnoStatistics(SlideQueryIn params) throws Exception {
        List<SlideAnnoStatisticsVO> voList = new ArrayList<>();
        QueryWrapper<Marking> queryWrapper = Wrappers.query();
        queryWrapper.eq("annotation_type", "Draw");
        queryWrapper.eq("project_id", params.getProjectId());
        queryWrapper.select("slide_id", "category_id", "create_by");
        List<Marking> annotationList = markingMapperV1.selectList(queryWrapper);
        if (annotationList != null && !annotationList.isEmpty()) {
            voList.add(SlideAnnoStatisticsVO.builder().statisticsType(MessageSource.M("MAN_ANNOTATION")).result(annotationList.size()).build());
            voList.add(SlideAnnoStatisticsVO.builder().statisticsType(MessageSource.M("RECHECK_ANNOTATION")).result(getAnnoCount(params.getProjectId())).build());
            List<PathologicalIndicatorCategory> pathologicalIndicatorCategoryList = pathologicalIndicatorCategoryMapperV1.selectList(Wrappers.query());
            Map<Long, String> categoryMap = new HashMap<>(16);
            for (PathologicalIndicatorCategory c : pathologicalIndicatorCategoryList) {
                categoryMap.put(c.getCategoryId(), c.getCategoryName());
            }
            //按类别分组
            Map<Long, List<Marking>> categorys = annotationList.stream().collect(Collectors.groupingBy(Marking::getCategoryId));
            if (categorys != null && !categorys.isEmpty()) {
                for (Long key : categorys.keySet()) {
                    List<Marking> subs = categorys.get(key);
                    if (subs != null && !subs.isEmpty()) {
                        SlideAnnoStatisticsVO vo = null;
                        if (key == 0) {
                            vo = SlideAnnoStatisticsVO.builder().statisticsType(MessageSource.M("NO_ATTRIBUTE")).result(subs.size()).build();
                        } else {
                            vo = SlideAnnoStatisticsVO.builder().statisticsType(categoryMap.get(key)).result(subs.size()).build();
                        }
                        voList.add(vo);
                    }
                }
            }
        }
        return voList;
    }





    @Override
    public List<ProjectStatisticsOut> projectUserStatistics(ProjectStatisticsIn params) throws Exception{
        cn.staitech.anno.project.domain.Project project = projectMapperV1.selectById(params.getProjectId());
        QueryWrapper<PathologicalIndicatorCategory> pathologicalIndicatorCategoryQueryWrapper = new QueryWrapper<>();
        pathologicalIndicatorCategoryQueryWrapper.select("category_id","category_name");
        if(params.getCategoryList() != null && params.getCategoryList().size() > 0){
            pathologicalIndicatorCategoryQueryWrapper.in("category_id", params.getCategoryList());
        } else {
            pathologicalIndicatorCategoryQueryWrapper.eq("indicator_id", project.getIndicatorId());
        }
        List<PathologicalIndicatorCategory> pathologicalIndicatorCategoryList = pathologicalIndicatorCategoryMapperV1.selectList(pathologicalIndicatorCategoryQueryWrapper);
        List<Long> categoryList = pathologicalIndicatorCategoryList.stream().map(PathologicalIndicatorCategory::getCategoryId).collect(Collectors.toList());
        Map<Long,String> categoryNameMap = pathologicalIndicatorCategoryList.stream().collect(Collectors.toMap(PathologicalIndicatorCategory::getCategoryId,PathologicalIndicatorCategory::getCategoryName));
        QueryWrapper<ProjectMember> projectMemberQueryWrapper = new QueryWrapper<>();
        projectMemberQueryWrapper.select("user_id","project_id");
        if(params.getUserList() != null && params.getUserList().size() > 0){
            projectMemberQueryWrapper.in("user_id",params.getUserList());
        }else {
            projectMemberQueryWrapper.eq("project_id", params.getProjectId());
        }
        List<ProjectMember> projectMembers = projectMemberMapperV1.selectList(projectMemberQueryWrapper);
        List<Long> userList = projectMembers.stream().map(ProjectMember::getUserId).map(s -> (long) s).collect(Collectors.toList());
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.select("user_id","user_name").in("user_id", userList);
        List<SysUser> users = userMapperV1.selectList(sysUserQueryWrapper);
        Map<Long,String> userMap = users.stream().collect(Collectors.toMap(SysUser::getUserId,SysUser::getUserName));
        // 查询当前项目下所有的切片集合
        QueryWrapper<Slide> slideQueryWrapper = new QueryWrapper<>();
        slideQueryWrapper.eq("project_id",params.getProjectId());
        List<Slide> slides = slideMapperV1.selectList(slideQueryWrapper);
        List<Long> slideList = slides.stream().map(Slide::getSlideId).collect(Collectors.toList());
        // 拥有标注的标签集合
        Map<Long,Integer> categoryMap = new HashMap<>(16);
        // 循环当前标签
        for(Long categoryId:categoryList){
            List<Long> slideHaveMarkingList = new ArrayList<>();
            for(Long slideId:slideList){
                QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
                markingQueryWrapper.eq("category_id",categoryId).eq("slide_id",slideId).orderByDesc("create_time").last("LIMIT 1");
                Marking marking = markingMapperV1.selectOne(markingQueryWrapper);
                if(marking != null){
                    slideHaveMarkingList.add(marking.getSlideId());
                }
            }
            categoryMap.put(categoryId, slideHaveMarkingList.size());
        }
        Map<Long,Map<Long,Integer>> userSumMap = new HashMap<>();
        for(Long userId:userList){
            Map<Long,Integer> categoryCountMap = new HashMap<>();
            for(Long categoryId:categoryList){
                QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
                markingQueryWrapper.select("slide_id").eq("category_id",categoryId).eq("create_by",userId).eq("project_id",params.getProjectId());
                int count = markingMapperV1.selectCount(markingQueryWrapper);
                if(count > 0){
                    categoryCountMap.put(categoryId, count);
                }
            }
            userSumMap.put(userId,categoryCountMap);
        }
        Map<Long,Integer> categoryCountNumMap = new HashMap<>(16);
        for(Long categoryId:categoryList){
            int categoryCount = 0;
            for(Long userId:userList){
                if(userSumMap.get(userId).get(categoryId) != null){
                    categoryCount += userSumMap.get(userId).get(categoryId);
                }
            }
            categoryCountNumMap.put(categoryId, categoryCount);
        }
        List<ProjectStatisticsOut> projectStatisticsOuts = new ArrayList<>();
        for(Long categoryId:categoryList){
            int totalCount = categoryCountNumMap.get(categoryId);
            for(Long userId:userList){
                Integer categoryImageNum = categoryMap.get(categoryId);
                Integer markingCount = userSumMap.get(userId).get(categoryId);
                if(categoryImageNum > 0 && totalCount > 0 && markingCount != null && markingCount > 0){
                    ProjectStatisticsOut projectStatisticsOut = new ProjectStatisticsOut();
                    projectStatisticsOut.setUserName(userMap.get(userId));
                    projectStatisticsOut.setCategoryImageNum(categoryImageNum);
                    projectStatisticsOut.setCategoryName(categoryNameMap.get(categoryId));
                    projectStatisticsOut.setMarkingCount(markingCount);
                    projectStatisticsOut.setMarkingSum(totalCount);
                    projectStatisticsOuts.add(projectStatisticsOut);
                }
            }
        }
        return projectStatisticsOuts;
    }

    /**
     * 数据导出
     *
     * @param params
     * @return
     * @throws Exception
     */
    public void slideAnnoStatisticsExport(SlideQueryIn params) throws Exception {
        List<SlideExportVO> list = getBaseMapper().querySlides(params);
        Map<Long, SlideExportVO> map = new HashMap<>(16);
        List<Map<String, String>> catesMapList = new ArrayList<>();
        List<Long> slideIds = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            list.forEach(slideVO -> {
                slideIds.add(slideVO.getSlideId());
                map.put(slideVO.getSlideId(), slideVO);
            });
            List<Marking> annotationList = queryAnnotation(slideIds, params);
            List<PathologicalIndicatorCategory> columns = handleAnnoStatisticsExport(annotationList, map, catesMapList);
            // 通过hutool工具创建的excel的writer，默认为xls格式
            ExcelWriter writer = ExcelUtil.getWriter();
            writer.addHeaderAlias(MessageSource.M("IMAGE_ID"), MessageSource.M("IMAGE_ID"));
            writer.addHeaderAlias(MessageSource.M("IMAGE_NAME"), MessageSource.M("IMAGE_NAME"));
            writer.addHeaderAlias(MessageSource.M("ENTRY_NAME"), MessageSource.M("ENTRY_NAME"));
            writer.addHeaderAlias(MessageSource.M("IMAGE_DESCRIPTION"), MessageSource.M("IMAGE_DESCRIPTION"));
            writer.addHeaderAlias(MessageSource.M("TOTAL_NUMBER_OF_IMAGE_ANNOTATIONS"), MessageSource.M("TOTAL_NUMBER_OF_IMAGE_ANNOTATIONS"));

            // 自定义excel标题和列名
            columns.forEach(c -> {
                writer.addHeaderAlias(String.valueOf(c.getCategoryId()), c.getCategoryName());
            });
            writer.write(catesMapList, true);
            httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
            httpServletResponse.setHeader("responseType", "blob");
            //name是下载对话框的名称，不支持中文，想用中文名称需要进行utf8编码
            String excelName = MessageSource.M("SLIDE_DATA");
            excelName = URLEncoder.encode(excelName, "utf-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xls");
            ServletOutputStream excelOut = null;
            //将excel文件信息写入输出流，返回给调用者
            try {
                excelOut = httpServletResponse.getOutputStream();
                writer.flush(excelOut, true);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                writer.close();
            }
            IoUtil.close(excelOut);
        }
    }

    private List<PathologicalIndicatorCategory> handleAnnoStatisticsExport(List<Marking> annotationList, Map<Long, SlideExportVO> slideExportVOMap, List<Map<String, String>> catesMapList) throws Exception {
        List<PathologicalIndicatorCategory> columns = new ArrayList<>();
        Map<Long, Boolean> columnMap = new HashMap<>(16);
        List<PathologicalIndicatorCategory> pathologicalIndicatorCategoryList = pathologicalIndicatorCategoryMapperV1.selectList(Wrappers.query());
        Map<Long, PathologicalIndicatorCategory> categoryMap = new HashMap<>(16);
        for (PathologicalIndicatorCategory c : pathologicalIndicatorCategoryList) {
            categoryMap.put(c.getCategoryId(), c);
        }
        Map<Long, List<Marking>> map = new HashMap<>(16);
        if (annotationList != null && !annotationList.isEmpty()) {
            map = annotationList.stream().collect(Collectors.groupingBy(Marking::getSlideId));
        }
        for (Long slideKey : slideExportVOMap.keySet()) {
            /*for (Long slideKey : map.keySet()) {*/
            SlideExportVO vo = slideExportVOMap.get(slideKey);
            Map<String, String> catesMap = new HashMap<>(16);
            List<Marking> subs = map.get(slideKey);
            if (subs != null && !subs.isEmpty()) {
                Map<Long, List<Marking>> categorys = subs.stream().collect(Collectors.groupingBy(Marking::getCategoryId));
                if (categorys != null && !categorys.isEmpty()) {
                    for (Long categoryKey : categorys.keySet()) {
                        List<Marking> annoCateList = categorys.get(categoryKey);
                        if (annoCateList != null && !annoCateList.isEmpty()) {
                            catesMap.put(String.valueOf(categoryKey), String.valueOf(annoCateList.size()));
                            if (columnMap.get(categoryKey) == null || !columnMap.get(categoryKey)) {
                                columnMap.put(categoryKey, true);
                                columns.add(categoryMap.get(categoryKey));
                            }
                        }
                    }
                }
            }
            catesMap.put(MessageSource.M("IMAGE_ID"), String.valueOf(vo.getSlideId()));
            catesMap.put(MessageSource.M("IMAGE_NAME"), vo.getImageCode());
            catesMap.put(MessageSource.M("ENTRY_NAME"), vo.getProjectName());
            catesMap.put(MessageSource.M("IMAGE_DESCRIPTION"), vo.getRemark());
            catesMap.put(MessageSource.M("TOTAL_NUMBER_OF_IMAGE_ANNOTATIONS"), String.valueOf(subs == null ? 0 : subs.size()));
            vo.setCates(catesMap);
            vo.setManualAnnoCount(subs == null ? 0 : subs.size());
            catesMapList.add(catesMap);
        }

        for (Map<String, String> catesMap : catesMapList) {
            for (PathologicalIndicatorCategory column : columns) {
                if (null != catesMap && !catesMap.isEmpty()) {
                    if (null != column.getCategoryId()) {
                        if (catesMap.containsKey(String.valueOf(column.getCategoryId()))) {
                            String count = catesMap.get(String.valueOf(column.getCategoryId()));
                            if (count == null) {
                                catesMap.put(String.valueOf(column.getCategoryId()), "");
                            }
                        } else {
                            catesMap.put(String.valueOf(column.getCategoryId()), "");
                        }
                    }
                }
            }
        }


        return columns;
    }


    private List<Marking> queryAnnotation(List<Long> slideIds, SlideQueryIn params) throws Exception {
        QueryWrapper<Marking> queryWrapper = Wrappers.query();
//        queryWrapper.eq("annotation_type", "Draw");
        queryWrapper.ne("annotation_type", "Measure");
        queryWrapper.eq("project_id", params.getProjectId());
//        if (slideIds != null) {
        if (CollectionUtils.isNotEmpty(slideIds)) {
            queryWrapper.in("slide_id", slideIds);
        }
        if (params.getAnnoCategory() != null) {
            queryWrapper.eq("category_id", params.getAnnoCategory());
        }
        if (params.getAnnoUser() != null) {
            queryWrapper.eq("create_by", params.getAnnoUser());
        }
        queryWrapper.select("slide_id", "category_id", "create_by");
        return markingMapperV1.selectList(queryWrapper);
    }

    private void handleAnnoList(List<Marking> annotationList, Map<Long, SlideVO> slideVOMap) throws Exception {
        if (annotationList != null && !annotationList.isEmpty()) {
            List<PathologicalIndicatorCategory> pathologicalIndicatorCategoryList = pathologicalIndicatorCategoryMapperV1.selectList(Wrappers.query());
            Map<Long, String> categoryMap = new HashMap<>(16);
            for (PathologicalIndicatorCategory c : pathologicalIndicatorCategoryList) {
                categoryMap.put(c.getCategoryId(), c.getCategoryName());
            }
            List<SysUser> userList = sysUserMapperV1.selectList(Wrappers.query());
            Map<Long, String> userMap = new HashMap<>(16);
            for (SysUser u : userList) {
                userMap.put(u.getUserId(), u.getUserName());
            }
            Map<Long, List<Marking>> map = annotationList.stream().collect(Collectors.groupingBy(Marking::getSlideId));
            for (Long key : map.keySet()) {
                SlideVO vo = slideVOMap.get(key);
                List<Marking> subs = map.get(key);
                if (subs != null && !subs.isEmpty()) {
                    Map<Long, List<Marking>> categorys = subs.stream().collect(Collectors.groupingBy(Marking::getCategoryId));
                    if (categorys != null && !categoryMap.isEmpty()) {
                        vo.setCategoryTypes(handleCategorys(categorys, categoryMap));//TODO
                    } else {
                        vo.setCategoryTypes("");
                    }
                    Map<Long, List<Marking>> users = subs.stream().collect(Collectors.groupingBy(Marking::getCreateBy));
                    vo.setManualAnnoDetails(handleUsers(users, userMap));
                }
                vo.setManualAnnoCount(subs.size());

            }
        }
    }

    private String handleCategorys(Map<Long, List<Marking>> categorys, Map<Long, String> categoryMap) throws Exception {
        String resp = "";
        if (categorys != null && !categorys.isEmpty()) {
            int i = 0;
            for (Long key : categorys.keySet()) {
                List<Marking> subs = categorys.get(key);
                if (subs != null && !subs.isEmpty()) {
                    if (i == 0) {
                        resp += (categoryMap.get(key) + "(" + subs.size() + ")");
                    } else {
                        resp += ("," + categoryMap.get(key) + "(" + subs.size() + ")");
                    }
                }
                i++;
            }
        }
        return resp;
    }

    private String handleUsers(Map<Long, List<Marking>> users, Map<Long, String> userMap) {
        String resp = "";
        if (users != null && !users.isEmpty()) {
            int i = 0;
            for (Long key : users.keySet()) {
                List<Marking> subs = users.get(key);
                if (subs != null && !subs.isEmpty()) {
                    if (i == 0) {
                        resp += (userMap.get(key) + "(" + subs.size() + ")");
                    } else {
                        resp += ("," + userMap.get(key) + "(" + subs.size() + ")");
                    }
                }
                i++;
            }
        }
        return resp;
    }

    @Override
    public boolean isProjectAmin(LoginUser user, Long projectId) {
        boolean isProjectAmin = false;
        Long userId = user.getUserid();

        //项目创建者就是项目管理员（不根据系统角色去判断）
        cn.staitech.anno.domain.Project project = projectService.selectPrimKey(projectId);
        if (userId.equals(project.getCreateBy())) {
            isProjectAmin = true;
        }
        return isProjectAmin;
    }
}

