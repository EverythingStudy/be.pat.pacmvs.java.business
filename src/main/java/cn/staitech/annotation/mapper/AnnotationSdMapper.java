package cn.staitech.annotation.mapper;

import cn.staitech.annotation.domain.AnnotationSd;
import cn.staitech.annotation.vo.anno.AnnotationSdVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 筛差
 */
public interface AnnotationSdMapper extends BaseMapper<AnnotationSd> {

    List<AnnotationSdVo> selectLists(@NotNull(message = "单切片id不能为空") Long singleId);
}




