package cn.staitech.anno.service.impl.manage;

import cn.staitech.anno.domain.Annotation;
import cn.staitech.anno.mapper.AnnotationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class AnnotationManage {

    @Resource
    private AnnotationMapper annotationMapper;

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Transactional(rollbackFor = Exception.class)
    public int updateAnnotation(Annotation annotation) {
        return annotationMapper.updateAnnotation(annotation);

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Transactional(rollbackFor = Exception.class)
    public int deleteAnnotationById(Long annotationId) {
        return annotationMapper.deleteAnnotationById(annotationId);
    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Transactional(rollbackFor = Exception.class)
    public int insertAnnotation(Annotation annotation) {
        return annotationMapper.insertAnnotation(annotation);
    }

}





