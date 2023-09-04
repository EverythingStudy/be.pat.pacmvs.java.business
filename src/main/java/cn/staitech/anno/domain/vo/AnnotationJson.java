package cn.staitech.anno.domain.vo;

import cn.staitech.anno.domain.Annotation;
import lombok.Data;

import java.util.List;

@Data
public class AnnotationJson {
    
    private List<Annotation> annotationList;
}
