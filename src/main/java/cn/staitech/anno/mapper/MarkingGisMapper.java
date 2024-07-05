package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.marking.AnnotationDistance;

public interface MarkingGisMapper {

    AnnotationDistance stClosestPoint(AnnotationDistance annotation);

    AnnotationDistance stDistance(AnnotationDistance annotation);

    AnnotationDistance avgDistance(AnnotationDistance annotation);
}
