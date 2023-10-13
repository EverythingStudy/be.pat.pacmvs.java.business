package cn.staitech.anno.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.diagnosis.StatisticsBodyVo;
import cn.staitech.anno.domain.vo.diagnosis.StatisticsHeadVo;
import cn.staitech.anno.domain.vo.reportRecord.ReportRecordAddVO;
import cn.staitech.anno.exception.ReportException;
import cn.staitech.anno.mapper.SpecialDiagnosisDetailMapper;
import cn.staitech.anno.mapper.SpecialDiagnosisMapper;
import cn.staitech.anno.mapper.SpecialMapper;
import cn.staitech.anno.service.DiagnosticReportService;
import cn.staitech.anno.service.DiagnosticStatisticsService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.WordTool;
import cn.staitech.anno.utils.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: DiagnosticStatisticsServiceImpl
 * @Description:诊断统计处理
 * @date 2023年7月18日
 */
@Slf4j
@Service
public class DiagnosticStatisticsServiceImpl implements DiagnosticStatisticsService {

    private final static String SUFFIX = ".docx";

    @Resource
    private SpecialDiagnosisMapper specialDiagnosisMapper;

    @Resource
    private SpecialDiagnosisDetailMapper specialDiagnosisDetailMapper;

    @Value("${rpt.dir:../REPORT}")
    private String RPT_DIR;

    @Resource
    private SpecialMapper specialMapper;

    @Resource
    private DiagnosticReportService diagnosticReportService;

    @SuppressWarnings("unused")
    @Override
    public String createRpt(ReportRecordAddVO recordAddVO) throws Exception {
        long startTime = System.currentTimeMillis();
        String specialNumber = recordAddVO.getSpecialNumber();
        Long specialId = recordAddVO.getSpecialId();
        int reasons = recordAddVO.getReasons();
        if (specialId == null) {
            throw new ReportException("专题id为空！");
        }
        // 查询专题
        Special special = specialMapper.selectById(specialId);
        // 计算总共需要生成几个表（看雄性几行、雌性几行，单个表最多8行，一个行别最多4行）
        Map<String, Object> map = new HashMap<>();
        map.put("specialId", specialId);
        map.put("reasons", recordAddVO.getReasons());
        List<StatisticsHeadVo> list = specialDiagnosisMapper.getStatisticsHeadVoListByParm(map);
        // 雄性
        List<StatisticsHeadVo> maleList = new ArrayList<StatisticsHeadVo>();
        // 雌性
        List<StatisticsHeadVo> femaleList = new ArrayList<StatisticsHeadVo>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (StatisticsHeadVo vo : list) {
                int gender = vo.getGender();
                if (gender == 0) {
                    femaleList.add(vo);
                } else if (gender == 1) {
                    maleList.add(vo);
                }
            }
        }

        int avgCol = 4;
        // 将雌性List集合按长度切分
        List<List<StatisticsHeadVo>> fList = ListUtil.partition(femaleList, avgCol);
        int maxFCount = 0;
        if (CollectionUtils.isNotEmpty(fList)) {
            maxFCount = fList.size();
        }
        // 将雄性List集合按长度切分
        List<List<StatisticsHeadVo>> mList = ListUtil.partition(maleList, avgCol);
        int maxMCount = 0;
        if (CollectionUtils.isNotEmpty(mList)) {
            maxMCount = mList.size();
        }
        // 需要创建的总的表个数
        int tableCount = maxMCount > maxFCount ? maxMCount : maxFCount;
        // 每个表的数据汇总，封装为map 分为manPlsList、womanPlsList、和tableName
        List<Map<String, Object>> tableMapList = new ArrayList<>();
        for (int index = 0; index < tableCount; index++) {
            Map<String, Object> tableDate = new HashMap<>();
            List<StatisticsHeadVo> manPlsList = new ArrayList<>();
            List<StatisticsHeadVo> womanPlsList = new ArrayList<>();
            if (index >= maxMCount) {
                // index not exists
                // manPlsList.addAll(new ArrayList<>());
            } else {
                // index exists
                manPlsList.addAll(mList.get(index));
            }
            if (index >= maxFCount) {
                // index not exists
                // womanPlsList.addAll(new ArrayList<>());
            } else {
                // index exists
                womanPlsList.addAll(fList.get(index));
            }
            tableDate.put("tableName", "table_" + index);
            tableDate.put("manPlsList", manPlsList);
            tableDate.put("womanPlsList", womanPlsList);
            tableMapList.add(tableDate);
        }

