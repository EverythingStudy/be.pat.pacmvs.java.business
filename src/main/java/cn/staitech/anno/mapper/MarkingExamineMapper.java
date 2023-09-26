package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.domain.geojson.Properties;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
public interface MarkingExamineMapper extends BaseMapper<MarkingExamine> {

    /**
     * 查询单个切片详情信息
     * @param markingExamineId 考核标注id
     * @return
     */
    Properties selectBy(Long markingExamineId);
}
