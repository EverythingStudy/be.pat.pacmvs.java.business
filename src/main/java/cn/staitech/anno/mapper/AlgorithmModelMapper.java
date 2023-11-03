package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AlgorithmModel;

import java.util.List;

public interface AlgorithmModelMapper {
    /**
     * 查询算法
     * */
    List<AlgorithmModel> selectByPrimaryKey(AlgorithmModel algorithmModel);


}