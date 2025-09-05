package cn.staitech.annotation.service;

import cn.staitech.annotation.domain.Annotation;
import cn.staitech.annotation.domain.AnnotationSd;
import cn.staitech.annotation.vo.anno.AnnotationSdReq;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 筛差
 */
public interface AnnotationSdService extends IService<AnnotationSd> {
    /**
     * 获取筛差数据
     *
     * @param req 获取筛差数据
     * @return 获取筛差数据
     */
    List<Annotation> selectLists(AnnotationSdReq req);
}
