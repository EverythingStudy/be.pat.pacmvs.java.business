package cn.staitech.anno.service;

import cn.staitech.anno.domain.AlgorithmModel;

import java.util.List;

public interface AlgorithmModelService {

    /**
     * 查询算法
     * */
    List<AlgorithmModel> selectByPrimaryKey();
}
