package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.image.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lif
 * @description 针对表【tb_image】的数据库操作Mapper
 * @createDate 2024-09-10 10:21:48
 * @Entity cn.staitech.anno.domain/image.Image
 */
public interface ImageMapper extends BaseMapper<Image> {

    Integer selectFrSlideCountByImageId(Long imageId);
}




