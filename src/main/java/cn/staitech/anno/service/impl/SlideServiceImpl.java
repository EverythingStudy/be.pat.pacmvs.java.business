package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.service.SlideService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 切片 服务层实现
 *
 * @author staitech
 */
@Slf4j
@Service
public class SlideServiceImpl extends ServiceImpl<SlideMapper, Slide> implements SlideService {
    @Resource
    private SlideMapper slideMapper;

    /**
     * 项目id 和图片id 查询是否存在
     *
     * @param
     * @return 结果
     */
    @Override
    public List<Slide> selectImageExist(Slide slide) {
        return slideMapper.selectImageExist(slide);
    }

}
