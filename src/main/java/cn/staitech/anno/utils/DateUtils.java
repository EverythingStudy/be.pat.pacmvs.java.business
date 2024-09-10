package cn.staitech.anno.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author staitech
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
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

    public static Date addAndSubtractDaysByGetTime(Date dateTime/*待处理的日期*/, int n/*加减天数*/){

        //日期格式
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //System.out.println(df.format(new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L)));
        //System.out.println(dd.format(new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L)));
        //注意这里一定要转换成Long类型，要不n超过25时会出现范围溢出，从而得不到想要的日期值
        return new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L);
    }

    public static Date addAndSubtractDaysByCalendar(Date dateTime/*待处理的日期*/,int n/*加减天数*/){

        //日期格式
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calstart = Calendar.getInstance();
        calstart.setTime(dateTime);

        calstart.add(Calendar.DAY_OF_WEEK, n);

        //System.out.println(df.format(calstart.getTime()));
        //System.out.println(dd.format(calstart.getTime()));
        return calstart.getTime();
    }

    public static Date addAndSubtractDaysByString(String dateTime/*待处理的日期*/,int n/*加减天数*/) throws ParseException {

        Calendar calstart = Calendar.getInstance();
        Date date = stringToDate(dateTime, "yyyy-MM-dd");
        calstart.setTime(date);

        calstart.add(Calendar.DAY_OF_WEEK, n);

        //System.out.println(df.format(calstart.getTime()));
        //System.out.println(dd.format(calstart.getTime()));
        return calstart.getTime();
    }

    /**
     * 使用前需要判断StartTime和endTime不为空
     * @param StartTime
     * @param endTime
     * @return
     */
    public static String getDateYearAndMonth(Date StartTime, Date endTime) {
        LocalDate beginTime = StartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate time = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period duration = Period.between(beginTime, time);
        int years = duration.getYears();// 转换为年数
        int months = duration.getMonths();
        return years + "年 " + months + "月 ";
    }

    /**
     * 将String类型日期转换为LocalDate对象
     *
     * @param dateString 要转换的String类型日期
     * @return 转换后的LocalDate对象
     */
    public static LocalDate convertToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * 将Date对象转换为LocalDate
     *
     * @param date 要转换的Date对象
     * @return 转换后的LocalDate对象
     */
    public static LocalDate convertToLocalDate(Date date) {
        Instant instant = date.toInstant(); // 将Date转换为Instant
        ZoneId zoneId = ZoneId.systemDefault(); // 获取系统默认时区
        return instant.atZone(zoneId).toLocalDate(); // 在指定时区转换为LocalDate
    }

    /**
     * 去掉时分秒并加1天
     *
     * @param time
     * @return
     */
    public static Date addOneDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 获取格式化后的日期时间字符串
     *
     * @param dateString 日期字符串
     * @param isEnd      true获取结束时间23:59:59
     *                   false获取开始时间00:00:00
     * @return 格式化后的日期时间字符串
     */
    public static String getFormattedDateTime(String dateString, boolean isEnd) {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        LocalDateTime dateTime;
        if (isEnd) {
            dateTime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        } else {
            dateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
