package cn.staitech.anno.mapper;


import cn.staitech.anno.domain.Annotation;
import cn.staitech.anno.domain.AnnotationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标注日志表 数据层 .
 *
 * @author staitech
 */
public interface AnnotationLogMapper {
    
    /**
     * 批量插入标注日志 .
     * @param annotationLogList  标注日志信息
     * @return True || false
     */
    int insertAnnotationLogList(@Param("annotationLogList") List<AnnotationLog> annotationLogList);
    
    List<AnnotationLog> selectAnnotationLogById (Long annotationId);
    
    List<AnnotationLog> selectAnnotationLog();
    
    int insertAnnotationLog(AnnotationLog annotationLog);
    
    
    

}