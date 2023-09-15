package cn.staitech.anno.project.constants;

/**
 * @author mugw
 * @version 1.0
 * @description 常量
 * @date 2023/9/14 09:44:28
 */
public class Constants {
    /********************************切片状态**************************************/
    //未开始
    public static final String  SLIDE_STATE_PREPARE = "1";
    //标注中
    public static final String  SLIDE_STATE_ANNO = "2";
    //标注完成
    public static final String  SLIDE_STATE_ANNO_FINISH = "3";
    //未复核
    public static final String  SLIDE_STATE_NO_CHECK = "4";
    //复核中
    public static final String  SLIDE_STATE_CHECKING = "5";
    //已复核
    public static final String  SLIDE_STATE_CHECKED = "6";
    //已交付
    public static final String  SLIDE_STATE_FINISH = "7";


}
