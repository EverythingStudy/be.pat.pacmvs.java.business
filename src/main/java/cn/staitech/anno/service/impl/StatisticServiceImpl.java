package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.mapper.StatisticMapper;
import cn.staitech.anno.service.StatisticService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.StatisticListUtils;
import cn.staitech.anno.vo.statistic.*;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static cn.staitech.anno.constant.CommonConstant.THREE_YEAR;
import static cn.staitech.anno.aspect.LogFileAspect.response;
@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    @Resource
    private StatisticMapper statisticMapper;

    /**
     * 综合统计列表：标注数量-项目
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectAnnoProjectList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoProjectList(statisticList);
    }

    /**
     * 综合统计列表：标注数量-病理指标
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectAnnoIndicatorList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoIndicatorList(statisticList);
    }

    /**
     * 综合统计列表：标注数量-标注类别
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectAnnoCategoryList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoCategoryList(statisticList);
    }

    /**
     * 综合统计列表：标注数量-成员
     *
     * @param statisticList
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectMemberList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectMemberList(statisticList);
    }

    /**
     * 综合统计列表：标注数量-图像
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectAnnoImageList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoImageList(statisticList);
    }

    /**
     * 综合统计列表: 标注数量-日期-按天统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<StatisticObjectOutVO> statisticSelectAnnoDateDaysList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoDateDaysListExt(statisticList);
    }

    /**
     * 综合统计列表: 标注数量-日期-按月统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<StatisticObjectOutVO> statisticSelectAnnoDateMonthsList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoDateMonthsListExt(statisticList);
    }

    /**
     * 综合统计列表：图像数量-项目
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectImageProjectList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectImageProjectList(statisticList);
    }

    /**
     * 综合统计列表：图像数量-病理指标
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectImageIndicatorList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectImageIndicatorList(statisticList);
    }

    /**
     * 综合统计列表：图像数量-标注类别
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectImageCategoryList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectImageCategoryList(statisticList);
    }

    /**
     * 综合统计列表：图像数量-成员
     *
     * @param statisticList
     * @return
     */
    @Override
    public List<StatisticObjectOutVO> statisticSelectImageMemberList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectImageMemberList(statisticList);
    }

    /**
     * 综合统计列表: 日期-图像数量-按天统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<StatisticObjectOutVO> statisticSelectImageDateDaysList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectImageDateDaysList(statisticList);
    }

    /**
     * 综合统计列表: 日期-图像数量-按月统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<StatisticObjectOutVO> statisticSelectImageDateMonthsList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectImageDateMonthsList(statisticList);
    }

    /**
     * 综合统计列表: 成员列表-标注
     *
     * @return
     */
    @Override
    public List<StatisticUserListOutVO> queryAnnotationMembersList(StatisticListInVO statisticListInVO) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            statisticListInVO.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        return statisticMapper.queryAnnotationMembersList(statisticListInVO);
    }

    /**
     * 综合统计列表: 成员列表-图像
     *
     * @param statisticListInVO
     * @return
     */
    @Override
    public List<StatisticUserListOutVO> queryImageMembersList(StatisticListInVO statisticListInVO) {
        return statisticMapper.queryImageMembersList(statisticListInVO);
    }


    /**
     * 标注统计列表: 项目列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<AnnotationStatisticIdListOutVO> statisticSelectAnnoProjectIdList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoProjectIdList(statisticList);
    }

    /**
     * 标注统计列表: 病理指标列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<AnnotationStatisticIdListOutVO> statisticSelectAnnoIndicatorIdList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoIndicatorIdList(statisticList);
    }

    /**
     * 标注统计列表: 标注类别列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<AnnotationStatisticIdListOutVO> statisticSelectAnnoCategoryIdList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoCategoryIdList(statisticList);
    }

    @Override
    public List<AnnotationStatisticIdListOutVO> statisticSelectAnnoMembersIdList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoMembersIdList(statisticList);
    }

    /**
     * 标注统计列表: 图像列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<AnnotationStatisticIdListOutVO> statisticSelectAnnoImageIdList(StatisticListInVO statisticList) {
        return statisticMapper.statisticSelectAnnoImageIdList(statisticList);
    }


    /**
     * 标注统计列表-项目Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<AnnotationStatisticListPageOutVO> statisticSelectAnnoProjectPageList(AnnotationStatisticListPageInVO statisticList) {
        return statisticMapper.statisticSelectAnnoProjectPageList(statisticList);
    }

    /**
     * 标注统计列表-病理指标Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<AnnotationStatisticListPageOutVO> statisticSelectAnnoIndicatorPageList(AnnotationStatisticListPageInVO statisticList) {
        return statisticMapper.statisticSelectAnnoIndicatorPageList(statisticList);
    }

    /**
     * 标注统计列表: 标注类别Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<AnnotationStatisticListPageOutVO> statisticSelectAnnoCategoryPageList(AnnotationStatisticListPageInVO statisticList) {
        return statisticMapper.statisticSelectAnnoCategoryPageList(statisticList);
    }

    /**
     * 标注统计列表: 成员Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    @Override
    public List<AnnotationStatisticListPageOutVO> SelectMembersPageList(AnnotationStatisticListPageInVO statisticList) {
        return statisticMapper.SelectMembersPageList(statisticList);
    }

    /**
     * 标注统计列表: 图像Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    public List<AnnotationStatisticListPageOutVO> statisticSelectAnnoImagePageList(AnnotationStatisticListPageInVO statisticList) {
        return statisticMapper.statisticSelectAnnoImagePageList(statisticList);
    }


    /**
     * 查询标注表最早时间记录
     *
     * @return
     */
    public TableDateOutVO statisticSelectEarliestAnnoDate(Long organizationId) {
        return statisticMapper.statisticSelectEarliestAnnoDate(organizationId);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return
     */
    public StatisticSysDictDataOutVO statisticSelectDictDataById(Long dictCode) {
        return statisticMapper.statisticSelectDictDataById(dictCode);
    }

    /**
     * 综合统计列表
     *
     * @param statisticList
     * @return
     */
    @Override
    public R<StatisticListOutVO> statisticList(StatisticListInVO statisticList) throws ParseException {

        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            statisticList.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        // 创建返回结果实例
        StatisticListOutVO statisticListRep = new StatisticListOutVO();
        List<StatisticObjectOutVO> resp = new ArrayList<>();
        StatisticListUtils statisticListUtils = new StatisticListUtils();
        // 通过SysDictData获取统计维度值、统计数量类别
        String displayQuantity = statisticSelectDictDataById(statisticList.getStatisticCategory()).getDictLabel();
        String statisticalDimension = statisticSelectDictDataById(statisticList.getStatisticDimension()).getDictLabel();
        // 数量（横轴）--标注数量
        if (displayQuantity.equals(MessageSource.M("ANNOTATION_COUNT"))) {
            // 统计维度（竖轴）--标注日期
            if (statisticalDimension.equals(MessageSource.M("ANNOTATION_DATE"))) {
                // 判断startTime、endTime值，并返回日期差
                long daysBetween = statisticListUtils.statisticSetStartEndTime(statisticList, this);
                // 按日期差进行分类查询
                if (daysBetween < THREE_YEAR) {
                    statisticListRep = statisticListUtils.statisticAnnoDateRespOut(statisticList, statisticListRep, resp, displayQuantity, statisticalDimension, daysBetween, this);
                    return R.ok(statisticListRep);
                } else if (daysBetween >= THREE_YEAR) {
                    return R.fail(MessageSource.M("MAX_SELECT_TIME_ERROR"));
                } else {
                    return R.fail(MessageSource.M("SELECT_TIME_ERROR"));
                }
            } else {
                // 统计维度（竖轴）--除标注日期以外的
                statisticListRep = statisticListUtils.statisticAnnoRespOut(statisticList, statisticListRep, resp, displayQuantity, statisticalDimension, this);
                return R.ok(statisticListRep);
            }
        }
        // 数量（横轴）--图像数量
        else if (displayQuantity.equals(MessageSource.M("SLIDE_COUNT"))) {
            // 统计维度（竖轴）--标注日期
            if (statisticalDimension.equals(MessageSource.M("ANNOTATION_DATE"))) {
                // 判断startTime、endTime值，并返回日期差
                long daysBetween = statisticListUtils.statisticSetStartEndTime(statisticList, this);
                // 按日期差进行分类查询
                if (daysBetween >= 0 && daysBetween < THREE_YEAR) {
                    statisticListRep = statisticListUtils.statisticImageDateRespOut(statisticList, statisticListRep, resp, displayQuantity, statisticalDimension, daysBetween, this);
                    return R.ok(statisticListRep);
                } else if (daysBetween >= THREE_YEAR) {
                    return R.fail(MessageSource.M("MAX_SELECT_TIME_ERROR"));
                } else {
                    return R.fail(MessageSource.M("SELECT_TIME_ERROR"));
                }
            } else {
                // 统计维度（竖轴）--除标注日期以外的
                statisticListRep = statisticListUtils.statisticImageRespOut(statisticList, statisticListRep, resp, displayQuantity, statisticalDimension, this);
                return R.ok(statisticListRep);
            }
        } else {
            return R.fail(MessageSource.M("STATISTIC_CATEGORY"));
        }
    }

    /**
     * 切片维度
     * @param req
     * @throws IOException
     */
    @Override
    public void exportOrderSlide(ExportOrderProjectIn req) throws IOException {
        log.info("标注统计切片维度导出接口开始！");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("标注统计-切片维度", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        //Long organizationId=1L;
        //查询所有标签数据
        List<PathologicalIndicatorCategory> categoryList = statisticMapper.statisticCategoryName(req.getProjectList(), organizationId);

        //查询内容数据
        List<ExportSlideOrderProjectOut> projectOutList = statisticMapper.statisticSlideByProject(req.getProjectList(), organizationId);

        //创建表头
        List<List<String>> header = getLists(categoryList);

        //表格数据
        List<List<Object>> datas = new ArrayList<>();

        if(!CollectionUtils.isEmpty(projectOutList)){
            projectOutList.forEach(e->{
                List<Object> temp = new ArrayList<>();
                temp.add(e.getSlideId());
                temp.add(e.getImageName());
                temp.add("saas2.0");
                temp.add(e.getProjectName());
                temp.add(e.getDescription());
                temp.add(e.getMarkingTotal());
                if(!CollectionUtils.isEmpty(categoryList)){
                    categoryList.forEach(category->{
                        int i = statisticMapper.countSlideByProject(e.getSlideId(), category.getCategoryId());
                        temp.add(i);
                    });
                }
                datas.add(temp);
            });
        }

        ExcelWriter head = EasyExcel.write(response.getOutputStream()).build();
        WriteSheet mb1 = EasyExcel.writerSheet(0, "项目切片数据").head(header).registerWriteHandler(new SimpleColumnWidthStyleStrategy(32)).build();
        head.write(datas,mb1);
        head.finish();
    }

    /**
     * 参与者维度
     * @param req
     */
    @Override
    public void exportOrderMember(ExportOrderProjectIn req) throws IOException{
        log.info("标注统计参与者维度导出接口开始！");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("标注统计-参与者维度", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        //Long organizationId=1L;
        //查询所有标签数据
        List<PathologicalIndicatorCategory> categoryList = statisticMapper.statisticCategoryName(req.getProjectList(), organizationId);

        //查询内容数据
        List<ExportMemberOrderProjectOut> projectOuts = statisticMapper.statisticMembersByProject(req.getProjectList(), organizationId);

        //创建表头
        List<List<String>> header = getListsExt(categoryList);

        //表格数据
        List<List<Object>> datas = new ArrayList<>();

        if(!CollectionUtils.isEmpty(projectOuts)){
            projectOuts.forEach(e->{
                List<Object> temp = new ArrayList<>();
                temp.add("saas2.0");
                temp.add(e.getProjectName());
                temp.add(e.getUserName());
                temp.add(e.getMarkingTotal());
                temp.add(e.getNotReviewed());
                temp.add(e.getReviewed());
                if(!CollectionUtils.isEmpty(categoryList)){
                    categoryList.forEach(category->{
                        CountMembersByProjecOut projecOuts = statisticMapper.countMembersByProject(e.getProjectId(), e.getUserId(), category.getCategoryId());
                        temp.add(projecOuts.getMarkingTotal());
                        temp.add(projecOuts.getNotReviewed());
                        temp.add(projecOuts.getReviewed());
                    });
                }
                datas.add(temp);
            });
        }

        ExcelWriter head = EasyExcel.write(response.getOutputStream()).build();
        WriteSheet mb1 = EasyExcel.writerSheet(0, "项目切片数据").head(header).registerWriteHandler(new SimpleColumnWidthStyleStrategy(32)).build();
        head.write(datas,mb1);
        head.finish();
    }

    /**
     * 创建表头
     * @param categoryList
     * @return
     */
    private List<List<String>> getLists(List<PathologicalIndicatorCategory> categoryList) {
        List<List<String>> header = new ArrayList<>();
        List<String> cellContain0 = new ArrayList<>();
        cellContain0.add("图像id");
        header.add(cellContain0);
        List<String> cellContain1 = new ArrayList<>();
        cellContain1.add("图像名称");
        header.add(cellContain1);
        List<String> cellContain2 = new ArrayList<>();
        cellContain2.add("平台名称");
        header.add(cellContain2);
        List<String> cellContain3 = new ArrayList<>();
        cellContain3.add("项目名称");
        header.add(cellContain3);
        List<String> cellContain4 = new ArrayList<>();
        cellContain4.add("图像描述");
        header.add(cellContain4);
        List<String> cellContain5 = new ArrayList<>();
        cellContain5.add("图像标注总数");
        header.add(cellContain5);
        if(!CollectionUtils.isEmpty(categoryList)){
            categoryList.forEach(e->{
               List<String> objects = new ArrayList<>();
                objects.add(e.getCategoryName()+"数量");
                header.add(objects);
            });
        }

        return header;
    }

    private List<List<String>> getListsExt(List<PathologicalIndicatorCategory> categoryList) {
        List<List<String>> header = new ArrayList<>();
        List<String> cellContain0 = new ArrayList<>();
        cellContain0.add("平台名称");
        header.add(cellContain0);
        List<String> cellContain1 = new ArrayList<>();
        cellContain1.add("项目名称");
        header.add(cellContain1);
        List<String> cellContain2 = new ArrayList<>();
        cellContain2.add("人员");
        header.add(cellContain2);
        List<String> cellContain3 = new ArrayList<>();
        cellContain3.add("all总数");
        cellContain3.add("总数");
        header.add(cellContain3);
        List<String> cellContain4 = new ArrayList<>();
        cellContain4.add("all总数");
        cellContain4.add("复核前");
        header.add(cellContain4);
        List<String> cellContain5 = new ArrayList<>();
        cellContain5.add("all总数");
        cellContain5.add("复核后");
        header.add(cellContain5);
        if(!CollectionUtils.isEmpty(categoryList)){
            categoryList.forEach(e->{
                List<String> objects1 = new ArrayList<>();
                objects1.add(e.getCategoryName()+"总数");
                objects1.add("总数");
                header.add(objects1);
                List<String> objects2 = new ArrayList<>();
                objects2.add(e.getCategoryName()+"总数");
                objects2.add("复核前");
                header.add(objects2);
                List<String> objects3 = new ArrayList<>();
                objects3.add(e.getCategoryName()+"总数");
                objects3.add("复核后");
                header.add(objects3);
            });
        }

        return header;
    }
}
