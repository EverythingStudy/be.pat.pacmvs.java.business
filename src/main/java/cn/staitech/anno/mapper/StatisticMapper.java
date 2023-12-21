package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.vo.statistic.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 数据统计 数据层
 *
 * @author staitech
 */
public interface StatisticMapper {
    /**
     * 综合统计列表: 标注数量-项目
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectAnnoProjectList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 标注数量-病理指标
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectAnnoIndicatorList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 标注数量-标注类别
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectAnnoCategoryList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 标注数量-成员
     *
     * @param statisticList
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectMemberList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 标注数量-图像
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectAnnoImageList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 标注数量-日期-按天统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectAnnoDateDaysList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 标注数量-日期-按天统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectAnnoDateDaysListExt(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 标注数量-日期-按月统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectAnnoDateMonthsList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 标注数量-日期-按月统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectAnnoDateMonthsListExt(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 图像数量-项目
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectImageProjectList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 图像数量-病理指标
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectImageIndicatorList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 图像数量-标注类别
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectImageCategoryList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 图像数量-成员
     *
     * @param statisticList
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectImageMemberList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 日期-图像数量-按天统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectImageDateDaysList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 日期-图像数量-按月统计
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<StatisticObjectOutVO> statisticSelectImageDateMonthsList(StatisticListInVO statisticList);

    /**
     * 综合统计列表: 成员列表-标注
     *
     * @return
     */
    List<StatisticUserListOutVO> queryAnnotationMembersList(StatisticListInVO statisticListInVO);

    /**
     * 综合统计列表: 成员列表-图像
     *
     * @return
     */
    List<StatisticUserListOutVO> queryImageMembersList(StatisticListInVO statisticListInVO);


    /**
     * 标注统计列表: 项目列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticIdListOutVO> statisticSelectAnnoProjectIdList(StatisticListInVO statisticList);

    /**
     * 标注统计列表: 病理指标列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticIdListOutVO> statisticSelectAnnoIndicatorIdList(StatisticListInVO statisticList);

    /**
     * 标注统计列表: 标注类别列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticIdListOutVO> statisticSelectAnnoCategoryIdList(StatisticListInVO statisticList);

    /**
     * 标注统计列表: 成员列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticIdListOutVO> statisticSelectAnnoMembersIdList(StatisticListInVO statisticList);

    /**
     * 标注统计列表: 图像列表
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticIdListOutVO> statisticSelectAnnoImageIdList(StatisticListInVO statisticList);


    /**
     * 标注统计列表-项目Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticListPageOutVO> statisticSelectAnnoProjectPageList(AnnotationStatisticListPageInVO statisticList);

    /**
     * 标注统计列表-病理指标Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticListPageOutVO> statisticSelectAnnoIndicatorPageList(AnnotationStatisticListPageInVO statisticList);

    /**
     * 标注统计列表: 标注类别Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticListPageOutVO> statisticSelectAnnoCategoryPageList(AnnotationStatisticListPageInVO statisticList);

    /**
     * 标注统计列表: 成员Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticListPageOutVO> SelectMembersPageList(AnnotationStatisticListPageInVO statisticList);

    /**
     * 标注统计列表: 图像Page
     *
     * @param statisticList 数据统计入参列表
     * @return
     */
    List<AnnotationStatisticListPageOutVO> statisticSelectAnnoImagePageList(AnnotationStatisticListPageInVO statisticList);


    /**
     * 查询标注表最早时间记录
     *
     * @return
     */
    TableDateOutVO statisticSelectEarliestAnnoDate(Long organizationId);

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return
     */
    StatisticSysDictDataOutVO statisticSelectDictDataById(@PathVariable("dictCode") Long dictCode);

    List<PathologicalIndicatorCategory> statisticCategoryName(@Param("projectIds") List<Long> projectIds, @Param("organizationId") Long organizationId);

    List<ExportSlideOrderProjectOut> statisticSlideByProject(@Param("projectIds") List<Long> projectIds, @Param("organizationId") Long organizationId);

    int countSlideByProject(@Param("slideId") Long slideId, @Param("categoryId") Long categoryId);

    List<ExportMemberOrderProjectOut> statisticMembersByProject(@Param("projectIds") List<Long> projectIds, @Param("organizationId") Long organizationId);

    CountMembersByProjecOut countMembersByProject(@Param("projectId") Long projectId, @Param("userId") Long userId, @Param("categoryId") Long categoryId);
}
