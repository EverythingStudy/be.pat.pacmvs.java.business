package cn.staitech.annotation.vo.anno;

import cn.staitech.annotation.domain.Annotation;
import lombok.Data;

/**
 * 筛查
 *
 * @author yxy
 */
@Data
public class AnnotationSdVo extends Annotation {
    /**
     * 坐标
     */
    private String contour;
    /**
     * 单切片ID
     */
    private Long singleSlideId;
}
