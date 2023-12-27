package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.JsonExport;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.markMeasure.MarkMeasure;
import cn.staitech.anno.vo.marking.MarkingSelectListVO;
import cn.staitech.anno.vo.marking.PointCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标注测量表 Mapper 接口
 * </p>
 *
 * @author wanglibei
 * @since 2023-12-05
 */
public interface MarkMeasureMapper extends BaseMapper<MarkMeasure> {
    int selectListCount(Map<String, Object> map);

    List<MarkingSelectListVO> selectPointCountList(Map<String, Object> map);

    List<MarkingSelectListVO> selectList(Map<String, Object> map);

    List<Features> selectListBy(Long slideId);

    Properties selectBy(String markingId);

    PointCount selectCategoryCount(MarkMeasure marking);

    int updatePointCount(MarkMeasure marking);

    int delete(String markingId);

    List<Properties> selectMeasureList(Long slideId);

    List<Features> selectLists(Long slideId);

    JsonExport jsonExportSelect(Long slide);

    JsonExport reviewJsonExportSelect(Long slideId);


}
