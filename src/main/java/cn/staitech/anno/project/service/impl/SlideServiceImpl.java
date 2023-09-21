package cn.staitech.anno.project.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.staitech.anno.project.domain.*;
import cn.staitech.anno.project.mapper.*;
import cn.staitech.anno.project.vo.SlideAnnoStatisticsVO;
import cn.staitech.anno.project.vo.SlideExportVO;
import cn.staitech.anno.project.vo.SlideQueryIN;
import cn.staitech.anno.project.vo.SlideVO;
import cn.staitech.anno.utils.PageMaster;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.staitech.anno.project.service.SlideService;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private MarkingMapperV1 markingMapperV1;

    @Resource
    private ReviewMapper reviewMapper;

    private static final String col1 = "图像id";
    private static final String col2 = "图像名称";
    private static final String col3 = "项目名称";
    private static final String col4 = "图像描述";
    private static final String col5 = "图像标注总数";
    private static final String col6 = "无属性数量";

    public void reviewHandle(List<Long> slideIds){
        QueryWrapper<Review> queryWrapper = Wrappers.query();
        queryWrapper.in("slide_id",slideIds);
        queryWrapper.select("slide_id", "group_concat(score separator '-') as score").groupBy("slide_id");
        List<Map<String, Object>> reviewList = reviewMapper.selectMaps(queryWrapper);
        Map<Long,String> resp = new HashMap<>();
        reviewList.forEach(r->{
            resp.put(Long.parseLong(r.get("slide_id").toString()),r.get("score").toString());
        });
    }

    @Override
    public PageMaster<SlideVO> pageSlides(Page page, SlideQueryIN params) throws Exception {
        getBaseMapper().pageSlides(page, params);
        List<SlideVO> list = page.getRecords();
        List<Long> slideIds = new ArrayList<>();
        Map<Long, SlideVO> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            list.forEach(slideVO -> {
                slideIds.add(slideVO.getSlideId());
                map.put(slideVO.getSlideId(), slideVO);
            });
            List<Marking> annotationList = queryAnnotation(slideIds, params);
            handleAnnoList(annotationList, map);
        }
        PageMaster<SlideVO> pageMaster = PageMaster.of(list);
        pageMaster.setTotal(page.getTotal());
        return pageMaster;
    }

    /**
     * 查看标注数目
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public List<SlideAnnoStatisticsVO> getSlideAnnoStatistics(SlideQueryIN params) throws Exception {
        List<SlideAnnoStatisticsVO> voList = new ArrayList<>();
        QueryWrapper<Marking> queryWrapper = Wrappers.query();
        queryWrapper.eq("annotation_type", "Draw");
        queryWrapper.eq("project_id", params.getProjectId());
        queryWrapper.select("slide_id", "category_id", "create_by");
        List<Marking> annotationList = markingMapperV1.selectList(queryWrapper);
        if (annotationList != null && !annotationList.isEmpty()) {
            voList.add(SlideAnnoStatisticsVO.builder().statisticsType("人工标注").result(annotationList.size()).build());
            voList.add(SlideAnnoStatisticsVO.builder().statisticsType("已审核标注").result(annotationList.size()).build());
            List<PathologicalIndicatorCategory> pathologicalIndicatorCategoryList = pathologicalIndicatorCategoryMapperV1.selectList(Wrappers.query());
            Map<Long, String> categoryMap = new HashMap<>();
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
                            vo = SlideAnnoStatisticsVO.builder().statisticsType("无属性").result(subs.size()).build();
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

    /**
     * 数据导出
     *
     * @param params
     * @return
     * @throws Exception
     */
    public void slideAnnoStatisticsExport(SlideQueryIN params) throws Exception {
        List<SlideExportVO> list = getBaseMapper().querySlides(params);
        Map<Long, SlideExportVO> map = new HashMap<>();
        List<Map<String,String>> catesMapList = new ArrayList<>();
        List<Long> slideIds = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            list.forEach(slideVO -> {
                slideIds.add(slideVO.getSlideId());
                map.put(slideVO.getSlideId(), slideVO);
            });
            List<Marking> annotationList = queryAnnotation(slideIds, params);
            List<String>  columns = handleAnnoStatisticsExport(annotationList,map,catesMapList);
            //通过hutool工具创建的excel的writer，默认为xls格式
            ExcelWriter writer = ExcelUtil.getWriter();
            //自定义excel标题和列名
            columns.forEach(c->{
                writer.addHeaderAlias(c,c);
            });
            writer.write(catesMapList,true);
            httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
            httpServletResponse.setHeader("responseType","blob");
            //name是下载对话框的名称，不支持中文，想用中文名称需要进行utf8编码
            String excelName = "切片数据";
            excelName = URLEncoder.encode(excelName, "utf-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + excelName +".xls");
            ServletOutputStream excelOut = null;
            //将excel文件信息写入输出流，返回给调用者
            try {
                excelOut = httpServletResponse.getOutputStream();
                writer.flush(excelOut,true);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                writer.close();
            }
            IoUtil.close(excelOut);
        }
    }

    private List<String> handleAnnoStatisticsExport(List<Marking> annotationList, Map<Long, SlideExportVO> slideExportVOMap,List<Map<String,String>> catesMapList) throws Exception {
        List<String> columns = new ArrayList<>();
        columns.add(col1);
        columns.add(col2);
        columns.add(col3);
        columns.add(col4);
        columns.add(col5);
        columns.add(col6);
        Map<String,Boolean> columnMap = new HashMap<>();
        if (annotationList != null && !annotationList.isEmpty()) {
            List<PathologicalIndicatorCategory> pathologicalIndicatorCategoryList = pathologicalIndicatorCategoryMapperV1.selectList(Wrappers.query());
            Map<Long, String> categoryMap = new HashMap<>();
            for (PathologicalIndicatorCategory c : pathologicalIndicatorCategoryList) {
                categoryMap.put(c.getCategoryId(), c.getCategoryName());
            }
            Map<Long, List<Marking>> map = annotationList.stream().collect(Collectors.groupingBy(Marking::getSlideId));
            for (Long key : map.keySet()) {
                SlideExportVO vo = slideExportVOMap.get(key);
                Map<String,String> catesMap = new HashMap<>();
                List<Marking> subs = map.get(key);
                if (subs != null && !subs.isEmpty()) {
                    Map<Long, List<Marking>> categorys = subs.stream().collect(Collectors.groupingBy(Marking::getCategoryId));
                    if (categorys != null && !categorys.isEmpty()) {
                        for (Long k : categorys.keySet()) {
                            List<Marking> annoCateList = categorys.get(k);
                            if (annoCateList != null && !annoCateList.isEmpty()) {
                                if (k==0){
                                    catesMap.put(col6,String.valueOf(annoCateList.size()));
                                }else{
                                    String col = categoryMap.get(k);
                                    catesMap.put(col,String.valueOf(annoCateList.size()));
                                    if (columnMap.get(col)==null){
                                        columnMap.put(col,true);
                                        columns.add(col);
                                    }
                                }
                            }
                        }
                    }
                }
                catesMap.put(col1,String.valueOf(vo.getSlideId()));
                catesMap.put(col2,vo.getImageCode());
                catesMap.put(col3,vo.getProjectName());
                catesMap.put(col4,vo.getRemark());
                catesMap.put(col5,String.valueOf(subs.size()));
                vo.setCates(catesMap);
                vo.setManualAnnoCount(subs.size());
                catesMapList.add(catesMap);
            }
        }
        return columns;
    }


    private List<Marking> queryAnnotation(List<Long> slideIds, SlideQueryIN params) throws Exception {
        QueryWrapper<Marking> queryWrapper = Wrappers.query();
        queryWrapper.eq("annotation_type", "Draw");
        queryWrapper.eq("project_id", params.getProjectId());
        if (slideIds != null) {
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
            Map<Long, String> categoryMap = new HashMap<>();
            for (PathologicalIndicatorCategory c : pathologicalIndicatorCategoryList) {
                categoryMap.put(c.getCategoryId(), c.getCategoryName());
            }
            List<SysUser> userList = sysUserMapperV1.selectList(Wrappers.query());
            Map<Long, String> userMap = new HashMap<>();
            for (SysUser u : userList) {
                userMap.put(u.getUserId(), u.getUserName());
            }
            Map<Long, List<Marking>> map = annotationList.stream().collect(Collectors.groupingBy(Marking::getSlideId));
            for (Long key : map.keySet()) {
                SlideVO vo = slideVOMap.get(key);
                List<Marking> subs = map.get(key);
                if (subs != null && !subs.isEmpty()) {
                    Map<Long, List<Marking>> categorys = subs.stream().collect(Collectors.groupingBy(Marking::getCategoryId));
                    vo.setCategoryTypes(handleCategorys(categorys, categoryMap));
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

    private String handleUsers(Map<Long, List<Marking>> users, Map<Long, String> userMap) throws Exception {
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

}




