package cn.staitech.anno.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.service.StatisticService;
import cn.staitech.anno.vo.statistic.StatisticListInVO;
import cn.staitech.anno.vo.statistic.StatisticListOutVO;
import cn.staitech.anno.vo.statistic.StatisticObjectOutVO;
import cn.staitech.anno.vo.statistic.TableDateOutVO;
import cn.staitech.anno.vo.statistic.excel.AnnotationCountExcelVO;
import cn.staitech.anno.vo.statistic.excel.AnnotationDateExcelVO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.staitech.anno.constant.CommonConstant.*;

/**
 * 数据统计模块通用类
 *
 * @author zhaoshaoning
 */
@Slf4j
public class StatisticListUtils {
    /**
     * 获取两个日期之间的所有日期 (年月日)
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getBetweenDate(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.DATE, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取两个日期之间的所有月份
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getBetweenMonth(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一月
                calendar.add(Calendar.MONTH, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取综合统计列表x轴、y轴结果列表
     *
     * @param resp      综合查询结果列表
     * @param dimension 统计维度
     * @return 综合统计列表x轴、y轴结果列表
     */
    public static List statisticListObject2List(List<StatisticObjectOutVO> resp, String dimension) {
        // 处理综合查询返回结果
        List respList = new ArrayList<>();
        for (StatisticObjectOutVO statisticObject : resp) {
            List respOne = new ArrayList<>();
            respOne.add(statisticObject.getStatisticCount());
            // 统计维度：项目
            respOne.add(dimension + statisticObject.getStatisticName());
            respOne.add(statisticObject.getStatisticId());

            respList.add(respOne);
        }
        return respList;

    }

    /**
     * 获取日期维度下的x轴、y轴结果列表
     *
     * @param resp     综合查询结果列表
     * @param dateList 综合查询时间段的所有日期
     * @return 综合统计列表x轴、y轴结果列表
     */

