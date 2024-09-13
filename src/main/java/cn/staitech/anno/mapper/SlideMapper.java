package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Slide;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 切片 数据层
 *
 * @author staitech
 */
public interface SlideMapper extends BaseMapper<Slide> {

    /**
     * 项目id 和图片id 查询是否存在
     *
     * @param
     * @return 结果
     */
    List<Slide> selectImageExist(Slide slide);


}