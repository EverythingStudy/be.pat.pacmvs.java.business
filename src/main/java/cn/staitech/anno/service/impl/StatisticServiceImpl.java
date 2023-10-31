package cn.staitech.anno.service.impl;

import cn.staitech.anno.mapper.StatisticMapper;
import cn.staitech.anno.service.StatisticService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.StatisticListUtils;
import cn.staitech.anno.vo.statistic.*;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static cn.staitech.anno.constant.CommonConstant.THREE_YEAR;

@Service
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
}
