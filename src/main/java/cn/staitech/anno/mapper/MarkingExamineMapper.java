package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.Properties;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

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

    /**
     * 查询详情接口
     * @param questionProjectId
     * @return
     */
    List<Features> selectLists(Long questionProjectId);
}