        // 查询专题下所有的脏器（去重查询）
        Map<String, Object> map2 = new HashMap<>();
        map2.put("specialId", specialId);
        map2.put("status", 1);
        map2.put("reasons", reasons);

        // 查询专题+reasons 下所有数据
        List<StatisticsBodyVo> dataAllList = specialDiagnosisMapper.getNeedStaticsListByParm(map2);
        // TODO 处理所有脏器组成
        List<Map<String, Integer>> staticsData = getTotalnMap(dataAllList);
        // System.out.println("第一次查表统计信息：" + JSONUtil.toJsonStr(staticsData.get(3)));

        // 专题下脏器统计信息查询
        List<StatisticsBodyVo> staticsVisceraList = specialDiagnosisMapper.getStaticsVisceraListByParm(map2);
        Map<String, Map<String, List<String>>> visceraMap = getVisceraMap(staticsVisceraList);
        // System.out.println("器官统计信息：" + JSONUtil.toJsonStr(visceraMap));

        // 表头处理-动态生成表头的所有行
        List<List<String>> headerList = autoHeader(fList, mList, maxFCount, maxMCount, tableCount);

        // 所有数据处理，按行处理，用|分隔
        List<List<String>> dataList = getAllRowDataList(tableMapList, specialId, visceraMap, staticsData);

        // 汇总table数据
        List<List<String>> tDataList = new ArrayList<>();
        for (int i = 0; i < headerList.size(); i++) {
            List<String> perList = new ArrayList<>();
            List<String> head = headerList.get(i);
            perList.addAll(head);
            List<String> data = dataList.get(i);
            perList.addAll(data);
            tDataList.add(perList);
        }
        /*
         * for(int j=0;j<tDataList.size();j++){ System.out.println("表："+(j+1)+
         * " 数据："+tDataList.get(j)); }
         */
        // ToolWord.test2(special,recordAddVO,dataList,tableMapList);

        // 创建专题报告文件夹
        String basePath = RPT_DIR + File.separator + specialId + File.separator + reasons;
        File rptDir = new File(basePath);
        if (!rptDir.exists()) {
            rptDir.mkdirs();
        }
        // 按专题下项目生成word报告
        String rptPath = basePath + File.separator + special.getSpecialNumber() + "_"
                + DateUtils.getDateToString(new Date(), "yyyy-MM-dd") + System.currentTimeMillis() + SUFFIX;
        if (CollectionUtils.isNotEmpty(tDataList)) {
            WordTool.generateWord(special, recordAddVO, tDataList, tableMapList, rptPath);
            log.info("路径：" + rptPath);
            System.out.println("-----------");
            log.info("数据1：" + JSONUtil.toJsonStr(tDataList));
            System.out.println("=====");
            log.info("tableMapList：" + JSONUtil.toJsonStr(tableMapList));
//			diagnosticReportService.createRpt(special, recordAddVO, tDataList, tableMapList, rptPath);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("程序运行时间： " + totalTime + " 毫秒");
            return rptPath;
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("程序运行时间： " + totalTime + " 毫秒");
        return MessageSource.M("NO_DATA_AVAILABLE");
    }

    public Map<String, Map<String, List<String>>> getVisceraMap(List<StatisticsBodyVo> dataAllList) {
        Map<String, Map<String, List<String>>> retMap = new HashMap<>();
        for (StatisticsBodyVo vo : dataAllList) {
            String sysVisceraName = vo.getSysVisceraName();
            String sysGradeName = vo.getSysGradeName();
            String sysLesionName = vo.getSysLesionName();
            String sysPositionName = vo.getSysPositionName();

            String dataKey = sysLesionName + "^" + sysPositionName;

            if (retMap.isEmpty()) {
                Map<String, List<String>> dtMap = new HashMap<>();
                List<String> graList = new ArrayList<>();
                graList.add(sysGradeName);
                dtMap.put(dataKey, graList);
                retMap.put(sysVisceraName, dtMap);
            } else {
                if (retMap.containsKey(sysVisceraName)) {
                    Map<String, List<String>> dtMap = new HashMap<>();
                    dtMap = retMap.get(sysVisceraName);
                    if (dtMap.isEmpty()) {
                        List<String> graList = new ArrayList<>();
                        graList.add(sysGradeName);
                        dtMap.put(dataKey, graList);
                        retMap.put(sysVisceraName, dtMap);
                    } else {
                        if (dtMap.containsKey(dataKey)) {
                            List<String> graList = dtMap.get(dataKey);
                            graList.add(sysGradeName);
                            dtMap.put(dataKey, graList);
                            retMap.put(sysVisceraName, dtMap);
                        } else {
                            List<String> graList = new ArrayList<>();
                            graList.add(sysGradeName);
                            dtMap.put(dataKey, graList);
                            retMap.put(sysVisceraName, dtMap);
                        }
                    }
                    /*
                     * List<String> list = dtMap.get(dataKey); if(null == list){
                     * list = new ArrayList<>(); }
                     * if(!list.contains(sysGradeName)){ list.add(sysGradeName);
                     * dtMap.put(sysVisceraName, list); }
                     */
                } else {
                    Map<String, List<String>> dtMap = new HashMap<>();
                    List<String> graList = new ArrayList<>();
                    graList.add(sysGradeName);
                    dtMap.put(dataKey, graList);
                    retMap.put(sysVisceraName, dtMap);
                }
            }

        }
        return retMap;
    }

