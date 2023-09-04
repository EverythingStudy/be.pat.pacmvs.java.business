package cn.staitech.anno.mapper;

import java.util.List;

import cn.staitech.anno.domain.AlgorithmSpecialImage;

public interface AlgorithmSpecialImageMapper {
    int deleteByPrimaryKey(Long algorithmSpecialImageId);

    int insert(AlgorithmSpecialImage record);

    int insertSelective(AlgorithmSpecialImage record);

    AlgorithmSpecialImage selectByPrimaryKey(Long algorithmSpecialImageId);

    int updateByPrimaryKeySelective(AlgorithmSpecialImage record);

    int updateByPrimaryKey(AlgorithmSpecialImage record);
    
    List<AlgorithmSpecialImage> getListByCondition(AlgorithmSpecialImage algorithmSpecialImage);
}