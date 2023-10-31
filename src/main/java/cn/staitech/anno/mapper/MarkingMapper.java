package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.JsonExport;
import cn.staitech.anno.domain.geojson.Properties;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.domain.marking.MarkingSelectListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * viewer 标注页面 .
 *
 * @author gjt
 */
public interface MarkingMapper extends BaseMapper<Marking> {

    /**
     * 查看当前切片下所有的切片
     *
     * @param slideId 标注信息
     * @return List<Slide>
     */
    List<MarkingSelectListVO> selectList(Long slideId);

    /**
     * json文件导出的列表
     *
     * @param slideId
     * @return
     */
    List<Features> selectLists(Long slideId);


    /**
     * 与前端交互使用的列表
     *
     * @param slideId
     * @return
     */
    List<Features> selectListBy(Long slideId);


    List<MarkingSelectListVO> selectPointCountList(Long slideId);

    Properties selectBy(String markingId);

    List<Properties> selectMeasureList(Long slideId);

    JsonExport jsonExportSelect(Long slide);

    JsonExport reviewJsonExportSelect(Long slideId);

    /**
     * 根据专题查看当前专题下所有的切片
     *
     * @param specialId 标注信息
     * @return List<Slide>
     */
    List<SlideRes> selectSlideList(Long specialId);

    /**
     * 根据切片id查询当前切片下当前标签的总数
     *
     * @param marking 标注信息
     * @return PointCount
     */
    PointCount selectCategoryCount(Marking marking);

    /**
     * 根据切片id查询当前切片下的标签总数
     *
     * @param slideId 切片id
     * @return List<PointCount>
     */
    List<PointCount> selectCategoryCountList(Long slideId);

//    /**
//     * 根据主键查询详情信息
//     * @param markingId 标注id
//     * @return true || false
//     */
//    Marking selectById(Long markingId);

    /**
     * 添加标注
     * @param marking 标注数据
     * @return true || false
     */
//    int insert(Marking marking);

    /**
     * 删除标注
     * @param marking 标注数据
     * @return true || false
     */
//    int update(Marking marking);


    /**
     * 更新标注点数
     *
     * @param marking 标注数据
     * @return true || false
     */
    int updatePointCount(Marking marking);

    /**
     * 删除标注
     *
     * @param markingId 标注id
     * @return true || false
     */
    int delete(String markingId);


}
