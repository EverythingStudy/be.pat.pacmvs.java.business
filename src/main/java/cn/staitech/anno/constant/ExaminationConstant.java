package cn.staitech.anno.constant;

/**
 * .
 *
 * @author admin
 */
public class ExaminationConstant {
    
    private ExaminationConstant() {
        throw new IllegalStateException("ExaminationConstant class");
    }
    
    public static final String FILE_PATH = "annotation";
    
    public static final String FILE_PATH_FORMAT = ".json";
    
    public static final String CHARACTER_ENCODING = "UTF-8";
    
    public static final String CONTENT_TYPE = "application/json;charset=utf-8";
    
    public static final String HEADER = "Content-Disposition";
    
    public static final Integer NOT_START_REVIEW = 0;

    public static final Integer SUBMIT_REVIEW = 3;
    
}
