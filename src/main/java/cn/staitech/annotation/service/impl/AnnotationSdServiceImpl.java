package cn.staitech.annotation.service.impl;

import cn.staitech.annotation.domain.Annotation;
import cn.staitech.annotation.domain.AnnotationSd;
import cn.staitech.annotation.mapper.AnnotationSdMapper;
import cn.staitech.annotation.service.AnnotationSdService;
import cn.staitech.annotation.vo.anno.AnnotationSdReq;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛差
 */
@Slf4j
@Service
public class AnnotationSdServiceImpl extends ServiceImpl<AnnotationSdMapper, AnnotationSd> implements AnnotationSdService {
    /**
     * 获取筛差数据
     *
     * @param req 获取筛差数据
     * @return 获取筛差数据
     */
    @Override
    public List<Annotation> selectLists(AnnotationSdReq req) {
        List<Annotation> list = new ArrayList<>();
        LambdaQueryWrapper<AnnotationSd> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnnotationSd::getSingleSlideId, req.getSingleId());
        List<AnnotationSd> annotationSds = this.list(queryWrapper);
        for (AnnotationSd sd : annotationSds) {
            Annotation annotation = new Annotation();
            BeanUtils.copyProperties(sd, annotation);
            list.add(annotation);
        }
        return list;
    }
}




