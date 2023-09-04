package cn.staitech.anno.service;


import cn.staitech.anno.domain.AnnotationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标注日志 服务层  .
 *
 * @author staitech
 */

public interface AnnotationLogService {
    
    int insertAnnotationLogList(@Param("annotationLogList") List<AnnotationLog> annotationLogList);
    
    List<AnnotationLog> selectAnnotationLogById (Long annotationId);
    
    List<AnnotationLog> selectAnnotationLog();
    
    int insertAnnotationLog(AnnotationLog annotationLog);
    

}