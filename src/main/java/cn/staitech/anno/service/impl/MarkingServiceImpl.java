package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.domain.vo.BroadcastVO;
import cn.staitech.anno.domain.vo.marking.out.MarkingSelectListVo;
import cn.staitech.anno.mapper.MarkingMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.utils.CustomizationIdUtils;
import cn.staitech.anno.utils.SendMessage;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.staitech.anno.domain.geojson.Properties;
import org.springframework.transaction.annotation.Transactional;


import static cn.staitech.anno.constant.AnnotationConstant.*;
import static cn.staitech.anno.constant.ViewerConstant.MICRON;

@Service
public class MarkingServiceImpl implements MarkingService {

    @Resource
    private SlideMapper slideMapper;


    @Override
    public List<MarkingSelectListVo> selectList(Long slideId) {
        List<MarkingSelectListVo> markingSelectListVoList = markingMapper.selectList(slideId);
        List<MarkingSelectListVo> pointCountList = markingMapper.selectPointCountList(slideId);
        markingSelectListVoList = Stream.of(markingSelectListVoList, pointCountList).flatMap(list -> list.stream().map(x -> (MarkingSelectListVo) x)).collect(Collectors.toList());
        return markingSelectListVoList;
    }


    @Override
    public List<SlideRes> selectSlideList(Long specialId) {
        return markingMapper.selectSlideList(specialId);
    }

    @Override
    public PointCount selectCategoryCount(Marking marking) {
        return markingMapper.selectCategoryCount(marking);
    }

    @Override
    public List<PointCount> selectCategoryCountList(Long slideId) {
        return markingMapper.selectCategoryCountList(slideId);
    }

    @Resource
    private MarkingMapper markingMapper;

    @Override
    public Marking selectById(Long markingId) {
        return markingMapper.selectById(markingId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(viewAddIn req) throws Exception {
        Slide slideBy = slideMapper.selectById(req.getSlide_id());
        if (slideBy == null) {
            throw new Exception("未查询到切片信息");
        }
        Marking marking = new Marking();
        BeanUtils.copyProperties(req, marking);
        // 拼接标注名称
        String annotationId = CustomizationIdUtils.getSdId();
        marking.setAnnotation_id(annotationId);
        if(req.getArea() != null){
            Double area = new Double(req.getArea()) * MICRON;
            marking.setArea(String.valueOf(area));
        }
        if(req.getPerimeter() != null){
            Double perimeter = new Double(req.getPerimeter()) * MICRON;
            marking.setPerimeter(String.valueOf(perimeter));
        }
        marking.setCreate_by(SecurityUtils.getUserId());
        marking.setAnnotation_type("Draw");
        marking.setCreate_time(new Date());
        // 查询
        QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
        // 根据切片和测量轮廓名称查询最大值
        markingQueryWrapper.eq("slide_id", req.getSlide_id()).eq("measure_name", req.getMeasure_name()).orderByDesc();
        Marking markingBy = markingMapper.selectOne(markingQueryWrapper);
        Integer number = 1;
        if (markingBy != null) {
            if (markingBy.getNumber() != null) {
                number += markingBy.getNumber();
            }
        }
        marking.setNumber(number);
        // 添加数据库，添加后返回自增id
        markingMapper.insert(marking);
        Properties properties = markingMapper.selectBy(marking.getMarking_id());
        Features features = socketData(annotationId, req.getGeometry(), properties);
        // 如果是点类型，返回点的总数并返回
        List<PointCount> pointCountList = updatePoint(req.getLocation_type(), markingBy);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features, pointCountList);
        NioWebSocketHandler.sendAll(req.getSlide_id(), broadcastVO);
        updateSLide(marking.getSlide_id());
        return marking.getMarking_id();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(MarkingUpdateIn req) throws Exception {
        // 查询标注表中信息
        Marking markingBy = markingMapper.selectById(req.getMarking_id());
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception("未查询到标注信息");
        }
        // 查询标注表中信息
        Slide slide = slideMapper.selectById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception("未查询到切片信息");
        }
        Double area = new Double(req.getArea()) * MICRON;
        Double perimeter = new Double(req.getPerimeter()) * MICRON;
        // 更新前数据
        // 更新文件中的内容
        Marking marking = new Marking();
        BeanUtils.copyProperties(req, marking);
        marking.setUpdate_by(SecurityUtils.getUserId());
        marking.setUpdate_time(new Date());
        marking.setArea(String.valueOf(area));
        marking.setPerimeter(String.valueOf(perimeter));
        List<PointCount> pointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
        markingMapper.updateById(marking);
        // 判断标签
        if(req.getCategory_id() != null){
            if (req.getCategory_id() != 0 && !req.getCategory_id().equals(markingBy.getCategory_id())) {
                markingBy.setCategory_id(req.getCategory_id());
                List<PointCount> newPointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
                pointCountList = Stream.of(pointCountList, newPointCountList).flatMap(list -> list.stream().map(x -> (PointCount) x)).collect(Collectors.toList());
            }
        }
        Properties properties = markingMapper.selectBy(marking.getMarking_id());
        Features features = socketData(markingBy.getAnnotation_id(), req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features, pointCountList);
        // 使用websocket发送数据
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        // 更新切片表中数据
        updateSLide(slide.getSlideId());
        return markingBy.getMarking_id();
    }

    @Override
    public int updatePointCount(Marking marking) {
        return markingMapper.updatePointCount(marking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long markingId) throws Exception {
        if (!Optional.ofNullable(markingId).isPresent()) {
            throw new Exception("参数异常");
        }
        Marking markingBy = markingMapper.selectById(markingId);
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception("未查询到标注信息");
        }
        Slide slide = slideMapper.selectById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception("未查询到切片信息");
        }
        Properties properties = markingMapper.selectBy(markingId);
        markingMapper.delete(markingId);
        Features features = socketData(markingBy.getAnnotation_id(), markingBy.getGeometry(), properties);
        List<PointCount> pointCountList = updatePoint(markingBy.getLocation_type(), markingBy);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(DELETE_STATUS, features, pointCountList);
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        int res = markingMapper.delete(markingId);
        updateSLide(slide.getSlideId());
        return res;
    }

    /**
     * 封装socket发送数据
     *
     * @param annotationId
     * @param geometry
     * @param properties
     * @return
     */
    public Features socketData(String annotationId, JSONObject geometry, Properties properties) {
        Features features = new Features();
        features.setGeometry(geometry);
        features.setId(annotationId);
        features.setType("Feature");
        features.setProperties(properties);
        return features;
    }

    /**
     * 统计不同类型点的数量
     *
     * @param locationType
     * @param marking
     * @return
     */
    public List<PointCount> updatePoint(String locationType, Marking marking) {
        List<PointCount> pointCountList = new ArrayList<>();
        if (Objects.equals(locationType, "Point")) {
            PointCount pointCounts = markingMapper.selectCategoryCount(marking);
            marking.setPoint_count(pointCounts.getPoint_count());
            markingMapper.updatePointCount(marking);
            pointCountList.add(pointCounts);
        }
        return pointCountList;
    }


    /**
     * 更新切片表中数据
     *
     * @param slideId 切片id
     * @return
     */
    public void updateSLide(Long slideId) {
        Slide slide = new Slide();
        slide.setSlideId(slideId);
        slide.setUpdateTime(new Date());
        slideMapper.updateById(slide);
    }


}
