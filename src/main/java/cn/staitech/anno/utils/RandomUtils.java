package cn.staitech.anno.utils;


/**
 * @author gjt.
 */
public class RandomUtils {

    /**
     * 获取两位随机数字
     *
     * @return number
     */
    public static int RandomNumbers() {
        return (int) (Math.random() * 90 + 10);
    }
}
