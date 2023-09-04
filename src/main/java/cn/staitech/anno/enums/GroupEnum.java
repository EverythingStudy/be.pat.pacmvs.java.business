package cn.staitech.anno.enums;


import org.apache.commons.lang3.StringUtils;

public enum GroupEnum {
    gender_0(0, "雌性"),
    gender_1(1, "雄性");

    private int value;
    private String label;

    GroupEnum(int value, String label) {
        this.label = label;
        this.value = value;
    }
    public static String getEnumLabelByValue(Integer value) {

        if (value == null) {
            return StringUtils.EMPTY;
        }
        for (GroupEnum item : GroupEnum.values()) {

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
