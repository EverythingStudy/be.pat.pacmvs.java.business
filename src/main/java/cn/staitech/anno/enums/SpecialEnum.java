package cn.staitech.anno.enums;

/**
 * @author gjt.
 * &#064;data  2023/5/29 13:37
 */
public enum SpecialEnum {

    /**
     * 正常
     */
    DEL_FLAG_0(0L, "正常"),
    /**
     * 回收站
     */
    DEL_FLAG_1(1L, "回收站"),
    /**
     * 删除
     */
    DEL_FLAG_2(2L, "删除");

    private Long value;
    private String label;

    SpecialEnum(Long value, String label) {
        this.label = label;
        this.value = value;
    }

    public Long value() {
        return value;
    }

    public String label() {
        return label;
    }
}
