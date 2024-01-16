package cn.staitech.anno.utils;

import static cn.staitech.anno.utils.DateUtils.MillisDefaultZone;
import static cn.staitech.anno.utils.RandomUtils.RandomNumbers;

/**
 * @author gjt
 */
public class CustomizationIdUtils {

    private static final String AI = "ai";
    private static final String SD = "sd";
    private static final String CL = "cl";
    private static final String LABEL_NAME = "labelname";
    private static final String MEASURE_NAME = "measure_name";

    public static String getAiId() {
        return AI + LABEL_NAME + MillisDefaultZone() + RandomNumbers();
    }

    public static String getSdId() {
        return SD + LABEL_NAME + MillisDefaultZone() + RandomNumbers();
    }

    public static String getClId() {
        return CL + MEASURE_NAME + MillisDefaultZone() + RandomNumbers();
    }
}
