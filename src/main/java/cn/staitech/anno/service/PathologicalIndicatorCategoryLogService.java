package cn.staitech.anno.service;

import cn.staitech.anno.domain.PathologicalIndicatorCategoryLog;


@SuppressWarnings("checkstyle:EmptyLineSeparator")
public interface PathologicalIndicatorCategoryLogService {
    
    
    @SuppressWarnings("checkstyle:ParameterName")
    int insertSelective(PathologicalIndicatorCategoryLog Pathological) throws Exception;
    
    String deleteByPrimaryKey(Long logId) throws Exception;
    
    String deleteByAnnoIdCateId(Long annotationId) throws Exception;
    
    PathologicalIndicatorCategoryLog selectByPrimaryKey(Long logId);
    
    String deleteByAnnotationIdCateId(PathologicalIndicatorCategoryLog record) throws Exception;
    
    
    PathologicalIndicatorCategoryLog selectByAnnotationId(Long annotationId);
    
    
}
