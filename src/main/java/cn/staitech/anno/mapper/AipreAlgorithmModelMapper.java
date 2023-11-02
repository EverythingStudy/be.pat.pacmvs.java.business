package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AipreAlgorithmModel;

public interface AipreAlgorithmModelMapper {
    int deleteByPrimaryKey(Long modelId);

    int insert(AipreAlgorithmModel record);

    int insertSelective(AipreAlgorithmModel record);

    AipreAlgorithmModel selectByPrimaryKey(Long modelId);

    int updateByPrimaryKeySelective(AipreAlgorithmModel record);

    int updateByPrimaryKey(AipreAlgorithmModel record);
}