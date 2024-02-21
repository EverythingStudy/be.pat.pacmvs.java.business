package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.AccessProjectRecords;
import cn.staitech.anno.mapper.AccessProjectRecordsMapper;
import cn.staitech.anno.service.AccessProjectRecordsService;
import cn.staitech.anno.vo.accessprojectrecords.AccessProjectRecordsIn;
import cn.staitech.anno.vo.accessprojectrecords.AccessProjectRecordsOut;
import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 业务层处理：项目访问记录
 *
 * @author zmj
 * @date 2024-02-20
 */
@Service
public class AccessProjectRecordsServiceImpl implements AccessProjectRecordsService {
    @Resource
    private AccessProjectRecordsMapper accessProjectRecordsMapper;


    /**
     * 查询
     * */
    public List<AccessProjectRecordsOut> accessRecords(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        // 过去一月
        c.setTime(new Date());
        String today = format.format(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);

        //获取过去一个月的日期
        List<String> result = getBetweenDates(mon, today, false,true);
        List<String> results = getBetweenDates(mon, today, false,false);
        Map<String, Object> timeParams=new HashMap<>();
        timeParams.put("beginTime",result.get(0));
        timeParams.put("endTime",result.get(result.size()-1));
        AccessProjectRecordsIn accessProjectRecordsIn= AccessProjectRecordsIn.builder().userId(SecurityUtils.getUserId()).timeParams(timeParams).build();
        //根据开始和结束日期和用户查询
        List<AccessProjectRecordsOut>records=accessProjectRecordsMapper.accessRecords(accessProjectRecordsIn);
        Map<String,AccessProjectRecordsOut>recordsOutMap=records.stream().collect(Collectors.toMap(AccessProjectRecordsOut::getAccessTime, Function.identity()));
        List<AccessProjectRecordsOut> accessList=new ArrayList<>();
        //遍历判断给没有访问的量的日期赋值为0
        for (String access:results){
            if (recordsOutMap.containsKey(access)){
                accessList.add(recordsOutMap.get(access));
            }else{
                AccessProjectRecordsOut accessProjectRecordsOut= AccessProjectRecordsOut.builder().accessTime(access).num(0).build();
                accessList.add(accessProjectRecordsOut);
            }
        }
        return accessList;
    }

    /**
     * 过去一个月的时间
     * */
    public static List<String> getBetweenDates(String startTime, String endTime, boolean isIncludeStartTime,boolean types) {
        List<String> result = new ArrayList<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // 定义起始日期
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
                // 定义结束日期 可以去当前月也可以手动写日期。
            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
            // 定义日期实例
            Calendar dd = Calendar.getInstance();
            // 设置日期起始时间
            dd.setTime(d1);
            if (isIncludeStartTime) {
                result.add(format.format(d1));
            }
            // 判断是否到结束日期
            while (dd.getTime().before(d2)) {
                // 进行当前日期加1
                dd.add(Calendar.DATE, 1);
                SimpleDateFormat sdf;
                if (types){
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                }else{
                    sdf = new SimpleDateFormat("MM/dd");
                }
                String str = sdf.format(dd.getTime());
                result.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
