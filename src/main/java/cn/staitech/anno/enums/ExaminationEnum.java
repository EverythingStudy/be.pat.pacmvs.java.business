package cn.staitech.anno.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 切片复核枚举
 *
 * @author YL
 */
public enum ExaminationEnum {
    /**
     * 提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核不通过 4交付)
     */
    STATUS_INFO_0(0, "未复核"),
    STATUS_INFO_1(1, "复核中"),
    STATUS_INFO_2(2, "已复核"),
    STATUS_INFO_3(3, "驳回"),
    STATUS_INFO_4(4, "交付");
    private final int value;
    private final String label;

    ExaminationEnum(int value, String label) {
        this.label = label;
        this.value = value;
    }

    public static String getEnumlabelByValue(Integer value) {

        if (value == null) {
            return StringUtils.EMPTY;
        }
        for (ExaminationEnum item : ExaminationEnum.values()) {

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
