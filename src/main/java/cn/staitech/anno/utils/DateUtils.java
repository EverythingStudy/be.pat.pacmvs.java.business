package cn.staitech.anno.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;


/**
 * 时间格式化
 */
public class DateUtils {
    public DateUtils() {
    }

    /**
     * 获取当前日期格式字符串
     *
     * @param pattern HH:mm:ss
     * @return
     */
    public static String getCurrentHHmmssString(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    /**
     * 日期格式处理
     *
     * @param dateTime
     * @param n
     * @return
     */
    public static Date addAndSubtractDaysByCalendar(Date dateTime/*待处理的日期*/, int n/*加减天数*/) {
        java.util.Calendar calstart = java.util.Calendar.getInstance();
        calstart.setTime(dateTime);
        calstart.add(java.util.Calendar.DAY_OF_WEEK, n);
        return calstart.getTime();
    }
    public static Date addAndSubtractDaysByString(String dateTime/*待处理的日期*/,int n/*加减天数*/) throws ParseException {
        java.util.Calendar calstart = java.util.Calendar.getInstance();
        Date date = stringToDate(dateTime, "yyyy-MM-dd");
        calstart.setTime(date);
        calstart.add(java.util.Calendar.DAY_OF_WEEK, n);

        return calstart.getTime();
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
     * 使用clock.millis获取毫秒的时间戳
     *
     * @return millis 毫秒时间戳
     */
    public static Long MillisDefaultZone() {
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();
        return millis;
    }
}
