package cn.staitech.anno.enums;


/**
 * @author wangf
 */

public enum SysDictTypeEnum {
    /**
     * sys_viscera
     */
    organization(1, "sys_viscera"),
    /**
     * sys_position
     */
    position(2, "sys_position"),
    /**
     * sys_lesion
     */
    lesion(3, "sys_lesion"),
    /**
     * sys_ddefinition
     */
    ddefinition(4, "sys_ddefinition"),
    /**
     * sys_grade
     */
    grade(5, "sys_grade"),
    /**
     * sys_viscera_organization
     */
    sysvisceraorganization(6, "sys_viscera_organization");

    private final int value;
    private final String label;

    SysDictTypeEnum(int value, String label) {
        this.label = label;
        this.value = value;
    }


    public int value() {
        return value;
    }

    public String label() {
        return label;
    }
}
