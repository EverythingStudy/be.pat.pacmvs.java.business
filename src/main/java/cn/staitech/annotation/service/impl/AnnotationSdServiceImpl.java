package cn.staitech.annotation.service.impl;

import cn.staitech.annotation.domain.AnnotationSd;
import cn.staitech.annotation.mapper.AnnotationSdMapper;
import cn.staitech.annotation.service.AnnotationSdService;
import cn.staitech.annotation.vo.anno.AnnotationSdReq;
import cn.staitech.annotation.vo.anno.AnnotationSdVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public List<AnnotationSdVo> selectLists(AnnotationSdReq req) {
        return this.baseMapper.selectLists(req.getSingleId());
    }
}




