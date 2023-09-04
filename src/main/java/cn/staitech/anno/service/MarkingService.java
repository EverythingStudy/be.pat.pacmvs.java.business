package cn.staitech.anno.service;

import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.marking.SlideRes;
import org.yaml.snakeyaml.error.Mark;

import java.util.List;

public interface MarkingService {

    /**
     * 查看当前切片下所有的切片
     * @param slideId 标注信息
     * @return List<Slide>
     */
    List<Marking> selectList(Long slideId);


    /**
     * 根据专题查看当前专题下所有的切片
     * @param specialId 标注信息
     * @return List<Slide>
     */
    List<SlideRes> selectSlideList(Long specialId);

    /**
     * 根据切片id查询当前切片下当前标签的总数
     * @param marking 标注信息
     * @return PointCount
     */
    PointCount selectCategoryCount(Marking marking);

    /**
     * 根据切片id查询当前切片下的标签总数
     * @param slideId 切片id
     * @return List<PointCount>
     */
    List<PointCount> selectCategoryCountList(Long slideId);

    /**
     * 根据主键查询详情信息
     * @param markingId 标注id
     * @return true || false
     */
    Marking selectById(Long markingId);

    /**
     * 添加标注
     * @param marking 标注数据
     * @return true || false
     */
    int insert(Marking marking);

    /**
     * 删除标注
     * @param marking 标注数据
     * @return true || false
     */
    int update(Marking marking);

    /**
     * 更新标注点数
     * @param marking 标注数据
     * @return true || false
     */
    int updatePointCount(Marking marking);

    /**
     * 删除标注
     * @param markingId 标注id
     * @return true || false
     */
    int delete(Long markingId);
}
