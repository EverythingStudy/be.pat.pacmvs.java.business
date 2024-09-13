package cn.staitech.anno.service;

import cn.staitech.anno.domain.Slide;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 切片 服务层 .
 *
 * @author staitech
 */
public interface SlideService extends IService<Slide> {

    /**
     * 项目id 和图片id 查询是否存在
     *
     * @param
     * @return 结果
     */
    List<Slide> selectImageExist(Slide slide);

}