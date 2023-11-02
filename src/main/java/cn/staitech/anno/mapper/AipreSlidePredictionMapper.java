package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AipreSlidePrediction;

public interface AipreSlidePredictionMapper {
    int deleteByPrimaryKey(Long slidePredictionId);

    int insert(AipreSlidePrediction record);

    int insertSelective(AipreSlidePrediction record);

    AipreSlidePrediction selectByPrimaryKey(Long slidePredictionId);

    int updateByPrimaryKeySelective(AipreSlidePrediction record);

    int updateByPrimaryKey(AipreSlidePrediction record);
}