    //
    public List<Map<String, Integer>> getTotalnMap(List<StatisticsBodyVo> dataAllList) {
        List<Map<String, Integer>> list = new ArrayList<>();
        Map<String, Integer> visceraMap = new HashMap<>();
        Map<String, Integer> visceraNoMap = new HashMap<>();
        Map<String, Integer> lesionAndPositionaMap = new HashMap<>();
        Map<String, Integer> lpgMap = new HashMap<>();
        for (StatisticsBodyVo vo : dataAllList) {
            String genderName = vo.getGenderName();
            String groupName = vo.getGroupName();
            String dosage = vo.getDosage();
            String headerKey = genderName + "_" + groupName + "_" + dosage;
            String sysVisceraName = vo.getSysVisceraName();
            String sysGradeName = vo.getSysGradeName();
            String sysLesionName = vo.getSysLesionName();
            String sysPositionName = vo.getSysPositionName();

            String visceraKey = headerKey + "_" + sysVisceraName;
            String lesionAndPositionKey = visceraKey + "_" + sysLesionName + "_" + sysPositionName;
            String lesionAndPositionGradeKey = lesionAndPositionKey + "_" + sysGradeName;
            String gradeKey = visceraKey + "_未见明显异常";
            // 器官诊断数量
            visceraMap = countData(visceraMap, visceraKey);
            // 未见明显异常
            if (sysGradeName.equalsIgnoreCase("未见明显异常")) {
                visceraNoMap = countData(visceraNoMap, gradeKey);
            } else {
                // 病理病变+部位
                lesionAndPositionaMap = countData(lesionAndPositionaMap, lesionAndPositionKey);

                // 病理病变+部位+级别
                lpgMap = countData(lpgMap, lesionAndPositionGradeKey);
            }
        }

        for (Map.Entry<String, Integer> entry : visceraMap.entrySet()) {
            // 病情key
            String lessionKey = entry.getKey();
            String gradeKey = lessionKey + "_未见明显异常";
            if (!visceraNoMap.containsKey(gradeKey)) {
                visceraNoMap.put(gradeKey, 0);
            }
        }
        // 脏器
        list.add(visceraMap);
        // 未见明显异常
        list.add(visceraNoMap);
        // 病理病变+部位
        list.add(lesionAndPositionaMap);
        // 病理病变+部位+级别
        list.add(lpgMap);
        return list;
    }

    public Map<String, Integer> countData(Map<String, Integer> hisMap, String key) {
        if (hisMap.containsKey(key)) {
            int total = hisMap.get(key);
            hisMap.put(key, total + 1);
        } else {
            hisMap.put(key, 1);
        }
        return hisMap;
    }

