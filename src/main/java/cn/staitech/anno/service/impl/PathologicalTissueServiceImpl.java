package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.AlgorithmModel;
import cn.staitech.anno.mapper.AlgorithmModelMapper;
import cn.staitech.anno.mapper.PathologicalTissueMapper;
import cn.staitech.anno.service.PathologicalTissueService;
import cn.staitech.anno.vo.pathologicaltissue.PathologicalTissueVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PathologicalTissueServiceImpl implements PathologicalTissueService {


    @Resource
    private PathologicalTissueMapper pathologicalTissueMapper;

    @Resource
    private AlgorithmModelMapper algorithmModelMapper;

    /**
     * 查询项目类型下的病理组织和算法模型
     * */
    @Override
   public List<PathologicalTissueVO> selectByPrimaryKey(Long projectTypeId){
        List<PathologicalTissueVO> pathologicalTissueVOList=pathologicalTissueMapper.selectByPrimaryKey(projectTypeId);
        for (PathologicalTissueVO pathologicalTissueVO:pathologicalTissueVOList){
            AlgorithmModel algorithmModel=AlgorithmModel.builder().tissueId(pathologicalTissueVO.getTissueId()).build();
            List<AlgorithmModel> algorithmModelList=algorithmModelMapper.selectByPrimaryKey(algorithmModel);
            pathologicalTissueVO.setModels(algorithmModelList);
        }

        return pathologicalTissueVOList;
    }
}
