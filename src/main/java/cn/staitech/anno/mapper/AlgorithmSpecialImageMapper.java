package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AlgorithmSpecialImage;

import java.util.List;

public interface AlgorithmSpecialImageMapper {
    int deleteByPrimaryKey(Long algorithmSpecialImageId);

    int insert(AlgorithmSpecialImage record);

    int insertSelective(AlgorithmSpecialImage record);

    AlgorithmSpecialImage selectByPrimaryKey(Long algorithmSpecialImageId);

    int updateByPrimaryKeySelective(AlgorithmSpecialImage record);

    int updateByPrimaryKey(AlgorithmSpecialImage record);

    List<AlgorithmSpecialImage> getListByCondition(AlgorithmSpecialImage algorithmSpecialImage);
}