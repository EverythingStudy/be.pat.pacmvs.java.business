package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Color;
import cn.staitech.anno.mapper.ColorMapper;
import cn.staitech.anno.service.ColorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 颜色
 */
@Service
class ColorServiceImpl extends ServiceImpl<ColorMapper, Color> implements ColorService {

}