    /**
     * @Title: getAllRowDataList @Description: TODO @param @param tableMapList
     * 每个表的数据汇总，封装为map 分为manPlsList、womanPlsList、和tableName @param @param
     * specialId 专题id @param @param visceraMap 所有脏器信息 @param @param dataAllList
     * 专题下所有数据信息组合 @param @return @return List<List<String>> @throws
     */
    @SuppressWarnings("unchecked")
    public List<List<String>> getAllRowDataList(List<Map<String, Object>> tableMapList, Long specialId,
                                                Map<String, Map<String, List<String>>> visceraMap, List<Map<String, Integer>> staticsData) {
        List<List<String>> dataList = new ArrayList<>();

        for (int i = 0; i < tableMapList.size(); i++) {
            Map<String, Object> mapD = tableMapList.get(i);
            // String tableName = (String) mapD.get("tableName");
            List<StatisticsHeadVo> manPlsList = (List<StatisticsHeadVo>) mapD.get("manPlsList");
            List<StatisticsHeadVo> womanPlsList = (List<StatisticsHeadVo>) mapD.get("womanPlsList");

            // 总的组数据
            List<StatisticsHeadVo> totalGroupList = new ArrayList<>();
            totalGroupList.addAll(manPlsList);
            totalGroupList.addAll(womanPlsList);
            // 查询专题下所有的脏器（去重查询）
            List<String> rowList = new ArrayList<>();
            if (!visceraMap.isEmpty()) {
                for (Map.Entry<String, Map<String, List<String>>> entry : visceraMap.entrySet()) {
                    // 脏器
                    String viserctKey = entry.getKey();
                    // 第一行 脏器名称
                    String sysVisceraName = getVisceraName(viserctKey, totalGroupList);
                    sysVisceraName = "viscera_" + sysVisceraName;
                    rowList.add(sysVisceraName);
                    // 第二行
                    // 已诊断信息查询
                    String haveCheckInfo = getHaveCheckInfo(totalGroupList, viserctKey, specialId, staticsData);
                    rowList.add(haveCheckInfo);
                    // 第三行
                    // 未见明显异常
                    String noWorseInfo = getNoWorseInfo(totalGroupList, viserctKey, specialId, staticsData);
                    rowList.add(noWorseInfo);
                    // 具体诊断信息和病情程度
                    Map<String, List<String>> lessAndPositionMap = entry.getValue();
                    // 遍历totalLessionMap，然后分别去查询统计是否有值
                    for (Map.Entry<String, List<String>> lapEntry : lessAndPositionMap.entrySet()) {
                        // 病情key
                        String lessionKey = lapEntry.getKey();

                        // 根据lessionKey 去查询 lessionKey =
                        // sysLesionName+"|"+sysPositionName;
                        // String[] lessionPosition = lessionKey.split("^");
                        String sysLesionName = lessionKey.split("\\^")[0];

                        String sysPositionName = lessionKey.split("\\^")[1];

                        // 病情行信息和统计数据是lessionKey
                        lessionKey = getLessionInfo(totalGroupList, viserctKey, specialId, sysLesionName,
                                sysPositionName, lessionKey, staticsData);
                        rowList.add(lessionKey);

                        // 程度value
                        List<String> lessionGradeList = lapEntry.getValue();
                        for (int p = 0; p < lessionGradeList.size(); p++) {
                            // 程度值
                            String perGrade = lessionGradeList.get(p);
                            String gradeInfo = perGrade;
                            gradeInfo = getGradeInfoInfo(totalGroupList, viserctKey, specialId, sysLesionName,
                                    sysPositionName, gradeInfo, perGrade, staticsData);
                            rowList.add(gradeInfo);
                        }
                    }

                }
            }
            dataList.add(rowList);
        }
        return dataList;
    }

    public String getVisceraName(String sysVisceraName, List<StatisticsHeadVo> totalGroupList) {
        // 第一行 脏器名称
        for (int j = 0; j < totalGroupList.size(); j++) {
            sysVisceraName = sysVisceraName + "||" + "";
        }
        return sysVisceraName;
    }

    public String getHaveCheckInfo(List<StatisticsHeadVo> totalGroupList, String sysVisceraName, Long specialId,
                                   List<Map<String, Integer>> staticsData) {
        // 脏器
        // 未见明显异常
        // 病理病变+部位
        // 病理病变+部位+级别

        // 第二行
        // 已诊断信息查询
        String haveCheckInfo = "已诊断";
        // 根据specialId gender groupName dosage sysVisceraName 统计
        for (int n = 0; n < totalGroupList.size(); n++) {
            StatisticsHeadVo vo = totalGroupList.get(n);
            String genderName = vo.getSex();
            String groupName = vo.getGroupName();
            String dosage = vo.getDosage();
            // Map<String, Object> map3 = new HashMap<>();
            // map3.put("specialId", specialId);
            // map3.put("status", 1);
            // map3.put("gender", gender);
            // map3.put("groupName", groupName);
            // map3.put("dosage", dosage);
            // map3.put("sysVisceraName", sysVisceraName);
            // List<StatisticsBodyVo> diagnosedList =
            // specialDiagnosisMapper.getDiagnosedListByParm(map3);
            String headerKey = genderName + "_" + groupName + "_" + dosage;
            String visceraKey = headerKey + "_" + sysVisceraName;

            Map<String, Integer> doneMap = staticsData.get(0);

            String diagnosedTotal = "-";
            if (doneMap.containsKey(visceraKey)) {
                int total = doneMap.get(visceraKey);
                diagnosedTotal = String.valueOf(total);
            }
            haveCheckInfo = haveCheckInfo + "||" + diagnosedTotal;
        }
        return haveCheckInfo;
    }

