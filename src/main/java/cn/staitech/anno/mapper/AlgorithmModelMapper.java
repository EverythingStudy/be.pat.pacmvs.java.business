package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AlgorithmModel;

public interface AlgorithmModelMapper {
    int deleteByPrimaryKey(Long modelId);

    int insert(AlgorithmModel record);

    int insertSelective(AlgorithmModel record);

    AlgorithmModel selectByPrimaryKey(Long modelId);

    int updateByPrimaryKeySelective(AlgorithmModel record);

    int updateByPrimaryKey(AlgorithmModel record);
}