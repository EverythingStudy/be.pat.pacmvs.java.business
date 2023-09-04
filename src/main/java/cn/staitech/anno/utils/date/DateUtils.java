package cn.staitech.anno.utils.date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



/**
 * 时间格式化
 */
public class DateUtils {

    private static Logger logger = LogManager.getLogger(DateUtils.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdfLongTimeS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static SimpleDateFormat sdfLongTime = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat sdfLongTimePlusMill = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
    private static SimpleDateFormat sdfLongU = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);
    private static long DAY_IN_MILLISECOND = 0x5265c00L;

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public DateUtils() {
    }

    /**
     * 获取英式时间格式
     *
     * @param date
     * @return Jun 12,2018
     */
    public static String getDateUS(Date date) {
        return sdfLongU.format(date);
    }

    /**
     * 获取当前日期格式字符串
     *
     * @param pattern yyyyMMdd、yyyyMMddHHmmss、yyyy-MM-dd HH:mm:ss:SSSS、yyyy年MM月dd日 HH:mm
     * @param pattern yyyy-MM-dd、yyyy年MM月dd日、MMM dd、MMM dd,yyyy
     * @return
     */
    public static String getCurrentDateString(String pattern) throws Exception {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    /**
     * 获取当前日期格式字符串
     *
     * @param pattern HH:mm:ss
     * @return
     */
    public static String getCurrentHHmmssString(String pattern) throws Exception {
        return new SimpleDateFormat(pattern).format(new Date());
    }


    /**
     * 获取系统时间的前一天
     *
     * @return
     */
    public static String getBeforeDateString() {
        try {
            Calendar calendar = Calendar.getInstance();
            int today = calendar.getTime().getDay();
            if (today == calendar.getFirstDayOfWeek()) {
                calendar.roll(Calendar.DAY_OF_YEAR, -3);
            } else {
                calendar.roll(Calendar.DAY_OF_YEAR, -1);
            }
            String yesterday = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
            return yesterday;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 功能：根据日期串和格式获取日期
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return
     * @throws Exception
     */
    public static Date getDateByDateStr(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取日期格式字符串
     *
     * @param pattern yyyyMMdd,yyyyMMddHHmmss
     * @return
     */
    public static String getDateToString(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 两个时间相差距离多少分
     *
     * @param startTime 时间参数 1 格式：1990-01-01 12:00:00
     * @param endTime   时间参数 2 格式：2009-01-01 12:00:00
     * @return long 返回值为：分
     */
    public static Long toMinutesApart(String startTime, String endTime) {
        try {
            Date begin = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
            return between / 60;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 两个时间相差距离多少秒
     *
     * @param startTime 时间参数 1 格式：1990-01-01 12:00:00
     * @param endTime   时间参数 2 格式：2009-01-01 12:00:00
     * @return long 返回值为：秒
     */
    public static Long toSecondApart(String startTime, String endTime) {
        try {
            Date begin = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
            return between;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * string时间类型转换为long时间类型
     *
     * @param strTime    时间参数 1 格式：2017-12-30 15:30:30
     * @param formatType 时间参数 2 格式：yyyy-MM-dd HH:mm:ss
     * @param strTime    时间格式和formatType的时间格式必须相同
     * @return long 返回值为：分
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime(); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * string时间类型转换为date时间类型
     *
     * @param strTime    要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * @param formatType 时间格式
     * @param strTime    时间格式和formatType的时间格式必须相同
     * @return date
     */
    public static Date stringToDate(String strTime, String formatType) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 获取时间字符串（精确到微妙）
     *
     * @return
     */
    public static String getLongTimeS() {
        return sdfLongTimeS.format(new Date());
    }

    /**
     * 获取时间字符串(精确到秒)
     *
     * @return
     */
    public static String getLongTime() {
        return sdfLongTime.format(new Date());
    }

    /**
     * 取得当前日期到毫秒
     *
     * @param date
     * @return
     */
    public static String getPlusTimeMill(Date date) {
        String nowDate = "";
        try {
            nowDate = sdfLongTimePlusMill.format(date);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return nowDate;
    }

    /**
     * 方法描述:将long类型的时间转换成String类型
     *
     * @param time
     * @return date
     */
    public static String getFormatDateLong(Long time) {
        String date = sdf.format(time);
        return date;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * @
     * @ 获取当月的第一天，2009-05-01
     */
    public static String getStringOfFirstDayInMonth() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String temp = sdf.format(date);
        String firstDayInMoth = "";
        firstDayInMoth = temp + "-01";
        return firstDayInMoth;
    }

    /**
     * @param date
     * @param days
     * @return DATE 型加具体的天数
     * @author zhangyong
     */
    public static Date dateAddDays(Date date, int days) {
        long now = date.getTime() + (long) days * DAY_IN_MILLISECOND;
        return new Date(now);
    }

    /**
     * 根据秒数返回时分秒
     *
     * @param _second 秒数
     * @return String
     * @throws Exception
     */
    public static String getTimeBySecond(String _second) throws Exception {
        String returnTime = "";
        long longHour = 0;
        long longMinu = 0;
        long longSec = 0;
        try {
            longSec = Long.parseLong(_second);
            if (longSec == 0) {
                returnTime = "0时0分0秒";
                return returnTime;
            }
            longHour = longSec / 3600; // 取得小时数
            longSec = longSec % 3600; // 取得余下的秒
            longMinu = longSec / 60; // 取得分数
            longSec = longSec % 60; // 取得余下的秒
            returnTime = longHour + "时" + longMinu + "分" + longSec + "秒";
            return returnTime;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * pablo 根据毫秒数返回时分秒毫秒
     *
     * @param ms_second 秒数
     * @return String
     * @throws Exception
     */
    public static String getTimeBySecond(long ms_second) throws Exception {
        String returnTime = "";
        long longHour = 0;
        long longMinu = 0;
        long longSec = 0;
        long longMs = ms_second;
        try {
            if (longMs == 0) {
                returnTime = "0时0分0秒0毫秒";
                return returnTime;
            }
            longHour = longMs / 3600000; // 取得小时数
            longMs = longMs % 3600000; // 取得余下的毫秒
            longMinu = longMs / 60000; // 取得分数
            longMs = longMs % 60000; // 取得余下的毫秒
            longSec = longMs / 1000; // 取得余下的秒
            longMs = longMs % 1000; // 取得余下的毫秒
            returnTime = longHour + "时" + longMinu + "分" + longSec + "秒"
                    + longMs + "毫秒";
            return returnTime;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取当前年为整型
     *
     * @return 获取当前日期中的年，int型 2018
     */
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        return currentYear;
    }

    /**
     * 获取当前月为整型
     *
     * @return 获取当前日期中的月，int型 6
     */
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        return currentMonth + 1;
    }

    /**
     * Returns a Date using the passed-in string and format. Returns null if the
     * string is null or empty or if the format is null. The string must match
     * the format.
     */
    public static Date parse(String aValue, SimpleDateFormat aFormat)
            throws ParseException {
        if (StringUtils.isEmpty(aValue) || aFormat == null) {
            return null;
        }

        return aFormat.parse(aValue);
    }

    /**
     * 字符串形式转化为Date类型 String类型按照format格式转为Date类型
     */
    public static Date fromStringToDate(Date date) throws ParseException {
        return sdf.parse(sdf.format(date));
    }

    /**
     * 字符串形式转化为Date类型 String类型按照format格式转为Date类型
     */
    public static Date fromStringToDate(String format, String dateTime)
            throws ParseException {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        date = sdf.parse(dateTime);
        return date;
    }

    /**
     * String类型按照format格式转为UTC类型
     *
     * @param format
     * @param dateTime
     * @return String
     * @throws Exception
     * @author: xbs
     * @date:2018-10-29 15:59:28
     */
    public static String fromStringToUtc(String format, String dateTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return sdf.format(fromStringToDate(format, dateTime));
    }

    /**
     * 得到两个日期之间相差的天数
     *
     * @param newDate 大的日期
     * @param oldDate 小的日期
     * @return newDate-oldDate相差的天数
     */
    public static int daysBetweenDates(Date newDate, Date oldDate) {
        int days = 0;
        Calendar calo = Calendar.getInstance();
        Calendar caln = Calendar.getInstance();
        calo.setTime(oldDate);
        caln.setTime(newDate);
        int oday = calo.get(Calendar.DAY_OF_YEAR);
        int nyear = caln.get(Calendar.YEAR);
        int oyear = calo.get(Calendar.YEAR);
        while (nyear > oyear) {
            calo.set(Calendar.MONTH, 11);
            calo.set(Calendar.DATE, 31);
            days = days + calo.get(Calendar.DAY_OF_YEAR);
            oyear = oyear + 1;
            calo.set(Calendar.YEAR, oyear);
        }
        int nday = caln.get(Calendar.DAY_OF_YEAR);
        days = days + nday - oday;

        return days;
    }

    /**
     * String类型按照format格式转为Calendar类型
     *
     * @param format
     * @param dateTime
     * @return String
     * @throws Exception
     * @author: xbs
     * @date:2018-11-28 13:37:28
     */
    public static Calendar fromStringDateToCalendar(String format, String dateTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 当前日期加上天数后的日期
     *
     * @param num 值可为正负(正：加多少天数；负：减多少天数；)
     * @return
     */
    public static Date addHowManyDay(int num) {
        Date d = new Date();
        String currdate = sdf.format(d);
        Calendar ca = Calendar.getInstance();
        //num为增加的天数，可以改变的
        ca.add(Calendar.DATE, num);
        d = ca.getTime();
        String enddate = sdf.format(d);
        return d;
    }

    /**
     * 时间加上或者减去几小时
     *
     * @param date 时间
     * @param hour 小时
     * @return Date
     */
    public static Date dayPlusHours(Date date, int hour) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 24小时制，-标识负数
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    /**
     * 当前月份加上或者减去多少月份
     *
     * @param num 值可为正负(正：加多少月份；负：减多少月份；)
     * @return Date
     */
    public static Date addHowManyDate(int num) {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        //将当前日期加一个月
        c.add(Calendar.MONTH, num);
        //返回Date型的时间
        return c.getTime();
    }

    /**
     * 当前月份加上或者减去多少月份
     *
     * @param num 值可为正负(正：加多少月份；负：减多少月份；)
     * @return String
     */
    public static String addHowManyString(int num) {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        //将当前日期加一个月
        c.add(Calendar.MONTH, num);
        //返回String型的时间
        return sdf.format(c.getTime());
    }

    /**
     * @param dateStr
     * @return
     */
    public static DateFormat getDateFmByParam(String dateStr) {
        DateFormat dateFm = null;
        //
        if (dateStr.contains("-") && dateStr.contains(":")) {
            dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFm;
        }
        if (dateStr.contains("-") && !dateStr.contains(":")) {
            dateFm = new SimpleDateFormat("yyyy-MM-dd");
            return dateFm;
        }
        //
        if (dateStr.contains(".") && dateStr.contains(":")) {
            dateFm = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            return dateFm;
        }
        if (dateStr.contains(".") && !dateStr.contains(":")) {
            dateFm = new SimpleDateFormat("yyyy.MM.dd");
            return dateFm;
        }
        //
        if (dateStr.contains("/") && dateStr.contains(":")) {
            dateFm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return dateFm;
        }
        if (dateStr.contains("/") && !dateStr.contains(":")) {
            dateFm = new SimpleDateFormat("yyyy/MM/dd");
            return dateFm;
        }
        dateFm = new SimpleDateFormat(DEFAULT_PATTERN);
        return dateFm;
    }

    /**
     * 获取指定时间的前后几分钟
     *
     * @param minute
     * @return
     */
    public static Date getTimeByMinute(Date d, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }


    /**
     * 获取指定时间的前后某一年或某一个月或某一天
     *
     * @param d
     * @param n    数值
     * @param unit 单位   1年  2月   3天   4小时  5分钟    6秒
     * @return
     */
    public static Date getSomeDate(Date d, int n, int unit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        switch (unit) {
            case 1:
                calendar.add(Calendar.YEAR, n);
                break;
            case 2:
                calendar.add(Calendar.MONTH, n);
                break;
            case 3:
                calendar.add(Calendar.DATE, n);
                break;
            case 4:
                calendar.add(Calendar.HOUR, n);
                break;
            case 5:
                calendar.add(Calendar.MINUTE, n);
                break;
            case 6:
                calendar.add(Calendar.SECOND, n);
                break;
        }
        return calendar.getTime();
    }

    /**
     * 获取当前日期格式字符串
     *
     * @param pattern
     * @return
     */
    public static String getDateStringByPattern(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 获取开始时间到结束时间中的每一天
     * @param start 开始时间
     * @param end 结束时间
     * @return 返回日期集合
     */
    public static List<Date> getDaysByStartEnd(String start, String end) throws ParseException {
        List<Date> list = new ArrayList<>();
        Calendar tempStart =Calendar.getInstance();
        tempStart.setTime(sdfYMD.parse(start));
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(sdfYMD.parse(end));
        while (tempStart.before(tempEnd)) {
            list.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return list;
    }

    /**
     * 获取开始时间到结束时间中的每一天字符串
     * @param start 开始时间
     * @param end 结束时间
     * @return 返回日期字符串集合
     */
    public static List<String> getDaysStringByStartEnd(String start, String end) throws ParseException {
        List<String> list = new ArrayList<>();
        Calendar tempStart =Calendar.getInstance();
        tempStart.setTime(sdfYMD.parse(start));

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(sdfYMD.parse(end));
        while (tempStart.before(tempEnd) || tempStart.equals(tempEnd)) {
            list.add(getDateToString(tempStart.getTime(), "yyyy-MM-dd"));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return list;
    }
}
