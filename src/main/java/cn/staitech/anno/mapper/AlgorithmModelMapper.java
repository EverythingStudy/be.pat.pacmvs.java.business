package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AlgorithmModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AlgorithmModelMapper extends BaseMapper<AlgorithmModel> {
    /**
     * 查询算法
     */
    List<AlgorithmModel> selectByPrimaryKey(AlgorithmModel algorithmModel);


}