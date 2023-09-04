package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.mapper.MarkingMapper;
import cn.staitech.anno.service.MarkingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MarkingServiceImpl implements MarkingService {


    @Override
    public List<Marking> selectList(Long slideId){
        return markingMapper.selectList(slideId);
    }


    @Override
    public List<SlideRes> selectSlideList(Long specialId){
        return markingMapper.selectSlideList(specialId);
    }

    @Override
    public PointCount selectCategoryCount(Marking marking){
        return markingMapper.selectCategoryCount(marking);
    }

    @Override
    public List<PointCount> selectCategoryCountList(Long slideId){
        return markingMapper.selectCategoryCountList(slideId);
    }

    @Resource
    private MarkingMapper markingMapper;

    @Override
    public Marking selectById(Long markingId) {
        return markingMapper.selectById(markingId);
    }

    @Override
    public int insert(Marking marking) {
        return markingMapper.insert(marking);
    }

    @Override
    public int update(Marking marking) {
        return markingMapper.update(marking);
    }

    @Override
    public int updatePointCount(Marking marking) {
        return markingMapper.updatePointCount(marking);
    }

    @Override
    public int delete(Long markingId) {
        return markingMapper.delete(markingId);
    }

}
