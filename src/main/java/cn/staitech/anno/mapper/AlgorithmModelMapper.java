package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AlgorithmModel;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface AlgorithmModelMapper extends BaseMapper<AlgorithmModel> {
    /**
     * 查询算法
     * */
    List<AlgorithmModel> selectByPrimaryKey(AlgorithmModel algorithmModel);


}