package cn.staitech.anno.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangf
 */

public enum ReportRecordEnum {

    TYPE_1(1, "单切片报告"),
    TYPE_2(2, "组间报告"),
    TYPE_3(3, "脏器病变报告");

    private int value;
    private String label;

    ReportRecordEnum(int value, String label) {
        this.label = label;
        this.value = value;
    }


    public static String getEnumLabelByValue(Integer value) {

        if (value == null) {
            return StringUtils.EMPTY;
        }
        for (ReportRecordEnum item : ReportRecordEnum.values()) {

            if (item.value == value) {
                return item.label;
            }
        }
        return StringUtils.EMPTY;
    }


    public int value() {
        return value;
    }

    public String label() {
        return label;
    }

}