    public String getNoWorseInfo(List<StatisticsHeadVo> totalGroupList, String sysVisceraName, Long specialId,
                                 List<Map<String, Integer>> staticsData) {
        // 脏器
        // 未见明显异常
        // 病理病变+部位
        // 病理病变+部位+级别
        // 第三行
        // 未见明显异常
        String noWorseInfo = "未见明显异常";
        for (int n = 0; n < totalGroupList.size(); n++) {
            StatisticsHeadVo vo = totalGroupList.get(n);
            String genderName = vo.getSex();
            String groupName = vo.getGroupName();
            String dosage = vo.getDosage();

            String headerKey = genderName + "_" + groupName + "_" + dosage;
            String visceraKey = headerKey + "_" + sysVisceraName;
            String gradeKey = visceraKey + "_未见明显异常";

            // Map<String, Object> map3 = new HashMap<>();
            // map3.put("specialId", specialId);
            // map3.put("status", 1);
            // map3.put("gender", gender);
            // map3.put("groupName", groupName);
            // map3.put("dosage", dosage);
            // map3.put("sysGrade", "0");
            // map3.put("sysVisceraName", sysVisceraName);
            // List<StatisticsBodyVo> diagnosedList =
            // specialDiagnosisMapper.getDiagnosedListByParm(map3);
            Map<String, Integer> noChangeMap = staticsData.get(1);

            String diagnosedTotal = "-";
            if (noChangeMap.containsKey(gradeKey)) {
                int total = noChangeMap.get(gradeKey);
                diagnosedTotal = String.valueOf(total);
            }
            noWorseInfo = noWorseInfo + "||" + diagnosedTotal;
        }
        return noWorseInfo;
    }

    public String getLessionInfo(List<StatisticsHeadVo> totalGroupList, String sysVisceraName, Long specialId,
                                 String sysLesionName, String sysPositionName, String lessionInfo, List<Map<String, Integer>> staticsData) {
        lessionInfo = lessionInfo.replaceAll("\\^", ";");
        // 脏器
        // 未见明显异常
        // 病理病变+部位
        // 病理病变+部位+级别
        // 异常信息
        for (StatisticsHeadVo vo : totalGroupList) {
            String genderName = vo.getSex();
            String groupName = vo.getGroupName();
            String dosage = vo.getDosage();

            String headerKey = genderName + "_" + groupName + "_" + dosage;
            String visceraKey = headerKey + "_" + sysVisceraName;
            String lesionAndPositionKey = visceraKey + "_" + sysLesionName + "_" + sysPositionName;
            // Map<String, Object> map3 = new HashMap<>();
            // map3.put("specialId", specialId);
            // map3.put("status", 1);
            // map3.put("gender", gender);
            // map3.put("groupName", groupName);
            // map3.put("dosage", dosage);
            // map3.put("sysVisceraName", sysVisceraName);
            // map3.put("sysLesionName", sysLesionName);
            // map3.put("sysPositionName", sysPositionName);
            // List<StatisticsBodyVo> diagnosedList =
            // specialDiagnosisMapper.getLessionStaticsListByParm(map3);
            Map<String, Integer> lesionPositionMap = staticsData.get(2);
            String diagnosedTotal = "-";
            if (lesionPositionMap.containsKey(lesionAndPositionKey)) {
                int total = lesionPositionMap.get(lesionAndPositionKey);
                diagnosedTotal = String.valueOf(total);
            }
            lessionInfo = lessionInfo + "||" + diagnosedTotal;
        }
        return lessionInfo;
    }

