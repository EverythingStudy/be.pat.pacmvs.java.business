package cn.staitech.anno.project.service;

import cn.staitech.anno.project.domain.SlideAttr;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 86186
* @description 针对表【tb_slide_attr(切片属性表)】的数据库操作Service
* @createDate 2023-09-14 13:12:56
*/
public interface SlideAttrService extends IService<SlideAttr> {
    public Boolean saveAnnoUsers(Long slideId, List<Long> userIds)throws Exception;
    public Boolean removeAnnoUsers(Long slideId, List<Long> userIds)throws Exception;
    public Boolean saveAnnoCategory(Long slideId, List<Long> categoryIds)throws Exception;
    public Boolean removeAnnoCategory(Long slideId, List<Long> categoryIds)throws Exception;
}
