package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.AlgorithmModel;
import cn.staitech.anno.mapper.AlgorithmModelMapper;
import cn.staitech.anno.service.AlgorithmModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AlgorithmModelServiceImpl extends ServiceImpl<AlgorithmModelMapper, AlgorithmModel> implements AlgorithmModelService {

    @Resource
    private AlgorithmModelMapper algorithmModelMapper;

//    /**
//     * 查询算法
//     * */
//    @Override
//    public List<AlgorithmModel> selectByPrimaryKey(){
//        AlgorithmModel algorithmModel=new AlgorithmModel();
//        return algorithmModelMapper.selectByPrimaryKey(algorithmModel);
//    }

}