    public String getGradeInfoInfo(List<StatisticsHeadVo> totalGroupList, String sysVisceraName, Long specialId,
                                   String sysLesionName, String sysPositionName, String gradeInfo, String perGrade,
                                   List<Map<String, Integer>> staticsData) {
        // 脏器
        // 未见明显异常
        // 病理病变+部位
        // 病理病变+部位+级别
        // 异常程度信息
        for (StatisticsHeadVo vo : totalGroupList) {
            String genderName = vo.getSex();
            String groupName = vo.getGroupName();
            String dosage = vo.getDosage();
            String headerKey = genderName + "_" + groupName + "_" + dosage;
            String visceraKey = headerKey + "_" + sysVisceraName;
            String lesionAndPositionKey = visceraKey + "_" + sysLesionName + "_" + sysPositionName;
            String lesionAndPositionGradeKey = lesionAndPositionKey + "_" + perGrade;
            //			System.out.println("异常程度信息:" + lesionAndPositionGradeKey);

            // Map<String, Object> map3 = new HashMap<>();
            // map3.put("specialId", specialId);
            // map3.put("status", 1);
            // map3.put("gender", gender);
            // map3.put("groupName", groupName);
            // map3.put("dosage", dosage);
            // map3.put("sysVisceraName", sysVisceraName);
            // map3.put("sysLesionName", sysLesionName);
            // map3.put("sysPositionName", sysPositionName);
            // map3.put("sysGradeName", perGrade);
            // List<StatisticsBodyVo> diagnosedList =
            // specialDiagnosisMapper.getLessionGradeStaticsListByParm(map3);

            Map<String, Integer> lesionPositioGradenMap = staticsData.get(3);

            String diagnosedTotal = "-";
            if (lesionPositioGradenMap.containsKey(lesionAndPositionGradeKey)) {
                int total = lesionPositioGradenMap.get(lesionAndPositionGradeKey);
                diagnosedTotal = String.valueOf(total);
            }
            gradeInfo = gradeInfo + "||" + diagnosedTotal;
        }
        return gradeInfo;
    }

    // 表头处理
    public List<List<String>> autoHeader(List<List<StatisticsHeadVo>> fList, List<List<StatisticsHeadVo>> mList,
                                         int maxFCount, int maxMCount, int tableCount) {
        List<List<String>> headerList = new ArrayList<>();

        List<Map<String, Object>> r2List = new ArrayList<>();
        for (int i = 0; i < tableCount; i++) {
            List<StatisticsHeadVo> totalList = new ArrayList<>();
            // List<StatisticsHeadVo> womanPlsList = new ArrayList<>();
            Map<String, Object> remberMap = new HashMap<>();
            if (i >= maxMCount) {
                // index not exists
                // manPlsList.addAll(new ArrayList<>());
            } else {
                // index exists
                totalList.addAll(mList.get(i));
                remberMap.put("1", mList.get(i).size());
            }
            if (i >= maxFCount) {
                // index not exists
                // womanPlsList.addAll(new ArrayList<>());
            } else {
                // index exists
                totalList.addAll(fList.get(i));
                remberMap.put("0", fList.get(i).size());
            }
            remberMap.put("totalList", totalList);
            r2List.add(remberMap);
        }

        // List<List<String>> totalDataList = new ArrayList<>();
        for (Map<String, Object> pmap : r2List) {
            List<StatisticsHeadVo> totalList = (List<StatisticsHeadVo>) pmap.get("totalList");

            List<String> rowDataList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                String rowInfo = "";
                if (i < 3) {
                    rowInfo = "移走原因：给药结束后安乐死";
                }
                if (i == 3) {
                    rowInfo = "检查切片数量：";
                }
                for (StatisticsHeadVo hv : totalList) {
                    if (i == 0) {
                        // 性别（0雌，1雄）
                        int gender = hv.getGender();
                        String genderStr = "雌性";
                        if (gender == 1) {
                            genderStr = "雄性";
                        }
                        rowInfo = rowInfo + "||" + genderStr;
                    } else if (i == 1) {
                        rowInfo = rowInfo + "||" + hv.getGroupName();
                    } else if (i == 2) {
                        rowInfo = rowInfo + "||" + hv.getDosage();
                    } else if (i == 3) {
                        rowInfo = rowInfo + "||" + hv.getTotal();
                    }
                }
                rowDataList.add(rowInfo);

            }
            headerList.add(rowDataList);
            // String json = JSONUtil.toJsonStr(rowDataList);
            // System.out.println("====开始===");

            // System.out.println(rowDataList);
            //			String arb = rowDataList.get(0);
            // System.out.println("行数是："+rowDataList.size()+" arb:"+arb+"
            // 列数是："+arb.split("\\|").length);
            // System.out.println("====结束===");
            // System.out.println("");
        }
        return headerList;
    }

}
