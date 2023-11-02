package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.SlidePrediction;

public interface SlidePredictionMapper {
    int deleteByPrimaryKey(Long slidePredictionId);

    int insert(SlidePrediction record);

    int insertSelective(SlidePrediction record);

    SlidePrediction selectByPrimaryKey(Long slidePredictionId);

    int updateByPrimaryKeySelective(SlidePrediction record);

    int updateByPrimaryKey(SlidePrediction record);
}