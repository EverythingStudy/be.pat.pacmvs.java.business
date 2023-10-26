package cn.staitech.anno.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangf
 */

public enum ReasonsEnum {

    /**
     * 给药结束安乐死
     */
    reasons_1(1, "给药结束安乐死"),
    /**
     * 恢复期结束安乐死
     */
    reasons_2(2, "恢复期结束安乐死");

    private int value;
    private String label;

    ReasonsEnum(int value, String label) {
        this.label = label;
        this.value = value;
    }

    public static String getEnumLabelByValue(Integer value) {

        if (value == null) {
            return StringUtils.EMPTY;
        }
        for (ReasonsEnum item : ReasonsEnum.values()) {

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
