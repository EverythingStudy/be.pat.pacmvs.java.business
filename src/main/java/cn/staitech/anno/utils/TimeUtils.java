package cn.staitech.anno.utils;

import java.text.SimpleDateFormat;
import java.time.Clock;
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

//    public static void main(String[] args) {
//        System.out.println(CurrentTime());
//    }

}
