package cn.staitech.anno.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Calendar;
import java.util.Date;

/**
 * @author gjt.
 * @data 2023/5/26 16:50
 */
public class TimeUtils {

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

    /**
     * 获取当前时间
     *
     * @return date
     */
    public static String CurrentTime() {
        Date date = new Date();
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return st.format(date);
    }


    public static String timeIncreases(String date) {
        Date time = conversionDate(date);
        Calendar cal = Calendar.getInstance();
        //设置起时间
        cal.setTime(time);
        // 增加三十天
        cal.add(Calendar.DATE, 30);
        return conversionString(cal.getTime());

    }

    public static String dateIncreases() {
        Date time = new Date();
        Calendar cal = Calendar.getInstance();
        //设置起时间
        cal.setTime(time);
        // 增加三十天
        cal.add(Calendar.DATE, 30);
        return conversionString(cal.getTime());

    }

    /**
     * 将String类型转化为Date
     *
     * @param time 时间
     * @return Date
     */
    public static Date conversionDate(String time) {
        //定义转化为字符串的日期格式
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = st.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //将时间转化为类似 2020-02-13 16:01:30 格式的字符串
        return date;
    }

    /**
     * 将String类型转化为Date,格式为年月日 时分
     *
     * @param time 时间
     * @return Date
     */
    public static Date conversionDates(String time) {
        //定义转化为字符串的日期格式
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = st.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 将Date类型转化为time
     *
     * @param time 时间
     * @return Date
     */
    public static String conversionString(Date time) {
        //定义转化为字符串的日期格式
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return st.format(time);
    }

    /**
     * 将Date类型转化为time
     *
     * @param time 时间
     * @return Date
     */
    public static String conversionStrings(Date time) {
        //定义转化为字符串的日期格式
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return st.format(time);
    }

    public static void main(String[] args) {
        System.out.println(CurrentTime());
    }


}
