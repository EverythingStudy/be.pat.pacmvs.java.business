package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.PathologicalIndicatorCategoryLog;

import java.util.List;

public interface PathologicalIndicatorCategoryLogMapper {
    
    int deleteByPrimaryKey(Long logId);
    
    int insert(PathologicalIndicatorCategoryLog record);
    
    int insertSelective(PathologicalIndicatorCategoryLog record);
    
    PathologicalIndicatorCategoryLog selectByPrimaryKey(Long logId);
    
    PathologicalIndicatorCategoryLog selectByAnnotationIdCategoryId(PathologicalIndicatorCategoryLog record);
    
    PathologicalIndicatorCategoryLog selectByAnnotationId(Long annotationId);
    
    int updateByPrimaryKeySelective(PathologicalIndicatorCategoryLog record);
    
    int updateByPrimaryKey(PathologicalIndicatorCategoryLog record);
    
    int deleteByAnnoIdCateId(Long annotationId);
    
//    List<PathologicalIndicatorCategoryLog> selectByCategoryId(Long categoryId);
    
    int deleteByAnnotationIdCateId(PathologicalIndicatorCategoryLog record);
    
}