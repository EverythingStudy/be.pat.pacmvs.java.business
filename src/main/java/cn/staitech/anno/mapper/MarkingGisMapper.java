package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.marking.AnnotationDistance;
import com.baomidou.dynamic.datasource.annotation.DS;

@DS("slave")
public interface MarkingGisMapper {

    AnnotationDistance stClosestPoint(AnnotationDistance annotation);

    AnnotationDistance stDistance(AnnotationDistance annotation);

    AnnotationDistance avgDistance(AnnotationDistance annotation);
}
