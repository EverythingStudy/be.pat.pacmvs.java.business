package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.special.SpecialAnnotation;
import cn.staitech.anno.domain.vo.specialimageanno.in.SpecialAnnoProperties;
import cn.staitech.anno.domain.vo.specialsliceimage.OrganDict;

import java.util.List;

public interface SpecialAnnotationMapper {
    int deleteByPrimaryKey(Long sliceAnnotationId);

    int insert(SpecialAnnotation record);

    int insertSelective(SpecialAnnotation record);


    /**
     * 更新标注点数
     *
     * @param marking 标注数据
     * @return true || false
     */
    int updatePointCount(SpecialAnnotation record);


    SpecialAnnotation selectByPrimaryKey(Long sliceAnnotationId);

    int updateByPrimaryKeySelective(SpecialAnnotation record);

    int updateByPrimaryKeyWithBLOBs(SpecialAnnotation record);

    int updateByPrimaryKey(SpecialAnnotation record);

//    int insertSpecialAnnotationList(@Param("list") List<SpecialAnnotation> list);

    List<SpecialAnnotation> selectSpecialAnnotationList(SpecialAnnotation specialAnnotation);


    List<SpecialAnnoProperties> selectSpecialPropertiesList(SpecialAnnotation specialAnnotation);


    //匹配新增字段之前的表
    List<SpecialAnnotation> selectAnnotationList(SpecialAnnotation specialAnnotation);

    List<OrganDict> getSystemDict(OrganDict dict);

    /**
     * 根据切片id查询当前切片下当前标签的总数
     *
     * @param marking 标注信息
     * @return PointCount
     */
    PointCount selectCategoryCount(SpecialAnnotation specialAnnotation);
}