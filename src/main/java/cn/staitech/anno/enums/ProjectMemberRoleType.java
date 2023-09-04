package cn.staitech.anno.enums;

import org.apache.commons.lang3.StringUtils;

/**
 *  项目角色类型
 * @author 王峰
 */
public enum ProjectMemberRoleType {
    REPRESENTATION(1, "项目代表"),
    MANAGER(2, "项目管理者"),
    CONTRIBUTOR(3, "项目贡献者");

    private int value;

    private String label;

    ProjectMemberRoleType(int value, String label) {
        this.label = label;
        this.value = value;
    }

    public static String getProjectMemberRoleType(Integer value) {

        if (value == null) {
            return StringUtils.EMPTY;
        }
        for (ProjectMemberRoleType item : ProjectMemberRoleType.values()) {

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
