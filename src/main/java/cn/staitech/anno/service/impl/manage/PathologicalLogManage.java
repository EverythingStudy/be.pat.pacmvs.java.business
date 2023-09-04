package cn.staitech.anno.service.impl.manage;

import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.PathologicalIndicatorCategoryLog;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class PathologicalLogManage {
    
    @Resource
    private PathologicalIndicatorCategoryLogMapper pathologicalIndicatorCategoryLogMapper;
    
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Transactional(rollbackFor = Exception.class)
    public int deleteByAnnotationIdCateId(PathologicalIndicatorCategoryLog record) {
        
        return pathologicalIndicatorCategoryLogMapper.deleteByAnnotationIdCateId(record);
    }
    
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:ParameterName"})
    @Transactional(rollbackFor = Exception.class)
    public int insertSelective(PathologicalIndicatorCategoryLog PathologicalLog) throws Exception {
        return pathologicalIndicatorCategoryLogMapper.insertSelective(PathologicalLog);
    }
    
}
