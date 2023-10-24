package cn.staitech.anno.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 张争
 * @date 2022/12/23 15:02
 */
@Getter
@AllArgsConstructor
public enum ProjectMemberEnum {
    CONTRIBUTOR(1, "CONTRIBUTOR"),
    CONTROLLER(2, "CONTROLLER"),
    REPRESENTATIVE(3, "REPRESENTATIVE");

    private int value;
    private String label;

    public static String getEnumLabelByValue(Integer value) {

        if (value == null) {
            return StringUtils.EMPTY;
        }
        for (ProjectMemberEnum item : ProjectMemberEnum.values()) {

            if (item.getValue() == value) {
                return item.getLabel();
            }
        }
        return StringUtils.EMPTY;
    }

    public static Integer getEnumValueByLabel(String label) {

        if (StringUtils.isEmpty(label)) {
            return null;
        }
        for (ProjectMemberEnum item : ProjectMemberEnum.values()) {

            if (item.getLabel().equals(label)) {
                return item.getValue();
            }
        }
        return null;
    }
}