    public static List<List> statisticListDateFill(List<StatisticObjectOutVO> resp, List<String> dateList) {
        // 处理综合查询返回结果, 将所有日期放到respDateList列表中, 将所有数量统计放到respCountList列表中
        List<String> respDateList = new ArrayList<>();
        List<Long> respCountList = new ArrayList<>();
        for (StatisticObjectOutVO statisticObject : resp) {
            respDateList.add(statisticObject.getStatisticDate());
            respCountList.add(statisticObject.getStatisticCount());
        }
        // 将dateList的日期与resp的日期一一对应，不对应的其statisticCount值填为0
        List respDateFillList = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            List respOne = new ArrayList<>();
            int index = respDateList.indexOf(dateList.get(i));
            if (index != -1) {
                Long statisticCount = respCountList.get(index);
                String dateOne = dateList.get(i);
                respOne.add(statisticCount);
                respOne.add("标注日期：" + dateOne);
            } else {
                String dateOne = dateList.get(i);
                respOne.add(0);
                respOne.add("标注日期：" + dateOne);
            }
            respDateFillList.add(respOne);
        }
        Collections.reverse(respDateFillList);
        return respDateFillList;
    }

    /**
     * 获取综合统计列表聚合后的x轴、y轴结果列表
     *
     * @param resp     综合查询结果列表
     * @param dateList 综合查询时间段的所有日期（3天/3月）
     * @return 综合统计列表聚合后的x轴、y轴结果列表
     */

    public static List statisticList3DateFill(List<StatisticObjectOutVO> resp, List<String> dateList) {
        // 获取每天或者每月综合统计列表的x轴、y轴结果列表
        // 处理综合查询返回结果, 将所有日期放到respDateList列表中, 将所有数量统计放到respCountList列表中
        List<String> respDateList = new ArrayList<>();
        List<Long> respCountList = new ArrayList<>();
        for (StatisticObjectOutVO statisticObject : resp) {
            respDateList.add(statisticObject.getStatisticDate());
            respCountList.add(statisticObject.getStatisticCount());
        }
        // 将dateList的日期与resp的日期一一对应，不对应的其statisticCount值填为0
        List<List> respDateFillList = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            List respOne = new ArrayList<>();
            int index = respDateList.indexOf(dateList.get(i));
            if (index != -1) {
                Long statisticCount = respCountList.get(index);
                String dateOne = dateList.get(i);
                respOne.add(statisticCount);
                respOne.add(dateOne);
            } else {
                String dateOne = dateList.get(i);
                respOne.add(0L);
                respOne.add(dateOne);
            }
            respDateFillList.add(respOne);
        }
        // 遍历列表，进行聚合统计
        List<List> resp3DateFillList = new ArrayList<>();
        int total = respDateFillList.size();
        for (int i = 0; i < total; i += 3) {
            List resp3DateFillOne = new ArrayList<>();
            Long respCountSum = 0L;
            String respDate = null;
            Long respCountSum1 = (Long) respDateFillList.get(i).get(0);
            String respDate1 = (String) respDateFillList.get(i).get(1);
            int j = i + 1;
            int k = i + 2;
            if (j < total) {
                if (k < total) {
                    Long respCountSum2 = (Long) respDateFillList.get(j).get(0);
                    Long respCountSum3 = (Long) respDateFillList.get(k).get(0);
                    String respDate2 = (String) respDateFillList.get(j).get(1);
                    String respDate3 = (String) respDateFillList.get(k).get(1);
                    respCountSum = respCountSum1 + respCountSum2 + respCountSum3;
                    respDate = "标注日期：" + respDate1 + "~" + respDate3;
                } else {
                    Long respCountSum2 = (Long) respDateFillList.get(j).get(0);
                    String respDate2 = (String) respDateFillList.get(j).get(1);
                    respCountSum = respCountSum1 + respCountSum2;
                    respDate = "标注日期：" + respDate1 + "~" + respDate2;
                }
            } else {
                respCountSum = respCountSum1;
                respDate = "标注日期：" + respDate1 + "~" + respDate1;
            }
            resp3DateFillOne.add(respCountSum);
            resp3DateFillOne.add(respDate);
            resp3DateFillList.add(resp3DateFillOne);
        }
        Collections.reverse(resp3DateFillList);
        return resp3DateFillList;
    }

    /**
     * 获取综合统计列表X轴刻度值
     *
     * @param resp 综合查询结果列表
     * @return 综合统计列表X轴刻度值
     */
    public static long statisticListXScaleValue(List<StatisticObjectOutVO> resp) {
        long XScaleValue;
        long MaxValue = 0L;
        // 遍历出数量最大值
        for (StatisticObjectOutVO statisticObject : resp) {
            if (statisticObject.getStatisticCount() > MaxValue) {
                MaxValue = statisticObject.getStatisticCount();
            }
        }
        // 筛选出x轴刻度值
        if (MaxValue >= 0 && MaxValue < 10) {
            XScaleValue = 1;
        } else if (MaxValue >= 10 && MaxValue < 50) {
            XScaleValue = 5;
        } else if (MaxValue >= 50 && MaxValue < 100) {
            XScaleValue = 10;
        } else if (MaxValue >= 100 && MaxValue < 500) {
            XScaleValue = 50;
        } else if (MaxValue >= 500 && MaxValue < 1000) {
            XScaleValue = 100;
        } else if (MaxValue >= 1000 && MaxValue < 5000) {
            XScaleValue = 500;
        } else if (MaxValue >= 5000 && MaxValue < 10000) {
            XScaleValue = 1000;
        } else if (MaxValue >= 10000 && MaxValue < 50000) {
            XScaleValue = 5000;
        } else if (MaxValue >= 50000 && MaxValue < 100000) {
            XScaleValue = 10000;
        } else if (MaxValue >= 100000 && MaxValue < 500000) {
            XScaleValue = 50000;
        } else {
            XScaleValue = 50000;
        }
        return XScaleValue;
    }

    /**
     * 处理service层返回结果
     *
     * @param statisticListRep            最终查询结果列表
     * @param resp                        service层查询结果列表
     * @param statisticCategoryDictLabel  统计数量类型
     * @param statisticDimensionDictLabel 统计维度
     */
    public static void statisticListOut(StatisticListOutVO statisticListRep, List resp, String statisticCategoryDictLabel, String statisticDimensionDictLabel) {
        // 获取综合统计列表x轴刻度值
        Long statisticListXScaleValue = StatisticListUtils.statisticListXScaleValue(resp);
        // 将返回结果由列表对象，改为列表[][0]为统计数量，[][1]为统计维度名称，[][2]为统计维度ID
        String dimension = statisticDimensionDictLabel + "名称：";
        List respList = StatisticListUtils.statisticListObject2List(resp, dimension);
        // 添加查询结果s
        statisticListRep.setStatisticList(respList);
        // 添加统计数量类型
        statisticListRep.setStatisticCategory(statisticCategoryDictLabel);
        // 添加统计维度
        statisticListRep.setStatisticDimension(statisticDimensionDictLabel);
        // 填加综合统计列表x轴刻度值
        statisticListRep.setStatisticListXScaleValue(statisticListXScaleValue);
    }

    /**
     * 统计维度为日期（1天）时，处理service层返回结果
     *
     * @param statisticList               数据统计入参
     * @param statisticListRep            最终查询结果列表
     * @param resp                        service层查询结果列表
     * @param statisticCategoryDictLabel  统计数量类型
     * @param statisticDimensionDictLabel 统计维度
     */
    public static void statisticListDateOut(StatisticListInVO statisticList, StatisticListOutVO statisticListRep, List resp, String statisticCategoryDictLabel, String statisticDimensionDictLabel, long daysBetween) {
        String startTime = statisticList.getStartTime();
        String endTime = statisticList.getEndTime();
        // 获取综合统计列表x轴刻度值
        Long statisticListXScaleValue = StatisticListUtils.statisticListXScaleValue(resp);
        // 将数据库不存在的日期填充，获取综合统计列表x轴、y轴结果列表
        List<String> dateList;
        List respDateFillList = new ArrayList<>();
        if (daysBetween >= 0 && daysBetween < THIRTEEN_DAY) {
            dateList = StatisticListUtils.getBetweenDate(startTime, endTime);
            respDateFillList = StatisticListUtils.statisticListDateFill(resp, dateList);
        } else if (daysBetween >= THIRTEEN_DAY && daysBetween < THIRTY_ONE_DAY) {
            dateList = StatisticListUtils.getBetweenDate(startTime, endTime);
            respDateFillList = StatisticListUtils.statisticList3DateFill(resp, dateList);
        } else if (daysBetween >= THIRTY_ONE_DAY && daysBetween < ONE_YEAR) {
            dateList = StatisticListUtils.getBetweenMonth(startTime, endTime);
            respDateFillList = StatisticListUtils.statisticListDateFill(resp, dateList);
        } else if (daysBetween >= ONE_YEAR && daysBetween < THREE_YEAR) {
            dateList = StatisticListUtils.getBetweenMonth(startTime, endTime);
            respDateFillList = StatisticListUtils.statisticList3DateFill(resp, dateList);
        }
        // 添加查询结果s
        statisticListRep.setStatisticList(respDateFillList);
        // 添加统计数量类型
        statisticListRep.setStatisticCategory(statisticCategoryDictLabel);
        // 添加统计维度
        statisticListRep.setStatisticDimension(statisticDimensionDictLabel);
        // 填加综合统计列表x轴刻度值
        statisticListRep.setStatisticListXScaleValue(statisticListXScaleValue);
    }

    /**
     * 导出excel(标注数量-标注日期)
     *
     * @param response
     * @param displayQuantity
     * @param statisticalDimension
     * @param result
     * @throws IOException
     */
    public static void exportExcelDateUtil(HttpServletResponse response, String displayQuantity, String statisticalDimension, List<StatisticObjectOutVO> result) throws IOException {
        String fileName = displayQuantity + GLIDE_LINE + statisticalDimension;

        List<List<String>> title = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add(statisticalDimension);
        List<String> head1 = ListUtils.newArrayList();
        head1.add(displayQuantity);
        title.add(head0);
        title.add(head1);

        List<AnnotationDateExcelVO> listDate = ListUtils.newArrayList();
        for (StatisticObjectOutVO statisticObjectOutVO : result) {
            AnnotationDateExcelVO excel = new AnnotationDateExcelVO();
            BeanUtils.copyProperties(statisticObjectOutVO, excel);
            listDate.add(excel);
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String replaceAll = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + replaceAll + ".xlsx");
        EasyExcel.write(response.getOutputStream(), AnnotationDateExcelVO.class).head(title).sheet(statisticalDimension).doWrite(listDate);
    }

    /**
     * 导出excel(标注数量-项目、病理指标、标注类别、成员、图像)
     *
     * @param response
     * @param displayQuantity
     * @param statisticalDimension
     * @param result
     * @throws IOException
     */
    public static void exportExcelUtil(HttpServletResponse response, String displayQuantity, String statisticalDimension, List<StatisticObjectOutVO> result) throws IOException {
        String fileName = displayQuantity + GLIDE_LINE + statisticalDimension;

        // 标题
        List<List<String>> title = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add(statisticalDimension);
        List<String> head1 = ListUtils.newArrayList();
        head1.add(displayQuantity);
        title.add(head0);
        title.add(head1);

        List<AnnotationCountExcelVO> list = ListUtils.newArrayList();
        for (StatisticObjectOutVO statisticObjectOutVO : result) {
            AnnotationCountExcelVO excel = new AnnotationCountExcelVO();
            BeanUtils.copyProperties(statisticObjectOutVO, excel);
            list.add(excel);
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String replaceAll = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + replaceAll + ".xlsx");
        EasyExcel.write(response.getOutputStream(), AnnotationCountExcelVO.class).head(title).sheet(statisticalDimension).doWrite(list);
    }

    /**
     * 获取两个日期之间的时间差（分钟）
     *
     * @param imageCreatedTime 图像上传创建时间
     * @return
     */
    public static long getDateDiff(Date imageCreatedTime) throws ParseException {
        // 获取系统当前时间
        Date cte = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cteSdf = sdf.format(cte);
        String imageCreatedTimeFormat = sdf.format(imageCreatedTime);
        // 计算日期差，单位：天
        Date startTime = sdf.parse(imageCreatedTimeFormat);
        Date endTime = sdf.parse(cteSdf);
        long daysBetween = (endTime.getTime() - startTime.getTime()) / (60 * 1000);
        return daysBetween;
    }

    /**
     * 判断startTime、endTime值，并返回日期差
     *
     * @param statisticList 细分筛选查询入参
     * @return daysBetween 日期差
     */
    public long statisticSetStartEndTime(StatisticListInVO statisticList, StatisticService statisticService) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 未输入日期的情况
        if (ObjectUtils.isEmpty(statisticList.getStartTime()) && ObjectUtils.isEmpty(statisticList.getEndTime())) {
            String endTime = dateFormat.format(new Date());
            TableDateOutVO tableDateOutVO = statisticService.statisticSelectEarliestAnnoDate(statisticList.getOrganizationId());
            if (ObjectUtils.isEmpty(tableDateOutVO)) {
                statisticList.setStartTime(endTime);
            } else {
                String startTime = statisticService.statisticSelectEarliestAnnoDate(statisticList.getOrganizationId()).getEarliestDate();
                statisticList.setStartTime(startTime);
            }
            statisticList.setEndTime(endTime);
        }
        long startTime = dateFormat.parse(statisticList.getStartTime()).getTime();
        long endTime = dateFormat.parse(statisticList.getEndTime()).getTime();
        long days = (endTime - startTime) / (60 * 60 * 24 * 1000) + 1;
        return days;
    }

    /**
     * 综合统计列表————数量（横轴）：标注数量；统计维度（竖轴）：项目、病理指标、标注类别、图像、成员
     *
     * @param statisticList               细分筛选接口入参
     * @param statisticListRep            返回结果列表
     * @param resp                        service层返回结果
     * @param statisticCategoryDictLabel  统计数量类型
     * @param statisticDimensionDictLabel 统计维度
     */
    public StatisticListOutVO statisticAnnoRespOut(StatisticListInVO statisticList, StatisticListOutVO statisticListRep, List<StatisticObjectOutVO> resp, String statisticCategoryDictLabel, String statisticDimensionDictLabel, StatisticService statisticService) {
        switch (statisticDimensionDictLabel) {
            case PROJECT:
                resp = statisticService.statisticSelectAnnoProjectList(statisticList);
                break;
            case PATHOLOGY_INDICATOR:
                resp = statisticService.statisticSelectAnnoIndicatorList(statisticList);
                break;
            case ANNOTATION_CATEGORY:
                resp = statisticService.statisticSelectAnnoCategoryList(statisticList);
                resp.forEach(o -> {
                    if (StringUtils.isEmpty(o.getStatisticName()) && ObjectUtil.isNotNull(o.getStatisticCount())) {
                        o.setStatisticName(MessageSource.M("NO_ATTRIBUTE"));
                    }
                });
                break;
            case SLIDE:
                resp = statisticService.statisticSelectAnnoImageList(statisticList);
                break;
            case USER:
                resp = statisticService.statisticSelectMemberList(statisticList);
                break;
            default:
                log.error("statisticDimension参数值错误");
                break;
        }
        // 处理service层返回结果
        StatisticListUtils.statisticListOut(statisticListRep, resp, statisticCategoryDictLabel, statisticDimensionDictLabel);
        return statisticListRep;
    }

    /**
     * 综合统计列表————数量（横轴）：图像数量；统计维度（竖轴）：项目、病理指标、标注类别、成员
     *
     * @param statisticList               细分筛选接口入参
     * @param statisticListRep            返回结果列表
     * @param resp                        service层返回结果
     * @param statisticCategoryDictLabel  统计数量类型
     * @param statisticDimensionDictLabel 统计维度
     */
    public StatisticListOutVO statisticImageRespOut(StatisticListInVO statisticList, StatisticListOutVO statisticListRep, List<StatisticObjectOutVO> resp, String statisticCategoryDictLabel, String statisticDimensionDictLabel, StatisticService statisticService) {
        switch (statisticDimensionDictLabel) {
            case PROJECT:
                resp = statisticService.statisticSelectImageProjectList(statisticList);
                break;
            case PATHOLOGY_INDICATOR:
                resp = statisticService.statisticSelectImageIndicatorList(statisticList);
                break;
            case ANNOTATION_CATEGORY:
                resp = statisticService.statisticSelectImageCategoryList(statisticList);
                break;
            case CommonConstant.USER:
                resp = statisticService.statisticSelectImageMemberList(statisticList);
                break;
            default:
                log.error("statisticDimension参数值错误");
                break;
        }
        // 处理service层返回结果
        StatisticListUtils.statisticListOut(statisticListRep, resp, statisticCategoryDictLabel, statisticDimensionDictLabel);
        return statisticListRep;
    }

    /**
     * 处理统计数量类别为标注、统计维度为日期的返回结果
     *
     * @param statisticList               细分筛选接口入参
     * @param statisticListRep            返回结果列表
     * @param resp                        service层返回结果
     * @param statisticCategoryDictLabel  统计数量类型
     * @param statisticDimensionDictLabel 统计维度
     * @param daysBetween                 日期差
     */
    public StatisticListOutVO statisticAnnoDateRespOut(StatisticListInVO statisticList, StatisticListOutVO statisticListRep, List<StatisticObjectOutVO> resp, String statisticCategoryDictLabel, String statisticDimensionDictLabel, long daysBetween, StatisticService statisticService) {
        // 按日期差进行分类查询
        if (daysBetween >= 0 && daysBetween < THIRTEEN_DAY) {
            // 0天≤时间＜13天,数据以每天来进行展现
            resp = statisticService.statisticSelectAnnoDateDaysList(statisticList);
        } else if (daysBetween >= THIRTEEN_DAY && daysBetween < THIRTY_ONE_DAY) {
            // 13天≤时间＜1月,数据以3天一压缩来进行展现
            resp = statisticService.statisticSelectAnnoDateDaysList(statisticList);
        } else if (daysBetween >= THIRTY_ONE_DAY && daysBetween < ONE_YEAR) {
            // 1月≤时间＜1年,数据以每月一压缩来进行展现
            resp = statisticService.statisticSelectAnnoDateMonthsList(statisticList);
        } else if (daysBetween >= ONE_YEAR && daysBetween < THREE_YEAR) {
            // 1年≤时间≤3年,数据以每3个月一压缩来进行展现
            resp = statisticService.statisticSelectAnnoDateMonthsList(statisticList);
        }
        // 统计维度为日期时，处理service层返回结果
        StatisticListUtils.statisticListDateOut(statisticList, statisticListRep, resp, statisticCategoryDictLabel, statisticDimensionDictLabel, daysBetween);
        return statisticListRep;
    }

    /**
     * 处理统计数量类别为图像、统计维度为日期的返回结果
     *
     * @param statisticList               细分筛选接口入参
     * @param statisticListRep            返回结果列表
     * @param resp                        service层返回结果
     * @param statisticCategoryDictLabel  统计数量类型
     * @param statisticDimensionDictLabel 统计维度
     * @param daysBetween                 日期差
     */
    public StatisticListOutVO statisticImageDateRespOut(StatisticListInVO statisticList, StatisticListOutVO statisticListRep, List<StatisticObjectOutVO> resp, String statisticCategoryDictLabel, String statisticDimensionDictLabel, long daysBetween, StatisticService statisticService) {
        // 按日期差进行分类查询
        if (daysBetween >= 0 && daysBetween < THIRTEEN_DAY) {
            // 0天≤时间＜13天,数据以每天来进行展现
            resp = statisticService.statisticSelectImageDateDaysList(statisticList);
        } else if (daysBetween >= THIRTEEN_DAY && daysBetween < THIRTY_ONE_DAY) {
            // 13天≤时间＜1月,数据以3天一压缩来进行展现
            resp = statisticService.statisticSelectImageDateDaysList(statisticList);
        } else if (daysBetween >= THIRTY_ONE_DAY && daysBetween < ONE_YEAR) {
            // 1月≤时间＜1年,数据以每月一压缩来进行展现
            resp = statisticService.statisticSelectImageDateMonthsList(statisticList);
        } else if (daysBetween >= ONE_YEAR && daysBetween < THREE_YEAR) {
            // 1年≤时间≤3年,数据以每3个月一压缩来进行展现
            resp = statisticService.statisticSelectImageDateMonthsList(statisticList);
        }
        // 统计维度为日期时，处理service层返回结果
        StatisticListUtils.statisticListDateOut(statisticList, statisticListRep, resp,
                statisticCategoryDictLabel, statisticDimensionDictLabel, daysBetween);
        return statisticListRep;
    }
}
