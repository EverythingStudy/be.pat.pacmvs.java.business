package cn.staitech.anno.enums;

/**
 * 切片复核枚举
 *
 * @author YL
 */
public enum ProcessFlagEnum {
    // 标注状态(0未开始 1标注中 2标注完成 3已提交复核)
    STATUS_INFO_0(0, "0未开始"),
    STATUS_INFO_1(1, "1标注中"),
    STATUS_INFO_2(2, "2标注完成"),
    STATUS_INFO_3(3, "3已提交复核");
    private int value;
    private String label;

    ProcessFlagEnum(int value, String label) {
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
