package cn.staitech.anno.utils;

import static cn.staitech.anno.utils.RandomUtils.RandomNumbers;
import static cn.staitech.anno.utils.TimeUtils.MillisDefaultZone;

public class CustomizationIdUtils {

    private static final String AI = "ai";
    private static final String SD = "sd";
    private static final String CL = "cl";
    private static final String LABEL_NAME = "labelname";
    private static final String MEASURE_NAME = "measure_name";

    public static void main(String[] args) {
        String res = AI + LABEL_NAME + MillisDefaultZone() + RandomNumbers();
        System.out.println(res);
    }

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
