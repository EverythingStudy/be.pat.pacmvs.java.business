package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.Properties;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * Mapper 接口
 * @author gjt
 * @since 2023-09-25
 */
@DS("sharding")
public interface MarkingExamineMapper extends BaseMapper<MarkingExamine> {

    /**
     * 查询单个切片详情信息
     *
     * @param markingExamineId 考核标注id
     * @return
     */
    Properties selectBy(Long markingExamineId);

    /**
     * 查询详情接口
     *
     * @param markingExamine
     * @return
     */
    List<Features> selectLists(MarkingExamine markingExamine);

    /**
     * 查询详情接口(json)
     *
     * @param markingExamine
     * @return
     */
    List<Features> selectListBy(MarkingExamine markingExamine);
}
