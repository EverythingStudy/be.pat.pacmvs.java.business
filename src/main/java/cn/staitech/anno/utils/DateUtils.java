package cn.staitech.anno.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 时间格式化
 */
public class DateUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfLongTimeS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final SimpleDateFormat sdfLongTime = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat sdfLongTimePlusMill = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");

    public DateUtils() {
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
}
