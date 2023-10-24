package cn.staitech.anno.project.mapper;

import cn.staitech.anno.project.domain.SlideAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 86186
 * @description 针对表【tb_slide_attr(切片属性表)】的数据库操作Mapper
 * @createDate 2023-09-14 13:12:56
 * @Entity cn.staitech.anno.project.domain.SlideAttr
 */
public interface SlideAttrMapper extends BaseMapper<SlideAttr> {
    @Select("select group_concat(attr_id) from tb_slide_attr where del_flag='0' and  attr_type ='2' and slide_id =#{slideId}")
    String selectCategoryIds(Long slideId);
}




