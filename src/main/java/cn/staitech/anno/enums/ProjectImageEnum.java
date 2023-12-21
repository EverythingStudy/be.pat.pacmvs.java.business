package cn.staitech.anno.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 标注处理枚举
 */
public enum ProjectImageEnum implements ProcessFlagValue {
    NOSTART(0, "未开始"),
    CONDUNT(1, "标注中"),
    COMPLETE(2, "标注完成"),
    SUBMITTED(3, "已提交复核");

    private final int value;

    private final String label;

    ProjectImageEnum(int value, String label) {
        this.label = label;
        this.value = value;
    }

    public static String getEnumStatusByValue(Integer value) {

        if (value == null) {
            return StringUtils.EMPTY;
        }
        for (ProjectImageEnum item : ProjectImageEnum.values()) {

            if (item.getValue() == value) {
                return item.getLabel();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * @return
     */
    public String getLabel() {
        return label;
    }

}
