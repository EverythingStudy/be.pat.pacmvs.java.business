package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.PathologicalIndicatorCategoryLog;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryLogMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.service.PathologicalIndicatorCategoryLogService;
import cn.staitech.anno.service.impl.manage.PathologicalLogManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class PathologicalIndicatorCategoryLogServicelmpl implements PathologicalIndicatorCategoryLogService {
    
    @Resource
    private PathologicalIndicatorCategoryLogMapper pathologicalIndicatorCategoryLogMapper;
    
    @Resource
    private PathologicalLogManage pathologicalLogManage;
    
    @Resource
    private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;
    
    @SuppressWarnings("checkstyle:ParameterName")
    @Override
    public int insertSelective(PathologicalIndicatorCategoryLog PathologicalLog) throws Exception {
        
        // 添加时查询标注是否已经存在
        PathologicalIndicatorCategoryLog pathologicalIndicatorCategoryLog = pathologicalIndicatorCategoryLogMapper.selectByAnnotationIdCategoryId(
                PathologicalLog);
        
        if (pathologicalIndicatorCategoryLog != null) {
            
            throw new Exception("该类别已经存在");
        }
        
        return pathologicalLogManage.insertSelective(PathologicalLog);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteByPrimaryKey(Long logId) throws Exception {
        
        int res = pathologicalIndicatorCategoryLogMapper.deleteByPrimaryKey(logId);
        
        if (res <= 0) {
            throw new Exception("删除失败");
        }
        return "删除成功";
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteByAnnoIdCateId(Long annotationId) throws Exception {
        
        // 查询标注记录是否存在
        
        PathologicalIndicatorCategoryLog pathologicalIndicatorCategoryLog = pathologicalIndicatorCategoryLogMapper.selectByAnnotationId(
                annotationId);
        
        if (pathologicalIndicatorCategoryLog != null) {
            return "该标注尚未选择标注类别";
        }
        
        int res = pathologicalIndicatorCategoryLogMapper.deleteByAnnoIdCateId(annotationId);
        
        if (res <= 0) {
            throw new Exception("删除失败");
        }
        return "删除成功";
    }
    
    @Override
    public PathologicalIndicatorCategoryLog selectByPrimaryKey(Long logId) {
        return pathologicalIndicatorCategoryLogMapper.selectByPrimaryKey(logId);
    }
    
    @Override
    public PathologicalIndicatorCategoryLog selectByAnnotationId(Long annotationId) {
        return pathologicalIndicatorCategoryLogMapper.selectByAnnotationId(annotationId);
    }
    
    @Override
    public String deleteByAnnotationIdCateId(PathologicalIndicatorCategoryLog record) throws Exception {
        
        // 查询是否存在
        PathologicalIndicatorCategoryLog pathologicalIndicatorCategoryLog = pathologicalIndicatorCategoryLogMapper.selectByAnnotationIdCategoryId(
                record);
        
        if (pathologicalIndicatorCategoryLog != null) {
            
            pathologicalLogManage.deleteByAnnotationIdCateId(record);
            
            return "删除成功";
        } else {
            return "删除失败";
        }
    }
    
    
}
