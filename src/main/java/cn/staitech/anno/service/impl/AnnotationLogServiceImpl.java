package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.AnnotationLog;
import cn.staitech.anno.mapper.AnnotationLogMapper;
import cn.staitech.anno.service.AnnotationLogService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标注 服务层实现 .
 *
 * @author staitech
 */
@Service
public class AnnotationLogServiceImpl implements AnnotationLogService {


    @Resource
    private AnnotationLogMapper annotationLogMapper;

    @Override
    public int insertAnnotationLogList(@Param("annotationLogList") List<AnnotationLog> annotationLogList) {
        return annotationLogMapper.insertAnnotationLogList(annotationLogList);
    }

    @Override
    public List<AnnotationLog> selectAnnotationLogById(Long annotationId) {
        return annotationLogMapper.selectAnnotationLogById(annotationId);
    }

    @Override
    public List<AnnotationLog> selectAnnotationLog() {
        return annotationLogMapper.selectAnnotationLog();
    }

    @Override
    public int insertAnnotationLog(AnnotationLog annotationLog) {
        return annotationLogMapper.insertAnnotationLog(annotationLog);
    }


}
